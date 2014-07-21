package org.motechproject.vxml.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.vxml.domain.CallRecord;
import org.motechproject.vxml.repository.CallRecordDataService;
import org.motechproject.vxml.service.CallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link org.motechproject.vxml.service.CallRecordService} interface. Uses
 * {@link org.motechproject.vxml.repository.CallRecordDataService} in order to retrieve and persist authors.
 */
@Service("callRecordService")
public class CallRecordServiceImpl implements CallRecordService {

    @Autowired
    private CallRecordDataService callRecordDataService;

    @Override
    public void logCallStatusFromProvider(String from, String to, String status, String motechId, String providerId,
                                          String providerData) {
        List<CallRecord> callRecords = null;

        if (StringUtils.isNotBlank(motechId)) {
            callRecords = callRecordDataService.findByMotechId(motechId);
            providerId = callRecords.get(0).getProviderId();
        }
        if (StringUtils.isNotBlank(providerId) && callRecords != null &&  callRecords.size() == 0) {
            callRecords = callRecordDataService.findByProviderId(providerId);
            motechId = callRecords.get(0).getMotechId();
        }
        callRecordDataService.create(new CallRecord(DateTime.now(), from, to, status, motechId, providerId,
                providerData));
    }

    @Override
    public void logCallStatusFromMotech(String from, String to, String status, String motechId) {
        callRecordDataService.create(new CallRecord(DateTime.now(), from, to, status, motechId, null, null));
    }
}
