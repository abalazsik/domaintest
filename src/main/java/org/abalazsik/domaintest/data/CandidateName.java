package org.abalazsik.domaintest.data;

import java.io.Serializable;

public class CandidateName implements Serializable {

    private String value;

    public CandidateName(String value) {
        if (value == null) {
            throw new IllegalArgumentException("name cannot be null!");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
