package org.motechproject.sample.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Spy {

    @Field(required = true)
    private String name;

    @Field
    @Cascade(persist = false, update = false, delete = false)
    private List<Secret> secrets = new ArrayList<>();

    public Spy(String name) {
        this(name, null);
    }

    public Spy(String name, List<Secret> secrets) {
        this.name = name;
        if (null != secrets) {
            this.secrets = secrets;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Secret> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<Secret> secrets) {
        if (null == secrets) {
            this.secrets = new ArrayList<>();
        } else {
            this.secrets = secrets;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spy)) return false;

        Spy spy = (Spy) o;

        if (secrets != null ? !secrets.equals(spy.secrets) : spy.secrets != null) return false;
        if (!name.equals(spy.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (secrets != null ? secrets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Spy{" +
                "name='" + name + '\'' +
                ", secrets=" + secrets +
                '}';
    }
}