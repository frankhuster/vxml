package org.motechproject.vxml.service.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.service.OutboundCallService;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jdo.JDOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Verify that the OutboundCallService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class OutboundCallServiceIT extends BasePaxIT {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private OutboundCallService outboundCallService;
    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;

    @Before
    public void cleanupDatabase() {
        logger.info("cleanupDatabase");
        try { callDetailRecordDataService.deleteAll(); } catch (JDOException e) { }
    }

    @Test
    public void verifyServiceFunctional() throws Exception {

        logger.info("verifyServiceFunctional");

        outboundCallService.initiateCall("config123", "from", "to");

        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(1, callDetailRecords.size());
        assertEquals("config123", callDetailRecords.get(0).config);
    }
}
