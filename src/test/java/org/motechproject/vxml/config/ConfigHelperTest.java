package org.motechproject.vxml.config;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.CallStatus;
import org.motechproject.vxml.domain.Config;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * todo
 */
public class ConfigHelperTest {

    private Config config;

    @Before
    public void setup() {
        Map<String, CallStatus> statusMap = new HashMap<>();
        statusMap.put("answered", CallStatus.ANSWERED);

        Map<String, String> callDetailMap = new HashMap<>();
        callDetailMap.put("recipient", "to");

        config = new Config("Config", statusMap, callDetailMap);
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
}
