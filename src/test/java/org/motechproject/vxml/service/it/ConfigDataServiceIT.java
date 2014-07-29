package org.motechproject.vxml.service.it;

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
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verify ConfigDataService present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ConfigDataServiceIT extends BasePaxIT {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean areMapsEqual(Map<String, CallStatus> m1, Map<String, CallStatus> m2) {
        if (m1 == m2) {
            return true;
        }

        if (m1.size() != m2.size()) {
            return false;
        }

        try {
            Iterator<Map.Entry<String,CallStatus>> i = m1.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String,CallStatus> e = i.next();
                String key = e.getKey();
                CallStatus value = CallStatus.FAILED;
                // this throws a ClassCastException - grrr...
                value = e.getValue();
                if (value == null) {
                    if (!(m2.get(key)==null && m2.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m2.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

    @Inject
    private ConfigDataService configDataService;

    @Before
    public void cleanupDatabase() {
        try { configDataService.deleteAll(); } catch (JDOException e) { }
    }

    @Test
    public void verifyServiceFunctional() throws Exception {
        logger.info("verifyServiceFunctional");

        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Map<String, CallStatus> statusMap = new HashMap<>();
            statusMap.put("answered", CallStatus.ANSWERED);

            Map<String, String> callDetailMap = new HashMap<>();
            callDetailMap.put("recipient", "to");

            configDataService.create(new Config("MyConfig", statusMap, callDetailMap));

            Config config = configDataService.findByName("MyConfig");
            assertEquals(config.name, "MyConfig");
            assertEquals(config.callDetailMap, callDetailMap);
            // Why oh why does this not work?!? See inside areMapsEqual() above for more details
            assertTrue(areMapsEqual(config.statusMap, statusMap));
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }
}
