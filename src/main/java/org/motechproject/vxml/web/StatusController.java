package org.motechproject.vxml.web;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.vxml.EventParams;
import org.motechproject.vxml.EventSubjects;
import org.motechproject.vxml.TimestampHelper;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.repository.ConfigDataService;
import org.motechproject.vxml.service.MotechStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Responds to HTTP queries to {motech-server}/module/vxml/status/{configName} by creating a CallDetailRecord entry in
 * the database and posting a corresponding Motech event on the queue.
 */
@Controller
@RequestMapping(value = "/status")
public class StatusController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CallDetailRecordDataService callDetailRecordDataService;
    private ConfigDataService configDataService;
    private MotechStatusMessage motechStatusMessage;
    private EventRelay eventRelay;

    @Autowired
    public StatusController(CallDetailRecordDataService callDetailRecordDataService, EventRelay eventRelay,
                            ConfigDataService configDataService, MotechStatusMessage motechStatusMessage) {
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.eventRelay = eventRelay;
        this.configDataService = configDataService;
        this.motechStatusMessage = motechStatusMessage;
    }

    /**
     * Listens to HTTP calls to http://{server}:{port}/module/vxml/status/{config}?param1=val1&param2=val2&... from IVR
     * providers. Creates a corresponding CDR entity in the database. Sends a MOTECH message with the CDR data in the
     * payload and the call status as the subject.
     *
     * @param configName
     * @param params
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}", produces = "text/xml")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params,
                       @RequestHeader Map<String, String> headers) {
        logger.debug(String.format("handle(configName = %s, params = %s, headers = %s)", configName, params, headers));

        Config config = Config.getConfig(configDataService, motechStatusMessage, configName);

        // Construct a CDR from the URL query parameters passed in the callback
        CallDetailRecord callDetailRecord = new CallDetailRecord();

        callDetailRecord.setConfigName(configName);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (config.shouldIgnoreField(entry.getKey())) {
                logger.debug("Ignoring provider field '{}' with value '{}'", entry.getKey(), entry.getValue());
            } else {
                callDetailRecord.setField(config.mapStatusField(entry.getKey()), entry.getValue());
            }
        }
        
        // Use current time if the provider didn't provide a timestamp
        if (null == callDetailRecord.getTimestamp()) {
            callDetailRecord.setTimestamp(TimestampHelper.currentTime());
        }

        // Generate a MOTECH event
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);
        MotechEvent event = new MotechEvent(EventSubjects.CALL_STATUS, eventParams);
        logger.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);

        // Save the CDR
        logger.debug("Saving CallDetailRecord {}", callDetailRecord);
        callDetailRecordDataService.create(callDetailRecord);
    }
}
