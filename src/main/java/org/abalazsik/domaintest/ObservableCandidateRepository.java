package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateEmail;
import io.reactivex.rxjava3.core.Observable;

public interface ObservableCandidateRepository {

    Observable<Candidate> save(Candidate candidate);

    Observable<Boolean> exists(CandidateEmail email);

    Observable<Candidate> getByEmail(CandidateEmail email);
}
