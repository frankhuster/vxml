package org.motechproject.vxml;

import org.motechproject.vxml.domain.CallDetailRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * Possible Event payloads (ie: params)
 */
public final class EventParams {

    private EventParams() { }

    /**
     * date & time when this event happened
     */
    public static final String TIMESTAMP = "timestamp";
    /**
     * Config that was used for this message
     */
    public static final String CONFIG = "domain";
    /**
     * Phone number that the call was issued from
     */
    public static final String FROM = "from";
    /**
     * Call recipient (phone number)
     */
    public static final String TO = "to";
    /**
     * Call direction  -  INBOUND (MO) or OUTBOUND (MT)
     */
    public static final String CALL_DIRECTION = "callDirection";
    /**
     * Call status
     */
    public static final String CALL_STATUS = "callStatus";
    /**
     * Provider's native call status
     */
    public static final String PROVIDER_STATUS = "providerStatus";
    /**
     * MOTECH call id - a MOTECH-generated GUID
     */
    public static final String MOTECH_CALL_ID = "motechCallId";
    /**
     * Provider call id - provider-generated id
     */
    public static final String PROVIDER_CALL_ID = "providerCallId";
    /**
     * Provider extra data
     */
    public static final String PROVIDER_EXTRA_DATA = "providerExtraData";

    /**
     * Create a MOTECH event parameters map payload from a given {@link org.motechproject.vxml.domain.CallDetailRecord}.
     *
     * @param callDetailRecord
     * @return
     */
    public static Map<String, Object> eventParamsFromCallDetailRecord(CallDetailRecord callDetailRecord) {
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParams.CONFIG, callDetailRecord.config);
        eventParams.put(EventParams.FROM, callDetailRecord.from);
        eventParams.put(EventParams.TO, callDetailRecord.to);
        eventParams.put(EventParams.CALL_DIRECTION, callDetailRecord.callDirection);
        eventParams.put(EventParams.CALL_STATUS, callDetailRecord.callStatus);
        eventParams.put(EventParams.PROVIDER_STATUS, callDetailRecord.providerStatus);
        eventParams.put(EventParams.MOTECH_CALL_ID, callDetailRecord.motechCallId);
        eventParams.put(EventParams.PROVIDER_CALL_ID, callDetailRecord.providerCallId);
        eventParams.put(EventParams.PROVIDER_EXTRA_DATA, callDetailRecord.providerExtraData);
        return eventParams;
    }
}
