package org.motechproject.vxml.service;

import java.util.Map;

/**
 * Service Interface used to initiate an outgoing (Mobile Terminated) call
 */
public interface OutboundCallService {
    /**
     * Constructs & executes an HTTP GET request, replacing [xxx] placeholders in the outgoingCallUrl string with their
     * provided values and adding the unmatched params to the request.
     *
     * todo: provide an example
     *
     * @param configName
     * @param params
     */
    void initiateCall(String configName, Map<String, String> params);
}
