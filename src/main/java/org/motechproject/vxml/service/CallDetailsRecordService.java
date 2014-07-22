package org.motechproject.vxml.service;

import org.motechproject.vxml.domain.CallStatus;

/**
 * Service interface for call records
 */
public interface CallDetailsRecordService {

    void logCallStatusFromProvider(String from, String to, CallStatus callStatus, String providerStatus,
                                   String motechId, String providerId, String providerData);
    void logCallStatusFromMotech(String from, String to, CallStatus callStatus, String motechId);
}
