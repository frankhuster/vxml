package org.motechproject.vxml.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.vxml.domain.CallDetailsRecord;
import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.repository.CallDetailsRecordDataService;
import org.motechproject.vxml.service.CallDetailsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link org.motechproject.vxml.service.CallDetailsRecordService} interface. Uses
 * {@link org.motechproject.vxml.repository.CallDetailsRecordDataService} in order to retrieve and persist authors.
 */
@Service("callRecordService")
public class CallDetailsRecordServiceImpl implements CallDetailsRecordService {

    @Autowired
    private CallDetailsRecordDataService callDetailsRecordDataService;

    @Override
    public void logCallStatusFromProvider(String from, String to, CallStatus callStatus, String providerStatus,
                                          String motechId, String providerId, String providerData) {
        List<CallDetailsRecord> callDetailsRecords = callDetailsRecords = callDetailsRecordDataService.findByProviderId(
                providerId);
        if (callDetailsRecords.size() > 0 && StringUtils.isNotBlank(callDetailsRecords.get(0).getMotechId())) {
            motechId = callDetailsRecords.get(0).getMotechId();
        }
        callDetailsRecordDataService.create(new CallDetailsRecord(DateTime.now(), from, to, callStatus, providerStatus,
                motechId, providerId, providerData));
    }

    @Override
    public void logCallStatusFromMotech(String from, String to, CallStatus callStatus, String motechId) {
        callDetailsRecordDataService.create(new CallDetailsRecord(DateTime.now(), from, to, callStatus,
                callStatus.toString(), motechId, null, null));
    }
}
