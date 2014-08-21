package org.motechproject.vxml.web;

import org.apache.commons.lang.StringUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.vxml.EventParams;
import org.motechproject.vxml.EventSubjects;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.repository.ConfigDataService;
import org.motechproject.vxml.service.CallDetailRecordService;
import org.motechproject.vxml.service.MotechStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * todo
 */
@Controller
@RequestMapping(value = "/status")
public class StatusController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CallDetailRecordService callDetailRecordService;
    private CallDetailRecordDataService callDetailRecordDataService;
    private ConfigDataService configDataService;
    private MotechStatusMessage motechStatusMessage;
    private EventRelay eventRelay;

    @Autowired
    public StatusController(CallDetailRecordService callDetailRecordService,
                            CallDetailRecordDataService callDetailRecordDataService, EventRelay eventRelay,
                            ConfigDataService configDataService, MotechStatusMessage motechStatusMessage) {
        this.callDetailRecordService = callDetailRecordService;
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
    public String handle(@PathVariable String configName, @RequestParam Map<String, String> params,
                       @RequestHeader Map<String, String> headers) {
        logger.debug(String.format("handle(configName = %s, params = %s, headers = %s)", configName, params, headers));

        Config config = Config.getConfig(configDataService, motechStatusMessage, configName);
        Set<String> ignoreFields;

        if (StringUtils.isBlank(config.getIgnoredStatusFields())) {
            ignoreFields = new HashSet<>();
        }
        else {
            ignoreFields = new HashSet<>(Arrays.asList(config.getIgnoredStatusFields().split(" *, *")));
        }

        // Construct a CDR from the URL query parameters passed in the callback
        CallDetailRecord callDetailRecord = new CallDetailRecord();

        callDetailRecord.setConfigName(configName);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (ignoreFields.contains(entry.getKey())) {
                logger.debug("Ignoring provider field {}: {}", entry.getKey(), entry.getValue());
            }
            else {
                callDetailRecord.setField(entry.getKey(), entry.getValue());
            }
        }
        
        // Use current time if the provider didn't provide a timestamp
        if (null == callDetailRecord.getTimestamp()) {
            callDetailRecord.setTimestamp(callDetailRecordService.currentTime());
        }

        // Generate a MOTECH event
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);
        MotechEvent event = new MotechEvent(EventSubjects.CALL_STATUS, eventParams);
        logger.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);

        // Save the CDR
        logger.debug("Saving CallDetailRecord {}", callDetailRecord);
        callDetailRecordDataService.create(callDetailRecord);

        return "<?xml version=\"1.0\"?>\n<vxml version=\"2.1\"><form></form></vxml>";
    }
}
