package org.motechproject.vxml.web;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.vxml.EventParams;
import org.motechproject.vxml.EventSubjects;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.domain.ConfigHelper;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.repository.ConfigDataService;
import org.motechproject.vxml.service.MotechStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * todo
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

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params) {
        logger.debug("configName = {}, params = {}", configName, params);

        Config config = configDataService.findByName(configName);
        // Status callbacks referring to invalid configs will receive HTTP-500
        if (null == config){
            String msg = String.format("No matching domain in the database for: %s", configName);
            logger.error(msg);
            motechStatusMessage.alert(msg);
            throw new IllegalArgumentException(msg);
        }

        // Construct a CDR from the URL query parameters passed in the callback
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.config = config.name;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            ConfigHelper.setCallDetail(config, entry.getKey(), entry.getValue(), callDetailRecord);
        }

        // Generate a MOTECH event
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);
        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.subjectFromCallStatus(callDetailRecord.callStatus),
                eventParams));

        // Log the status
        logger.debug("Saving CallDetailRecord {}", callDetailRecord);
        callDetailRecordDataService.create(callDetailRecord);
    }
}
