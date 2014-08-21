package org.motechproject.vxml.domain;

import org.motechproject.vxml.repository.ConfigDataService;
import org.motechproject.vxml.service.MotechStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * todo
 */
public class ConfigHelper {
    private static Logger logger = LoggerFactory.getLogger(ConfigHelper.class);
    private static final int MAX_ENTITY_STRING_LENGTH = 255;

    private static CallStatus mapStatus(Config config, String statusString) {
        try {
                return CallStatus.valueOf(statusString);
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
        if (value.length() > MAX_ENTITY_STRING_LENGTH) {
            logger.warn("The value for {} exceeds {} characters and will be truncated.", key, MAX_ENTITY_STRING_LENGTH);
            logger.warn("The complete value for {} is {}", key, value);
            value = value.substring(0, MAX_ENTITY_STRING_LENGTH);
        }

        try {
            java.lang.reflect.Field field = callDetailRecord.getClass().getField(key);
            Object object;
            try {
                if ("callStatus".equals(key)) {
                    try {
                        object = CallStatus.valueOf(value);
                    } catch (IllegalArgumentException e) {
                        // Always add unknown call status to the provider extra data, for inspection
                        logger.warn("Unknown callStatus: {}", value);
                        callDetailRecord.providerExtraData.put(key, value);
                        object = CallStatus.UNKNOWN;
                    }
                }
                else if ("callDirection".equals(key)) {
                    try {
                        CallDirection callDirection = CallDirection.valueOf(value);
                        object = callDirection;
                    } catch (IllegalArgumentException e) {
                        // Always add unknown call directions to the provider extra data, for inspection
                        logger.warn("Unknown callDirection: {}", value);
                        callDetailRecord.providerExtraData.put(key, value);
                        object = CallDirection.UNKNOWN;
                    }
                }
                else {
                    object = value;
                }
                field.set(callDetailRecord, object);
            } catch (IllegalAccessException e) {
                // This should never happen as all CallDetailRecord fields should be accessible, but if somehow there
                // happens to be a 'final' public field with the same name as a call detail key, then this will throw
                throw new IllegalStateException(String.format(
                        "Unable to set call detail record field '%s' to value '%s':\n%s", key, value, e));
            }
        } catch (NoSuchFieldException e) {
            logger.info("Extra data from provider: '{}': '{}'", key, value);
            callDetailRecord.providerExtraData.put(key, value);
        }
    }

    /**
     * Returns a URI where all the placeholders that can be are substituted from outgoingCallUriParams
     *
     * @param config
     * @return
     */
    public static String outgoingCallUri(Config config) {
        String uri = config.outgoingCallUriTemplate;

        for (Map.Entry<String, String> entry : config.outgoingCallUriParams.entrySet()) {
            String placeholder = String.format("[%s]", entry.getKey());
            if (uri.contains(placeholder)) {
                uri = uri.replace(placeholder, entry.getValue());
            }
        }

        return uri;
    }

    /**
     * Returns a Config object from the database given a configName. Logs an error and sends a MotechStatusMessage if
     * the configName doesn't exist in the database then throws an IllegalStateException
     *
     * @param configDataService  handle to the MDS service for the Config table
     * @param motechStatusMessage  handle to the MotechStatusMessage service
     * @param configName  name of the config to read from the database
     * @return
     */
    public static Config getConfig(ConfigDataService configDataService, MotechStatusMessage motechStatusMessage,
                                   String configName) {
        List<Config> configs = configDataService.findAllByName(configName);
        if (null == configs || configs.size() < 1) {
            String msg = String.format("No matching config in the database for: %s", configName);
            logger.error(msg);
            if (null != motechStatusMessage) {
                motechStatusMessage.alert(msg);
            }
            throw new IllegalArgumentException(msg);
        }
        if (configs.size() > 1) {
            String msg = String.format("More than one matching config in the database for: %s\n%s", configName,
                    configs.toString());
            logger.error(msg);
            if (null != motechStatusMessage) {
                motechStatusMessage.alert(msg);
            }
            throw new IllegalArgumentException(msg);
        }
        return configs.get(0);
    }
}
