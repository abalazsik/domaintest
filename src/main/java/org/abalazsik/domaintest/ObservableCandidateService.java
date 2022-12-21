package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateCreationTime;
import org.abalazsik.domaintest.data.CandidateEmail;
import org.abalazsik.domaintest.data.CreateCandidate;
import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDateTime;

public class ObservableCandidateService {

    private ObservableCandidateRepository repository;

    private TimeSource timeSource;

    public ObservableCandidateService(ObservableCandidateRepository repository, TimeSource timeSource) {
        this.repository = repository;
        this.timeSource = timeSource;
    }

    public Observable<Candidate> save(CreateCandidate createCandidate) {
        return Observable.create(subscriber -> {
            repository.exists(createCandidate.getEmail()).subscribe(exists -> {
                if (exists) {
                    subscriber.onError(new CandidateException("Candidate already exists"));
                    subscriber.onComplete();
                }

                LocalDateTime now = timeSource.now();

                Candidate candidateToSave = new Candidate(
                        createCandidate.getName(),
                        createCandidate.getEmail(),
                        new CandidateCreationTime(now)
                );

                repository.save(candidateToSave).subscribe(saved -> {
                    subscriber.onNext(saved);
                    subscriber.onComplete();
                }, error -> subscriber.onError(error));
            }, error -> subscriber.onError(error));
        });
    }

    public Observable<Candidate> getByEmail(CandidateEmail email) {
        return repository.getByEmail(email);
    }
}
