package org.motechproject.vxml.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.repository.ConfigDataService;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jdo.JDOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Verify ConfigDataService present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ConfigDataServiceIT extends BasePaxIT {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private ConfigDataService configDataService;

    @Before
    public void cleanupDatabase() {
        try { configDataService.deleteAll(); } catch (JDOException e) { }
    }

    @Test
    public void verifyServiceFunctional() throws Exception {
        logger.info("verifyServiceFunctional");

        Map<String, CallStatus> statusMap = new HashMap<>();
        statusMap.put("answered", CallStatus.ANSWERED);

        Map<String, String> callDetailMap = new HashMap<>();
        callDetailMap.put("recipient", "to");

        Config myConfig = new Config("MyConfig", statusMap, callDetailMap);
        configDataService.create(myConfig);

        Config config = configDataService.findByName("MyConfig");
        assertEquals(config, myConfig);
    }
}
