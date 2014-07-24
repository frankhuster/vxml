package org.motechproject.vxml.config;

import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.CallStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * todo
 */
public class Config {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, CallStatus> statusMap = new HashMap<>();
    private Map<String, String> callDetailMap = new HashMap<>();

    public Config(Map<String, CallStatus> statusMap, Map<String, String> callDetailMap) {
        this.statusMap = statusMap;
        this.callDetailMap = callDetailMap;
    }

    private CallStatus mapStatus(String statusString) {
        if (statusMap.containsKey(statusString)) {
            return statusMap.get(statusString);
        }
        logger.error("Unknown status string: {}", statusString);
        return CallStatus.UNKNOWN;
    }

    public void setCallDetail(String key, String value, CallDetailRecord callDetailRecord) {
        String fieldName;
        if (callDetailMap.containsKey(key)) {
            fieldName = callDetailMap.get(key);
        }
        else {
            fieldName = key;
        }
        try {
            Field field = callDetailRecord.getClass().getField(fieldName);
            Object object;
            try {
                if (fieldName.equals("callStatus")) {
                    object = mapStatus(value);
                    if (CallStatus.UNKNOWN.equals(object)) {
                        // Always add unknown call status to the provider extra data, for inspection
                        callDetailRecord.providerData.put(fieldName, value);
                    }
                }
                else {
                    object = value;
                }
                field.set(callDetailRecord, object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new IllegalStateException("Unable to set call detail record field value: {}", e.getCause());
            }
        } catch (NoSuchFieldException e) {
            callDetailRecord.providerData.put(fieldName, value);
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "statusMap=" + statusMap +
                ", callDetailMap=" + callDetailMap +
                '}';
    }
}
