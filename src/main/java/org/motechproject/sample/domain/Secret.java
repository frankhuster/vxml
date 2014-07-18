package org.motechproject.sample.domain;

import org.motechproject.mds.annotations.Entity;

@Entity
public class Secret {
    private String message;

    public Secret(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Secret)) return false;

        Secret secret = (Secret) o;

        if (message != null ? !message.equals(secret.message) : secret.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "message='" + message + '\'' +
                '}';
    }
}
