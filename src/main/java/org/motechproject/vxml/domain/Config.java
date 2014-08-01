package org.motechproject.vxml.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.HashMap;
import java.util.Map;

/**
 * todo
 */
@Entity
public class Config {
    @Field
    public String name;

    @Field
    public Map<String, CallStatus> statusMap = new HashMap<>();

    @Field
    public Map<String, String> callDetailMap = new HashMap<>();

    // This constructor needed to avoid an MDS error when creating an entity using the MDS Data Browser UI...
    public Config() {  }

    public Config(String name, Map<String, CallStatus> statusMap, Map<String, String> callDetailMap) {
        this.name = name;
        this.statusMap = statusMap;
        this.callDetailMap = callDetailMap;
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
        int result = name.hashCode();
        result = 31 * result + statusMap.hashCode();
        result = 31 * result + callDetailMap.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", statusMap=" + statusMap +
                ", callDetailMap=" + callDetailMap +
                '}';
    }
}
