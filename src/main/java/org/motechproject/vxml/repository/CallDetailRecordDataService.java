package org.motechproject.vxml.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.vxml.domain.CallDetailRecord;

import java.util.List;

/**
 * todo
 */
public interface CallDetailRecordDataService extends MotechDataService<CallDetailRecord> {
    @Lookup
    List<CallDetailRecord> findByMotechCallId(@LookupField(name = "motechCallId") String motechCallId);

    @Lookup
    List<CallDetailRecord> findByProviderCallId(@LookupField(name = "providerCallId") String providerCallId);
}
