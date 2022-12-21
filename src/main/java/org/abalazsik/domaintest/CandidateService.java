package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateCreationTime;
import org.abalazsik.domaintest.data.CandidateEmail;
import org.abalazsik.domaintest.data.CreateCandidate;

import java.util.Optional;

public class CandidateService {

    private CandidateRepository repository;
    private TimeSource timeSource;

    public CandidateService(CandidateRepository repository, TimeSource timeSource) {
        if (repository == null) {
            throw new RuntimeException("repository cannot be null!");
        }
        this.repository = repository;
        if (timeSource == null) {
            throw new RuntimeException("timeSource cannot be null!");
        }
        this.timeSource = timeSource;
    }

    public Candidate save(CreateCandidate createCandidate) {
        if (repository.exists(createCandidate.getEmail())) {
            throw new CandidateException("Candidate already exists");
        }

        Candidate candidateToSave = new Candidate(
                createCandidate.getName(),
                createCandidate.getEmail(),
                new CandidateCreationTime(timeSource.now())
        );

        return repository.save(candidateToSave);
    }

    public Optional<Candidate> getByEmail(CandidateEmail email) {
        return repository.getByEmail(email);
    }

}
