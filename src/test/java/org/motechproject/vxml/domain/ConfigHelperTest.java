package org.motechproject.vxml.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * todo
 */
public class ConfigHelperTest {

    private Config config;

    private static final String TOKEN_KEY = "tok";
    private static final String TOKEN_VALUE = "68496w84ef682f6des8";
    private static final String OUTGOING_URI_TEMPLATE_FORMAT = "http://foo.com/bar?token=%s";
    private static final String OUTGOING_URI_TEMPLATE = String.format(OUTGOING_URI_TEMPLATE_FORMAT,
            String.format("[%s]", TOKEN_KEY));

    @Before
    public void setup() {
        Map<String, CallStatus> statusMap = new HashMap<>();
        statusMap.put("answered", CallStatus.ANSWERED);

        Map<String, String> callDetailMap = new HashMap<>();
        callDetailMap.put("recipient", "to");

        Map<String, String> outgoingCallUriParams = new HashMap<>();
        outgoingCallUriParams.put(TOKEN_KEY, TOKEN_VALUE);

        config = new Config("Config", statusMap, callDetailMap, "ignore1, ignore2", "+12065551212",
                OUTGOING_URI_TEMPLATE, outgoingCallUriParams);
    }

    @Test
    public void shouldSetCallDetailStatus() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        ConfigHelper.setCallDetail(config, "callStatus", "answered", callDetailRecord);
        assertEquals(CallStatus.ANSWERED, callDetailRecord.callStatus);
    }

    @Test
    public void shouldSetUnknownCallDetailStatus() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        ConfigHelper.setCallDetail(config, "callStatus", "foo", callDetailRecord);
        assertEquals(CallStatus.UNKNOWN, callDetailRecord.callStatus);
        assertEquals(1, callDetailRecord.providerExtraData.size());
        assertTrue(callDetailRecord.providerExtraData.keySet().contains("callStatus"));
        assertTrue(callDetailRecord.providerExtraData.values().contains("foo"));
    }

    @Test
    public void shouldTruncateLongValue() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i<50 ; i++) {
            sb.append("0123456789");
        }
        String value = sb.toString();
        ConfigHelper.setCallDetail(config, "from", value, callDetailRecord);
        assertEquals(callDetailRecord.from.length(), 255);
    }

    @Test
    public void shouldSetCallDetailStatusAsUnknown() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        ConfigHelper.setCallDetail(config, "callStatus", "fubar", callDetailRecord);
        assertEquals(CallStatus.UNKNOWN, callDetailRecord.callStatus);
        assertTrue(callDetailRecord.providerExtraData.containsKey("callStatus"));
        assertEquals("fubar", callDetailRecord.providerExtraData.get("callStatus"));
    }

    @Test
    public void shouldSetCallDetail() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        ConfigHelper.setCallDetail(config, "recipient", "+12065551212", callDetailRecord);
        assertEquals("+12065551212", callDetailRecord.to);
    }

    @Test
    public void shouldSetCallDetailAsProviderExtraData() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        ConfigHelper.setCallDetail(config, "provider-specific-stuff", "specific-value", callDetailRecord);
        assertTrue(callDetailRecord.providerExtraData.containsKey("provider-specific-stuff"));
        assertEquals("specific-value", callDetailRecord.providerExtraData.get("provider-specific-stuff"));
    }

    //todo: test 'ignored' fields

    @Test
    public void shouldSubstituteOutgoingCallUriParameters() {
        String expectedUriTemplate = String.format(OUTGOING_URI_TEMPLATE_FORMAT, TOKEN_VALUE);
        assertEquals(expectedUriTemplate, ConfigHelper.outgoingCallUri(config));
    }
}
