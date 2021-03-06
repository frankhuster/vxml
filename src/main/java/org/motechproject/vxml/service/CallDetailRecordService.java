package org.motechproject.vxml.service;

import org.motechproject.vxml.domain.CallDirection;
import org.motechproject.vxml.domain.CallStatus;

import java.util.Map;

/**
 * Service interface for call detail records
 */
public interface CallDetailRecordService {

    public void logFromProvider(String config, String from, String to, CallDirection callDirection,
                                CallStatus callStatus, String motechCallId, String providerCallId,
                                Map<String, String> providerExtraData);
    void logFromMotech(String config, String from, String to, CallDirection callDirection, CallStatus callStatus,
                       String motechId);
}
