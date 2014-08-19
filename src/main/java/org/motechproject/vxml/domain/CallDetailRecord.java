package org.motechproject.vxml.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.HashMap;
import java.util.Map;

@Entity
public class CallDetailRecord {

    @Field(required = true)
    public String timestamp;

    @Field
    public String config;

    @Field
    public String from;

    @Field
    public String to;

    @Field
    public CallDirection callDirection;

    @Field
    public CallStatus callStatus;

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

    public CallDetailRecord(String timestamp, String config, String from, String to, CallDirection callDirection,
                            CallStatus callStatus, String motechCallId, String providerCallId,
                            Map<String, String> providerExtraData) {
        this();
        this.timestamp = timestamp;
        this.config = config;
        this.from = from;
        this.to = to;
        this.callDirection = callDirection;
        this.callStatus = callStatus;
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

        if (callDirection != that.callDirection) return false;
        if (callStatus != that.callStatus) return false;
        if (!config.equals(that.config)) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (motechCallId != null ? !motechCallId.equals(that.motechCallId) : that.motechCallId != null) return false;
        if (providerCallId != null ? !providerCallId.equals(that.providerCallId) : that.providerCallId != null)
            return false;
        if (providerExtraData != null ? !providerExtraData.equals(that.providerExtraData) :
                that.providerExtraData != null) return false;
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
        result = 31 * result + callDirection.hashCode();
        result = 31 * result + callStatus.hashCode();
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
                ", callDirection=" + callDirection +
                ", callStatus=" + callStatus +
                ", motechCallId='" + motechCallId + '\'' +
                ", providerCallId='" + providerCallId + '\'' +
                ", providerExtraData=" + providerExtraData +
                '}';
    }
}