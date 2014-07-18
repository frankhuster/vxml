package org.motechproject.vxml.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.vxml.domain.Spy;

public interface SpyDataService extends MotechDataService<Spy> {
    @Lookup
    Spy findByName(@LookupField(name = "name") String name);
}
