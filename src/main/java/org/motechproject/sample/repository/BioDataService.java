package org.motechproject.sample.repository;

import org.motechproject.sample.domain.Bio;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface BioDataService extends MotechDataService<Bio> {
    @Lookup
    Bio findByTitle(@LookupField(name = "text") String text);
}
