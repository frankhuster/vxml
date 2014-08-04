package org.motechproject.vxml.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.HashMap;
import java.util.Map;

@Entity
public class CallDetailRecord {

    @Field(required = true)
    public DateTime timestamp;

    @Field
    public String config;

    @Field
    public String from;

    @Field
    public String to;

    @Field
    public CallStatus callStatus;

    @Field
    public String providerStatus;

    @Field
    public String motechCallId;

    @Field
    public String providerCallId;

    @Field
    public Map<String, String> providerExtraData;

    public CallDetailRecord() {
        providerExtraData = new HashMap<>();
        callStatus = CallStatus.UNKNOWN;
    }

    public CallDetailRecord(DateTime timestamp, String config, String from, String to, CallStatus callStatus,
                            String providerStatus, String motechCallId, String providerCallId,
                            Map<String, String> providerExtraData) {
        this();
        this.timestamp = timestamp;
        this.config = config;
        this.from = from;
        this.to = to;
        this.callStatus = callStatus;
        this.providerStatus = providerStatus;
        this.motechCallId = motechCallId;
        this.providerCallId = providerCallId;
        if (providerExtraData != null) {
            this.providerExtraData = providerExtraData;
        }
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
        if (providerExtraData != null ? !providerExtraData.equals(that.providerExtraData) :
                that.providerExtraData != null) return false;
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
        result = 31 * result + (providerExtraData != null ? providerExtraData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CallDetailRecord{" +
                "timestamp=" + timestamp +
                ", domain='" + config + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", callStatus=" + callStatus +
                ", providerStatus='" + providerStatus + '\'' +
                ", motechCallId='" + motechCallId + '\'' +
                ", providerCallId='" + providerCallId + '\'' +
                ", providerExtraData=" + providerExtraData +
                '}';
    }
}