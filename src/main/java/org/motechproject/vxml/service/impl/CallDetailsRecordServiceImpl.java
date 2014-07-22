package org.motechproject.vxml.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.vxml.domain.CallDetailsRecord;
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
    public void logCallStatusFromProvider(String from, String to, String status, String motechId, String providerId,
                                          String providerData) {
        List<CallDetailsRecord> callDetailsRecords = null;

        if (StringUtils.isNotBlank(motechId)) {
            callDetailsRecords = callDetailsRecordDataService.findByMotechId(motechId);
            providerId = callDetailsRecords.get(0).getProviderId();
        }
        if (StringUtils.isNotBlank(providerId) && callDetailsRecords != null &&  callDetailsRecords.size() == 0) {
            callDetailsRecords = callDetailsRecordDataService.findByProviderId(providerId);
            motechId = callDetailsRecords.get(0).getMotechId();
        }
        callDetailsRecordDataService.create(new CallDetailsRecord(DateTime.now(), from, to, status, motechId, providerId,
                providerData));
    }

    @Override
    public void logCallStatusFromMotech(String from, String to, String status, String motechId) {
        callDetailsRecordDataService.create(new CallDetailsRecord(DateTime.now(), from, to, status, motechId, null, null));
    }
}
