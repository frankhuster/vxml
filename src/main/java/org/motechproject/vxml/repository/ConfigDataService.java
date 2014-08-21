package org.motechproject.vxml.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.vxml.domain.Config;

import java.util.List;

/**
 * todo
 */
public interface ConfigDataService extends MotechDataService<Config> {
    @Lookup
    List<Config> findAllByName(@LookupField(name = "name") String name);

    @Lookup
    Config findOneByName(@LookupField(name = "name") String name);
}
