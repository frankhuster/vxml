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

    public Config(String name, Map<String, CallStatus> statusMap, Map<String, String> callDetailMap) {
        this.name = name;
        this.statusMap = statusMap;
        this.callDetailMap = callDetailMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Config)) return false;

        Config config = (Config) o;

        //todo: I'm using a string compare because comparing the statusMap field (of type Map<String, CallStatus>) fails
        return this.toString().equals(o.toString());

//        if (!name.equals(config.name)) return false;
//todo:   why does the comparison below fail when comparing two configs where one is loaded from the DB and one is
//todo:   created in memory?!? Argh!
//        if (!statusMap.equals(config.statusMap)) return false;
//        if (!callDetailMap.equals(config.callDetailMap)) return false;
//
//        return true;
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
