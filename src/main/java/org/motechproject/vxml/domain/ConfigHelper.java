package org.motechproject.vxml.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * todo
 */
public class ConfigHelper {
    private static Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

    /**
     * Maps a given statusString to a CallStatus, using the given domain's statusMap to first try to map the
     * string to a status. If no match has been found in the domain's statusMap tries to match the string to the literal
     * value of the CallStatus. If all fails, return CallStatus.UNKNOWN and add a warning in the log.
     *
     * @param config
     * @param statusString
     * @return
     */
    private static CallStatus mapStatus(Config config, String statusString) {
        if (config.statusMap.containsKey(statusString)) {
            return config.statusMap.get(statusString);
        }

        try {
            CallStatus callStatus = CallStatus.valueOf(statusString);
            if (statusString.equals(callStatus.toString())) {
                return callStatus;
            }
        } catch (IllegalArgumentException e) { }

        logger.warn("Unknown status string: {}", statusString);
        return CallStatus.UNKNOWN;
    }

    /**
     * When receiving call detail information from an IVR provider the specific call details must be mapped from
     * what the provider sends back to MOTECH and a CallDetailRecord object. This method will find which field on the
     * given callDetailRecord matches the given key and set service to the given value. If there is no matching
     * CallDetailRecord field, then the key/value pair is added to the providerExtraData map field. Also, if a
     * callStatus value does not map to an existing CallStatus, then the value of the callStatus field is set to
     * CallStatus.UNKNOWN and the string value (with 'callStatus' key) is added to the providerExtraData
     * CallDetailRecord map field.
     *
     * @param config
     * @param key
     * @param value
     * @param callDetailRecord
     */
    public static void setCallDetail(Config config, String key, String value, CallDetailRecord callDetailRecord) {
        String fieldName;
        if (config.callDetailMap.containsKey(key)) {
            fieldName = config.callDetailMap.get(key);
        }
        else {
            fieldName = key;
        }
        try {
            java.lang.reflect.Field field = callDetailRecord.getClass().getField(fieldName);
            Object object;
            try {
                if (fieldName.equals("callStatus")) {
                    object = mapStatus(config, value);
                    if (CallStatus.UNKNOWN.equals(object)) {
                        // Always add unknown call status to the provider extra data, for inspection
                        callDetailRecord.providerExtraData.put(fieldName, value);
                    }
                }
                else {
                    object = value;
                }
                field.set(callDetailRecord, object);
            } catch (IllegalAccessException e) {
                // This should never happen as all CallDetailRecord fields should be accessible, but if somehow there
                // happens to be a 'final' public field with the same name as a call detail key, then this will throw
                throw new IllegalStateException(String.format("Unable to set call detail record field '%s' value:\n%s",
                        fieldName, e));
            }
        } catch (NoSuchFieldException e) {
            callDetailRecord.providerExtraData.put(fieldName, value);
        }
    }
}
