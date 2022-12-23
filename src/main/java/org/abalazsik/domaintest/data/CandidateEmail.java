package org.abalazsik.domaintest.data;

import java.io.Serializable;
import java.util.Objects;

public class CandidateEmail implements Serializable {

    private String value;

    public CandidateEmail(String value) {
        if (value == null) {
            throw new IllegalArgumentException("email cannot be null!");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CandidateEmail other = (CandidateEmail) obj;
        return Objects.equals(this.value, other.value);
    }
    
}
