package org.abalazsik.domaintest.data;

import java.io.Serializable;

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

}
