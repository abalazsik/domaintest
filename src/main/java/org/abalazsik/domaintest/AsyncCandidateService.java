package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateCreationTime;
import org.abalazsik.domaintest.data.CandidateEmail;
import org.abalazsik.domaintest.data.CreateCandidate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AsyncCandidateService {

    private AsyncCandidateRepository repository;
    private TimeSource timeSource;
    private ExecutorService executorService;

    public AsyncCandidateService(ExecutorService executorService, AsyncCandidateRepository repository, TimeSource timeSource) {
        this.executorService = executorService;
        this.repository = repository;
        this.timeSource = timeSource;
    }

    Future<Candidate> save(CreateCandidate createCandidate) {
        return executorService.submit(() -> {
            if (get(repository.exists(createCandidate.getEmail())) == Boolean.TRUE) {
                throw new CandidateException("Candidate already exists");
            }

            Candidate candidateToSave = new Candidate(
                    createCandidate.getName(),
                    createCandidate.getEmail(),
                    new CandidateCreationTime(timeSource.now())
            );

            return get(repository.save(candidateToSave));
        });

    }

    Future<Candidate> getByEmail(CandidateEmail email) {
        return repository.getByEmail(email);
    }

    private <T> T get(Future<T> future) {
        try {
            return (T) future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
