package org.motechproject.vxml.service.impl;

import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.service.CallDetailRecordService;
import org.motechproject.vxml.service.OutboundCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * TODO
 */
@Service("outboundCallService")
public class OutboundCallServiceImpl implements OutboundCallService{

    @Autowired
    private CallDetailRecordService callDetailRecordService;

    /**
     * Initiates an outbound call
     * 
     * @param config
     * @param from
     * @param to
     */
    @Override
    public void initiateCall(String config, String from, String to) {
        String motechCallId = UUID.randomUUID().toString();
        callDetailRecordService.logFromMotech(config, from, to, CallStatus.INITIATED, motechCallId);
        //todo
    }
}
