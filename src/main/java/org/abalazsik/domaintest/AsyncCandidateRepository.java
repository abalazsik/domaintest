package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateEmail;
import java.util.concurrent.Future;

public interface AsyncCandidateRepository {

    Future<Candidate> save(Candidate candidate);

    Future<Boolean> exists(CandidateEmail email);

    Future<Candidate> getByEmail(CandidateEmail email);
}
