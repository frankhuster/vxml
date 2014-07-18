package org.motechproject.vxml.repository;

import org.motechproject.vxml.domain.Secret;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface SecretDataService extends MotechDataService<Secret> {
    @Lookup
    Secret findByMessage(@LookupField(name = "message") String message);
}
