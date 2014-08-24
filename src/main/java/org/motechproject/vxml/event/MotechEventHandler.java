package org.motechproject.vxml.event;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.vxml.EventSubjects;
import org.motechproject.vxml.service.OutboundCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Listens to the vxml_initiate_call MotechEvent and calls initiateCall
 */
@Service
public class MotechEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotechEventHandler.class);
    private OutboundCallService outboundCallService;

    @Autowired
    public MotechEventHandler(OutboundCallService outboundCallService) {
        this.outboundCallService = outboundCallService;
    }

    @MotechListener(subjects = { EventSubjects.INITIATE_CALL })
    public void handleExternal(MotechEvent event) {
        LOGGER.info("Handling Motech event {}: {}", event.getSubject(), event.getParameters().toString());
        String config = event.getParameters().get("config").toString();
        Map<String, String> params = (Map<String, String>) event.getParameters().get("params");
        outboundCallService.initiateCall(config, params);
    }

}
