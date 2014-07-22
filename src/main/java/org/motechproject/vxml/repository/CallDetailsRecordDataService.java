package org.motechproject.vxml.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.vxml.domain.CallDetailsRecord;

import java.util.List;

public interface CallDetailsRecordDataService extends MotechDataService<CallDetailsRecord> {
    @Lookup
    List<CallDetailsRecord> findByMotechId(@LookupField(name = "motechId") String motechId);

    @Lookup
    List<CallDetailsRecord> findByProviderId(@LookupField(name = "providerId") String providerId);
}
