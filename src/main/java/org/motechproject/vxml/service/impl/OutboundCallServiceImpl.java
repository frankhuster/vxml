package org.motechproject.vxml.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.vxml.CallInitiationException;
import org.motechproject.vxml.EventParams;
import org.motechproject.vxml.EventSubjects;
import org.motechproject.vxml.TimestampHelper;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.CallDirection;
import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.domain.HttpMethod;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.repository.ConfigDataService;
import org.motechproject.vxml.service.MotechStatusMessage;
import org.motechproject.vxml.service.OutboundCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Generates & sends an HTTP request to an IVR provider to trigger an outbound call
 */
@Service("outboundCallService")
public class OutboundCallServiceImpl implements OutboundCallService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConfigDataService configDataService;
    private MotechStatusMessage motechStatusMessage;
    private CallDetailRecordDataService callDetailRecordDataService;
    private EventRelay eventRelay;

    @Autowired
    public OutboundCallServiceImpl(ConfigDataService configDataService, MotechStatusMessage motechStatusMessage,
                                   CallDetailRecordDataService callDetailRecordDataService, EventRelay eventRelay) {
        this.configDataService = configDataService;
        this.motechStatusMessage = motechStatusMessage;
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.eventRelay = eventRelay;
    }

    private void addCallDetailRecord(CallStatus callStatus, Config config, Map<String, String> params,
                                     String motechCallId) {
        logger.debug(String.format("addCallDetailRecord(config = %s, params = %s, motechCallId = %s)", config.getName(),
                params.toString(), motechCallId));

        String from = params.containsKey("from") ? params.get("from") : null;
        String to = params.containsKey("to") ? params.get("to") : null;
        callDetailRecordDataService.create(new CallDetailRecord(TimestampHelper.currentTime(), config.getName(), from,
                to, CallDirection.OUTBOUND, callStatus, motechCallId, null, params));
    }

    @Override
    public void initiateCall(String configName, Map<String, String> parameters) {
        logger.debug("initiateCall(configName = {}, params = {})", configName, parameters);

        Map<String, String> params = new HashMap<>(parameters);
        Config config = Config.getConfig(configDataService, motechStatusMessage, configName);

        String motechCallId = UUID.randomUUID().toString();
        Map<String, String> completeParams = new HashMap<>(params);
        completeParams.put("motechCallId", motechCallId);

        HttpUriRequest request = generateHttpRequest(config, completeParams);
        HttpResponse response;
        try {
            response = new DefaultHttpClient().execute(request);
        } catch (Exception e) {
            String message = String.format("Could not initiate call, unexpected exception: %s", e.toString());
            logger.info(message);
            motechStatusMessage.alert(message);
            params.put("ErrorMessage", message);
            addCallDetailRecord(CallStatus.FAILED, config, params, motechCallId);
            throw new CallInitiationException(message, e);
        }
        StatusLine statusLine = response.getStatusLine();

        //todo: it's possible that some IVR providers return an HTTP 200 and an error code in the response body.
        //todo: If we encounter such a provider, we'll have to beef up the response processing here
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            String message = String.format("Could not initiate call: %s", statusLine.toString());
            logger.info(message);
            motechStatusMessage.alert(message);
            params.put("ErrorMessage", message);
            addCallDetailRecord(CallStatus.FAILED, config, params, motechCallId);
            throw new CallInitiationException(message);
        }

        // Add a CDR to the database
        addCallDetailRecord(CallStatus.MOTECH_INITIATED, config, params, motechCallId);

        // Generate a MOTECH event
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParams.CONFIG, config.getName());
        if (params.containsKey("timestamp")) {
            eventParams.put(EventParams.TIMESTAMP, params.get("timestamp"));
        } else {
            eventParams.put(EventParams.TIMESTAMP, TimestampHelper.currentTime());
        }
        if (params.containsKey("to")) {
            eventParams.put(EventParams.TO, params.get("to"));
        }
        if (params.entrySet().size() > 0) {
            eventParams.put(EventParams.PROVIDER_EXTRA_DATA, params);
        }
        MotechEvent event = new MotechEvent(EventSubjects.CALL_INITIATED, eventParams);
        logger.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);
    }

    private HttpUriRequest generateHttpRequest(Config config, Map<String, String> params) {
        logger.debug("generateHttpRequest(config = {}, params = {})", config, params);

        String uri = config.getOutgoingCallUriTemplate();
        BasicHttpParams httpParams = new BasicHttpParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String placeholder = String.format("[%s]", entry.getKey());
            if (uri.contains(placeholder)) {
                uri = uri.replace(placeholder, entry.getValue());
            } else {
                httpParams.setParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpUriRequest request;
        if (HttpMethod.GET == config.getOutgoingCallMethod()) {
            request = new HttpGet(uri);
        } else {
            request = new HttpPost(uri);
        }
        request.setParams(httpParams);

        logger.debug("Generated {}", request.toString());

        return request;
    }

}
