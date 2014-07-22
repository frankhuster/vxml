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
    private String status;

    @Field
    private String motechId;

    @Field
    private String providerId;

    @Field
    private String providerData;

    public CallDetailsRecord(DateTime timestamp, String from, String to, String status, String motechId, String providerId, String providerData) {
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (motechId != null ? !motechId.equals(that.motechId) : that.motechId != null) return false;
        if (providerData != null ? !providerData.equals(that.providerData) : that.providerData != null) return false;
        if (providerId != null ? !providerId.equals(that.providerId) : that.providerId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (motechId != null ? motechId.hashCode() : 0);
        result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
        result = 31 * result + (providerData != null ? providerData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CallRecord{" +
                "timestamp=" + timestamp +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", status='" + status + '\'' +
                ", motechId='" + motechId + '\'' +
                ", providerId='" + providerId + '\'' +
                ", providerData='" + providerData + '\'' +
                '}';
    }
}