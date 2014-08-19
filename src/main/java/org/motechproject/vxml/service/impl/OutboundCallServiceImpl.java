package org.motechproject.vxml.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.motechproject.vxml.CallInitiationException;
import org.motechproject.vxml.domain.CallDirection;
import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.domain.ConfigHelper;
import org.motechproject.vxml.repository.ConfigDataService;
import org.motechproject.vxml.service.CallDetailRecordService;
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
 * TODO
 */
@Service("outboundCallService")
public class OutboundCallServiceImpl implements OutboundCallService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConfigDataService configDataService;
    private MotechStatusMessage motechStatusMessage;
    private CallDetailRecordService callDetailRecordService;

    @Autowired
    public OutboundCallServiceImpl(ConfigDataService configDataService, MotechStatusMessage motechStatusMessage,
                                   CallDetailRecordService callDetailRecordService) {
        this.configDataService = configDataService;
        this.motechStatusMessage = motechStatusMessage;
        this.callDetailRecordService = callDetailRecordService;
    }

    @Override
    public void initiateCall(String configName, Map<String, String> params) {
        logger.info("initiateCall(configName = {}, params = {})", configName, params);

        Config config = ConfigHelper.getConfig(configDataService, motechStatusMessage, configName);

        String motechCallId = UUID.randomUUID().toString();
        Map<String, String> completeParams = new HashMap<>(params);
        completeParams.put("motechCallId", motechCallId);

        HttpGet httpGet = generateGetRequest(config, completeParams);
        HttpResponse response;
        try {
            response = new DefaultHttpClient().execute(httpGet);
        } catch (Exception e) {
            //todo: further refine that
            String message = String.format("Could not initiate call, unexpected exception: %s", e.toString());
            logger.info(message);
            motechStatusMessage.alert(message);
            throw new CallInitiationException(message);
        }
        StatusLine statusLine = response.getStatusLine();
        //todo: verify
        logger.info("HTTP Status: {}" + statusLine.toString());

        //todo: for now assume 200 is good enough, obviously temporary
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            String message = String.format("Could not initiate call: %s", statusLine.toString());
            logger.info(message);
            motechStatusMessage.alert(message);
            throw new CallInitiationException(message);
        }

        //todo: add extra parameters to CDR?
        String from = config.outgoingCallUriParams.containsKey("from") ? config.outgoingCallUriParams.get("from") : params.containsKey("from") ? params.get("from") : "";
        String to = params.containsKey("to") ? params.get("to") : "";
        callDetailRecordService.logFromMotech(config.name, from, to, CallDirection.OUTBOUND, CallStatus.MOTECH_INITIATED,
                motechCallId);
    }

    private HttpGet generateGetRequest(Config config, Map<String, String> params) {
        logger.info("generateGetRequest(config = {}, params = {})", config, params);

        String uri = ConfigHelper.outgoingCallUri(config);
        BasicHttpParams httpParams = new BasicHttpParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String placeholder = String.format("[%s]", entry.getKey());
            if (uri.contains(placeholder)) {
                uri = uri.replace(placeholder, entry.getValue());
            } else {
                httpParams.setParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpGet get = new HttpGet(uri);
        get.setParams(httpParams);

        logger.info("Generated {}", get);

        return get;
    }

}
