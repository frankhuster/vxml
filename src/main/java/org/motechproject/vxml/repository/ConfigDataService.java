package org.motechproject.vxml.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.vxml.domain.Config;

import java.util.List;

/**
 * MDS generated ConfigDataService database queries
 */
public interface ConfigDataService extends MotechDataService<Config> {
    @Lookup
    List<Config> findByName(@LookupField(name = "name") String name);
}
