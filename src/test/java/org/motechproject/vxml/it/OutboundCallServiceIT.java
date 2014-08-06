package org.motechproject.vxml.it;

import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.testing.osgi.BasePaxIT;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Verify that the OutboundCallService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class OutboundCallServiceIT extends BasePaxIT {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private org.motechproject.vxml.service.OutboundCallService outboundCallService;
    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;
    @Inject
    private ConfigDataService configDataService;
    @Mock
    private HttpClient httpClient;

    @Before
    public void setup() {
        logger.info("setup");
        try { callDetailRecordDataService.deleteAll(); } catch (JDOException e) { }

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyServiceFunctional() throws Exception {
        logger.info("verifyServiceFunctional");

        //Create a config
        Map<String, CallStatus> statusMap = new HashMap<>();
        Map<String, String> callDetailMap = new HashMap<>();
        Config config = new Config("config123", statusMap, callDetailMap, "+12065551212", "http://localhost/fubar");
        configDataService.create(config);

        when(httpClient.execute(Matchers.<HttpUriRequest>any())).thenReturn(new BasicHttpResponse(new BasicStatusLine(
                new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK")));

        Map<String, String> params = new HashMap<>();
        outboundCallService.initiateCall("config123", params);

        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(1, callDetailRecords.size());
        assertEquals("config123", callDetailRecords.get(0).config);
    }
}
