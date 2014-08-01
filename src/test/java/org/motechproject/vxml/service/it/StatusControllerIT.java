package org.motechproject.vxml.service.it;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.service.CallDetailRecordService;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jdo.JDOException;

import static org.junit.Assert.assertEquals;

/**
 * Verify StatusController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class StatusControllerIT extends BasePaxIT {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private CallDetailRecordService callDetailRecordService;
    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;

    @Before
    public void setup() {
        logger.info("setup");
        try { callDetailRecordDataService.deleteAll(); } catch (JDOException e) { }
    }

    @Test
    public void verifyControllerFunctional() throws Exception {
        logger.info("verifyControllerFunctional");

        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/vxml/status/foo", TestContext.getJettyPort()));
        HttpResponse response = getHttpClient().execute(httpGet);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }
}
