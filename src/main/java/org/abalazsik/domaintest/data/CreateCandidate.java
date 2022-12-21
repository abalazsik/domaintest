package org.abalazsik.domaintest.data;

import java.io.Serializable;

public class CreateCandidate implements Serializable {
    private CandidateName name;
    private CandidateEmail email;

    public CreateCandidate() {}

    public CreateCandidate(CandidateName name, CandidateEmail email) {
        this.name = name;
        this.email = email;
    }

    public CandidateName getName() {
        return name;
    }

    public void setName(CandidateName name) {
        this.name = name;
    }

    public CandidateEmail getEmail() {
        return email;
    }

    public void setEmail(CandidateEmail email) {
        this.email = email;
    }
}
