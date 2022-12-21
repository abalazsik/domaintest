package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateEmail;

import java.util.Optional;

public interface CandidateRepository {

    Candidate save(Candidate candidate);

    boolean exists(CandidateEmail email);

    Optional<Candidate> getByEmail(CandidateEmail email);
}
