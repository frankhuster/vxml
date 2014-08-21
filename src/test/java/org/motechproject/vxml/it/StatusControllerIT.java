package org.motechproject.vxml.it;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
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
import org.motechproject.vxml.domain.HttpMethod;
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
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verify StatusController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class StatusControllerIT extends BasePaxIT {

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

    @Test
    public void shouldNotLogWhenPassedIvalidConfig() throws Exception {
        logger.info("shouldNotLogWhenPassedIvalidConfig");

        //Create & send a CDR status callback
        String motechCallId = UUID.randomUUID().toString();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort())
                .setPath("/vxml/status/fubar");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = new DefaultHttpClient().execute(httpGet);
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());

        //Verify we did not log this CDR because service contains an invalid config
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(0, callDetailRecords.size());
    }

    @Test
    public void verifyControllerFunctional() throws Exception {
        logger.info("verifyControllerFunctional");

        //Create a config
        Map<String, String> outgoingCallUriParams = new HashMap<>();
        Config config = new Config("foo", "", "http://foo.com/bar", HttpMethod.GET, outgoingCallUriParams);
        configDataService.create(config);

        //Create & send a CDR status callback
        String motechCallId = UUID.randomUUID().toString();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort()).setPath("/vxml/status/foo")
                .addParameter("from", "+12065551212")
                .addParameter("to", "+12066661212")
                .addParameter("callStatus", "ANSWERED")
                .addParameter("motechCallId", motechCallId)
                .addParameter("providerCallId", "123456")
                .addParameter("foo", "bar");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = new DefaultHttpClient().execute(httpGet);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

        //Verify we logged this CDR - by querying on its motechId - which is a GUID
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.findByMotechCallId(motechCallId);
        assertEquals(1, callDetailRecords.size());
        CallDetailRecord callDetailRecord = callDetailRecords.get(0);
        assertEquals(CallStatus.ANSWERED, callDetailRecord.callStatus);
        assertEquals(1, callDetailRecord.providerExtraData.keySet().size());
        assertTrue(callDetailRecord.providerExtraData.keySet().contains("foo"));
        assertTrue(callDetailRecord.providerExtraData.values().contains("bar"));
    }
}
