package org.motechproject.vxml.service;

/**
 * Service interface for call records
 */
public interface CallDetailsRecordService {

    void logCallStatusFromProvider(String from, String to, String status, String motechId, String providerId,
                                   String providerData);
    void logCallStatusFromMotech(String from, String to, String status, String motechId);
}
