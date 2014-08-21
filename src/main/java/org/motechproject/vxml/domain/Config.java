package org.motechproject.vxml.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import java.util.HashMap;
import java.util.Map;

/**
 * todo
 */
@Entity
public class Config {
    /**
     * How a config is identified
     */
    @Field
    @Unique
    public String name;

    /**
     * CSV list of fields the IVR provider sends to the status controller which shouldn't be included (ie: ignored) in
     * the CDR data
     */
    @Field
    public String ignoredStatusFields;

    /**
     * Template string used to issue an HTTP GET call to the IVR provider in order to initiate an outgoing (MT) call.
     * [xxx] placeholders are replaced with the values provided in the initiateCall() method.
     * todo: better javadoc when that's actually in place
     */
    @Field
    public String outgoingCallUriTemplate;

    /**
     * A map of parameters to be substituted to the outgoing URI template
     */
    @Field
    public Map<String, String> outgoingCallUriParams = new HashMap<>();


    //todo: This constructor needed to avoid an MDS error when creating an entity using the MDS Data Browser UI...
    //todo: Remove when https://applab.atlassian.net/browse/MOTECH-1202 is fixed
    public Config() {  }

    public Config(String name, String ignoredStatusFields, String outgoingCallUriTemplate,
                  Map<String, String> outgoingCallUriParams) {
        this.name = name;
        this.ignoredStatusFields = ignoredStatusFields;
        this.outgoingCallUriTemplate = outgoingCallUriTemplate;
        this.outgoingCallUriParams = outgoingCallUriParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Config)) return false;

        //todo: I'm using a string compare because comparing the statusMap field (of type Map<String, CallStatus>) fails
        //todo: change to a proper full fledged equals when https://applab.atlassian.net/browse/MOTECH-1186 is fixed
        return this.toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (ignoredStatusFields != null ? ignoredStatusFields.hashCode() : 0);
        result = 31 * result + (outgoingCallUriTemplate != null ? outgoingCallUriTemplate.hashCode() : 0);
        result = 31 * result + (outgoingCallUriParams != null ? outgoingCallUriParams.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", ignoredStatusFields='" + ignoredStatusFields + '\'' +
                ", outgoingCallUriTemplate='" + outgoingCallUriTemplate + '\'' +
                ", outgoingCallUriParams='" + outgoingCallUriParams + '\'' +
                '}';
    }
}
