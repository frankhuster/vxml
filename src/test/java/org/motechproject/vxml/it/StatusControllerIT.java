package org.motechproject.vxml.it;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.repository.ConfigDataService;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jdo.JDOException;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Verify StatusController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class StatusControllerIT extends BasePaxIT {

    public static final int NUM_TRIES = 10;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;
    @Inject
    private ConfigDataService configDataService;

    @Before
    public void setup() {
        logger.info("setup");
        try { callDetailRecordDataService.deleteAll(); } catch (JDOException e) { }
    }

    private boolean execHttpRequest(HttpUriRequest request, int expectedStatus) throws InterruptedException,
            IOException {
        int tries = 0;
        do {
            tries++;
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (expectedStatus == response.getStatusLine().getStatusCode()) {
                logger.debug(String.format("Successfully received HTTP %d in %d %s", expectedStatus, tries,
                        tries == 1 ? "try" : "tries"));
                return true;
            }
            logger.debug("Was expecting HTTP {} but received {}, trying again in 5s", expectedStatus,
                    response.getStatusLine().getStatusCode());
            Thread.sleep(5000);
        } while (tries < NUM_TRIES);

        logger.debug("Giving up trying to receive HTTP {} after {} tries", expectedStatus, NUM_TRIES);
        return false;
    }

    @Test
    public void shouldNotLogWhenPassedInvalidConfig() throws Exception {
        logger.info("shouldNotLogWhenPassedIvalidConfig");

        //Create & send a CDR status callback
        String motechCallId = UUID.randomUUID().toString();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort())
                .setPath("/vxml/status/fubar");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        assertTrue(execHttpRequest(httpGet, HttpStatus.SC_INTERNAL_SERVER_ERROR));

        //Verify we did not log this CDR because service contains an invalid config
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(0, callDetailRecords.size());
    }

    @Test
    public void verifyControllerFunctional() throws Exception {
        logger.info("verifyControllerFunctional");

        //Create a config
        List<String> ignoredStatusFields = new ArrayList<>(Arrays.asList("ignoreme", "ignoreme2"));
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("FROM", "from");

        Config config = new Config("foo", ignoredStatusFields, statusMap, null, null);
        configDataService.create(config);

        //Create & send a CDR status callback
        String motechCallId = UUID.randomUUID().toString();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort()).setPath("/vxml/status/foo")
                .addParameter("FROM", "+12065551212")
                .addParameter("to", "+12066661212")
                .addParameter("callStatus", "ANSWERED")
                .addParameter("motechCallId", motechCallId)
                .addParameter("ignoreme", "xxx")
                .addParameter("ignoreme2", "xxx")
                .addParameter("foo", "bar");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        assertTrue(execHttpRequest(httpGet, HttpStatus.SC_OK));

        //Verify we logged this CDR - by querying on its motechId - which is a GUID
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.findByMotechCallId(motechCallId);
        assertEquals(1, callDetailRecords.size());
        CallDetailRecord callDetailRecord = callDetailRecords.get(0);
        assertEquals("+12065551212", callDetailRecord.getFrom());
        assertEquals("+12066661212", callDetailRecord.getTo());
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme"));
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme2"));
        assertEquals(CallStatus.ANSWERED, callDetailRecord.getCallStatus());
        assertEquals(1, callDetailRecord.getProviderExtraData().keySet().size());
        assertEquals(callDetailRecord.getProviderExtraData().get("foo"), "bar");
    }
}
