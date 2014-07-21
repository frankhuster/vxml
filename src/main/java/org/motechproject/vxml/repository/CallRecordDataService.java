package org.motechproject.vxml.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.vxml.domain.CallRecord;

import java.util.List;

public interface CallRecordDataService extends MotechDataService<CallRecord> {
    @Lookup
    List<CallRecord> findByMotechId(@LookupField(name = "motechId") String motechId);

    @Lookup
    List<CallRecord> findByProviderId(@LookupField(name = "providerId") String providerId);
}
