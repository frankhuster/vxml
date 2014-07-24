package org.motechproject.vxml.service.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jdo.JDOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Verify CallDetailRecordService present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CallDetailRecordServiceIT extends BasePaxIT {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private org.motechproject.vxml.service.CallDetailRecordService callDetailRecordService;
    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;

    @Before
    public void cleanupDatabase() {
        logger.info("cleanupDatabase");
        try { callDetailRecordDataService.deleteAll(); } catch (JDOException e) { }
    }

    private Map<String, String> quickieMap(String k1, String v1, String k2, String v2) {
        Map<String, String> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Test
    public void verifyServiceFunctional() throws Exception {

        logger.info("verifyServiceFunctional");

        //
        // logFromMotech
        //
        String motechCallId = UUID.randomUUID().toString();
        callDetailRecordService.logFromMotech("config-a", "from", "to", CallStatus.IN_PROGRESS, motechCallId);
        logger.info("created call record with motechCallId {}", motechCallId);
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.findByMotechCallId(motechCallId);
        assertEquals(1, callDetailRecords.size());
        assertEquals(motechCallId, callDetailRecords.get(0).motechCallId);
        logger.info("found call record with motechCallId {}", callDetailRecords.get(0).motechCallId);

        //
        // logFromProvider
        //
        motechCallId = UUID.randomUUID().toString();
        callDetailRecordService.logFromMotech("config-b", "from", "to", CallStatus.IN_PROGRESS, motechCallId);
        logger.info("created call record with motechCallId {}", motechCallId);

        String providerCallId = "SOMEPROVIDER-" + UUID.randomUUID().toString();
        callDetailRecordService.logFromProvider("config-b", "from", "to", CallStatus.BUSY, "Busy", motechCallId,
                providerCallId, quickieMap("foo", "bar", "baz", "bat"));
        logger.info("created call record with motechCallId {} & providerCallId {}", motechCallId, providerCallId);

        callDetailRecordService.logFromProvider("config-b", "from", "to", CallStatus.ANSWERED, "Answered", null,
                providerCallId, quickieMap("goo", "zar", "zaz", "zat"));
        logger.info("created call record with providerCallId {}", providerCallId);

        callDetailRecords = callDetailRecordDataService.findByProviderCallId(providerCallId);
        assertEquals(2, callDetailRecords.size());
        assertEquals(motechCallId, callDetailRecords.get(0).motechCallId);
        assertEquals(motechCallId, callDetailRecords.get(1).motechCallId);
        assertEquals(providerCallId, callDetailRecords.get(0).providerCallId);
        assertEquals(providerCallId, callDetailRecords.get(1).providerCallId);
        logger.info("found call details record(s) with providerCallId {}: {}", providerCallId,
                callDetailRecords.toString());
    }
}
