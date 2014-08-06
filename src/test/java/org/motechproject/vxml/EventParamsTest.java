package org.motechproject.vxml;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.vxml.domain.CallDetailRecord;
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
        DateTime dtNow = DateTime.now();
        CallDetailRecord callDetailRecord = new CallDetailRecord(dtNow, "domain", "from", "to", CallStatus.ANSWERED,
                "providerStatus", "motechCallId", "providerCallId", extraData);

        //Pass service to eventParamsFromCallDetailRecord
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);

        //Verify all data was passed properly
        assertEquals("domain", eventParams.get(EventParams.CONFIG));
        assertEquals("from", eventParams.get(EventParams.FROM));
        assertEquals("to", eventParams.get(EventParams.TO));
        assertEquals(CallStatus.ANSWERED, eventParams.get(EventParams.CALL_STATUS));
        assertEquals("providerStatus", eventParams.get(EventParams.PROVIDER_STATUS));
        assertEquals("motechCallId", eventParams.get(EventParams.MOTECH_CALL_ID));
        assertEquals("providerCallId", eventParams.get(EventParams.PROVIDER_CALL_ID));
        assertEquals(extraData, eventParams.get(EventParams.PROVIDER_EXTRA_DATA));
    }
}
