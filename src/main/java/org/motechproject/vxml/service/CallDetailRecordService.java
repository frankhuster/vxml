package org.motechproject.vxml.service;

import org.motechproject.vxml.domain.CallStatus;

import java.util.Map;

/**
 * Service interface for call records
 */
public interface CallDetailRecordService {

    void logFromProvider(String config, String from, String to, CallStatus callStatus, String providerStatus,
                         String motechCallId, String providerCallId, Map<String, String> providerData);
    void logFromMotech(String config, String from, String to, CallStatus callStatus, String motechCallId);
}
