package org.motechproject.vxml.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class CallDetailsRecord {

    @Field(required = true)
    private DateTime timestamp;

    @Field
    private String from;

    @Field
    private String to;

    @Field
    private CallStatus callStatus;

    @Field
    private String providerStatus;

    @Field
    private String motechId;

    @Field
    private String providerId;

    @Field
    private String providerData;

    public CallDetailsRecord(DateTime timestamp, String from, String to, CallStatus callStatus, String providerStatus,
                             String motechId, String providerId, String providerData) {
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.callStatus = callStatus;
        this.providerStatus = providerStatus;
        this.motechId = motechId;
        this.providerId = providerId;
        this.providerData = providerData;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
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

    public String getMotechId() {
        return motechId;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderData() {
        return providerData;
    }

    public void setProviderData(String providerData) {
        this.providerData = providerData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CallDetailsRecord)) return false;

        CallDetailsRecord that = (CallDetailsRecord) o;

        if (callStatus != that.callStatus) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (motechId != null ? !motechId.equals(that.motechId) : that.motechId != null) return false;
        if (providerData != null ? !providerData.equals(that.providerData) : that.providerData != null) return false;
        if (providerId != null ? !providerId.equals(that.providerId) : that.providerId != null) return false;
        if (providerStatus != null ? !providerStatus.equals(that.providerStatus) : that.providerStatus != null)
            return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (callStatus != null ? callStatus.hashCode() : 0);
        result = 31 * result + (providerStatus != null ? providerStatus.hashCode() : 0);
        result = 31 * result + (motechId != null ? motechId.hashCode() : 0);
        result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
        result = 31 * result + (providerData != null ? providerData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CallDetailsRecord{" +
                "timestamp=" + timestamp +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", callStatus=" + callStatus +
                ", providerStatus='" + providerStatus + '\'' +
                ", motechId='" + motechId + '\'' +
                ", providerId='" + providerId + '\'' +
                ", providerData='" + providerData + '\'' +
                '}';
    }
}