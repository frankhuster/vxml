package org.motechproject.vxml.service.impl;

import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.service.CallDetailRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * TODO
 */
public class OutboundCallServiceImpl {

    @Autowired
    private CallDetailRecordService callDetailRecordService;

    void initiateCall(String config, String from, String to) {
        String motechCallId = UUID.randomUUID().toString();
        callDetailRecordService.logFromMotech(config, from, to, CallStatus.INITIATED, motechCallId);
        //todo

    }
}
