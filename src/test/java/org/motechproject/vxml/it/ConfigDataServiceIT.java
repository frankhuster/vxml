package org.motechproject.vxml.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.domain.HttpMethod;
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
    public void verifyServiceFunctional() {
        logger.info("verifyServiceFunctional");

        Config myConfig = new Config("MyConfig", null, null, HttpMethod.GET, "http://foo.com/bar");
        configDataService.create(myConfig);

        Config config = Config.getConfig(configDataService, null, "MyConfig");
        //Config config = configDataService.findByName("MyConfig");
        assertEquals(config, myConfig);
    }

    @Test(expected = JDOException.class)
    public void shouldNotCreateDuplicateConfigs() {
        logger.info("shouldNotCreateDuplicateConfigs");

        Config myConfig = new Config("MyConfig", null, null, HttpMethod.GET, "http://foo.com/bar");
        configDataService.create(myConfig);

        Config myIdenticalConfig = new Config("MyConfig", null, null, HttpMethod.GET, "http://foo.com/bar");
        configDataService.create(myIdenticalConfig);
    }

    @Test
    public void shouldStoreLongOutgoingCallUriTemplates() {
        logger.info("shouldStoreLongOutgoingCallUriTemplates");

        Map<String, String> outgoingCallUriParams = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i<100 ; i++) {
            sb.append("0123456789");
        }
        String longURI = sb.toString();

        Config myConfig = new Config("MyConfig", null, null, HttpMethod.GET, longURI);
        configDataService.create(myConfig);

        Config configFromDatabase = Config.getConfig(configDataService, null, "MyConfig");
        assertEquals(configFromDatabase.getOutgoingCallUriTemplate(), longURI);
    }
}
