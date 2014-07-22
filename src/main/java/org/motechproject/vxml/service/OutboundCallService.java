package org.motechproject.vxml.service;

/**
 * Service Interface used to initiate an outgoing (Mobile Terminated) call
 */
public interface OutboundCallService {
    void initiateCall(String config, String from, String to);
}
