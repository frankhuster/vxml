package org.motechproject.vxml;

import org.junit.Test;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.CallDirection;
import org.motechproject.vxml.domain.CallStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * todo
 */
public class EventParamsTest {
    @Test
    public void verifyFunctional() {
        //Construct a CDR
        Map<String, String> extraData = new HashMap<>();
        extraData.put("foo", "bar");
        CallDetailRecord callDetailRecord = new CallDetailRecord("sometime", "domain", "from", "to",
                CallDirection.INBOUND, CallStatus.ANSWERED, "motechCallId", "providerCallId", extraData);

        //Pass service to eventParamsFromCallDetailRecord
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);

        //Verify all data was passed properly
        assertEquals("sometime", eventParams.get(EventParams.TIMESTAMP));
        assertEquals("domain", eventParams.get(EventParams.CONFIG));
        assertEquals("from", eventParams.get(EventParams.FROM));
        assertEquals("to", eventParams.get(EventParams.TO));
        assertEquals(CallDirection.INBOUND, eventParams.get(EventParams.CALL_DIRECTION));
        assertEquals(CallStatus.ANSWERED, eventParams.get(EventParams.CALL_STATUS));
        assertEquals("motechCallId", eventParams.get(EventParams.MOTECH_CALL_ID));
        assertEquals("providerCallId", eventParams.get(EventParams.PROVIDER_CALL_ID));
        assertEquals(extraData, eventParams.get(EventParams.PROVIDER_EXTRA_DATA));
    }
}
