package org.motechproject.vxml.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Map;

@Entity
public class CallDetailRecord {

    @Field(required = true)
    private DateTime timestamp;

    @Field
    private String config;

    @Field
    private String from;

    @Field
    private String to;

    @Field
    private CallStatus callStatus;

    @Field
    private String providerStatus;

    @Field
    private String motechCallId;

    @Field
    private String providerCallId;

    @Field
    private Map<String, String> providerData;

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public CallStatus getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    public String getMotechCallId() {
        return motechCallId;
    }

    public void setMotechCallId(String motechCallId) {
        this.motechCallId = motechCallId;
    }

    public String getProviderCallId() {
        return providerCallId;
    }

    public void setProviderCallId(String providerCallId) {
        this.providerCallId = providerCallId;
    }

    public Map<String, String> getProviderData() {
        return providerData;
    }

    public void setProviderData(Map<String, String> providerData) {
        this.providerData = providerData;
    }

    public CallDetailRecord(DateTime timestamp, String config, String from, String to, CallStatus callStatus,
                            String providerStatus, String motechCallId, String providerCallId,
                            Map<String, String> providerData) {
        this.timestamp = timestamp;
        this.config = config;
        this.from = from;
        this.to = to;
        this.callStatus = callStatus;
        this.providerStatus = providerStatus;
        this.motechCallId = motechCallId;
        this.providerCallId = providerCallId;
        this.providerData = providerData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CallDetailRecord)) return false;

        CallDetailRecord that = (CallDetailRecord) o;

        if (callStatus != that.callStatus) return false;
        if (!config.equals(that.config)) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (motechCallId != null ? !motechCallId.equals(that.motechCallId) : that.motechCallId != null) return false;
        if (providerCallId != null ? !providerCallId.equals(that.providerCallId) : that.providerCallId != null)
            return false;
        if (providerData != null ? !providerData.equals(that.providerData) : that.providerData != null) return false;
        if (providerStatus != null ? !providerStatus.equals(that.providerStatus) : that.providerStatus != null)
            return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + config.hashCode();
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + callStatus.hashCode();
        result = 31 * result + (providerStatus != null ? providerStatus.hashCode() : 0);
        result = 31 * result + (motechCallId != null ? motechCallId.hashCode() : 0);
        result = 31 * result + (providerCallId != null ? providerCallId.hashCode() : 0);
        result = 31 * result + (providerData != null ? providerData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CallDetailRecord{" +
                "timestamp=" + timestamp +
                ", config='" + config + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", callStatus=" + callStatus +
                ", providerStatus='" + providerStatus + '\'' +
                ", motechCallId='" + motechCallId + '\'' +
                ", providerCallId='" + providerCallId + '\'' +
                ", providerData=" + providerData +
                '}';
    }
}