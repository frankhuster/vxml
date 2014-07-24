package org.motechproject.vxml.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.vxml.domain.Config;

/**
 * todo
 */
public interface ConfigDataService extends MotechDataService<Config> {
    @Lookup
    Config findByName(@LookupField(name = "name") String name);
}
