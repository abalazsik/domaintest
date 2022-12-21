package org.abalazsik.domaintest.data;

import java.io.Serializable;
import java.util.Objects;

public class Candidate implements Serializable {

    private CandidateName name;
    private CandidateEmail email;
    private CandidateCreationTime creationTime;

    public Candidate() {}

    public Candidate(CandidateName name, CandidateEmail email, CandidateCreationTime creationTime) {
        this.name = name;
        this.email = email;
        this.creationTime = creationTime;
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

    public CandidateCreationTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(CandidateCreationTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(name, candidate.name) && Objects.equals(email, candidate.email) && Objects.equals(creationTime, candidate.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, creationTime);
    }
}
