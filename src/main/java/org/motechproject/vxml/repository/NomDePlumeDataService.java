package org.motechproject.vxml.repository;

import org.motechproject.vxml.domain.NomDePlume;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface NomDePlumeDataService extends MotechDataService<NomDePlume> {
    @Lookup
    NomDePlume findByTitle(@LookupField(name = "nom") String nom);
}
