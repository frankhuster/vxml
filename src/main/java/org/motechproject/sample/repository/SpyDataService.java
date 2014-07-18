package org.motechproject.sample.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.sample.domain.Spy;

public interface SpyDataService extends MotechDataService<Spy> {
    @Lookup
    Spy findByName(@LookupField(name = "name") String name);
}
