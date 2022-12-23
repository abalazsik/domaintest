package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.CreateCandidate;
import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateName;
import org.abalazsik.domaintest.data.CandidateEmail;
import org.abalazsik.domaintest.data.CandidateCreationTime;
import io.reactivex.rxjava3.core.Observable;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;

@RunWith(EasyMockRunner.class)
public class ObservableCandidateServiceTest {

    @Mock
    private TimeSource timeSource;

    @Mock
    private ObservableCandidateRepository candidateRepository;

    private ObservableCandidateService candidateService;

    private static final CandidateEmail EMAIL = new CandidateEmail("candidate@email.com");

    @Before
    public void setup() {
        candidateService = new ObservableCandidateService(candidateRepository, timeSource);
    }

    @Test
    public void expectSaveWithDate() {
        LocalDateTime now = LocalDateTime.parse("2022-12-21T22:35:00");

        expect(timeSource.now()).andReturn(now).once();
        replay(timeSource);

        expect(candidateRepository.save(anyObject())).andAnswer(() -> {
            return Observable.just(getCurrentArgument(0));
        });
        expect(candidateRepository.exists(EMAIL)).andReturn(Observable.just(Boolean.FALSE));
        replay(candidateRepository);

        CreateCandidate createCandidate = new CreateCandidate(
                new CandidateName("candidate"), EMAIL);

        candidateService.save(createCandidate)
                .subscribe(result -> {
                    Assert.assertEquals(now, result.getCreationTime().getValue());
                },
                        error -> {
                            Assert.fail(error.getMessage());
                        });

    }

    @Test
    public void expectSaveThrowsExceptionWhenCandidateAlreadyExists() {
        LocalDateTime now = LocalDateTime.parse("2022-12-21T22:35:00");

        expect(timeSource.now()).andReturn(now).once();
        replay(timeSource);
        
        expect(candidateRepository.exists(EMAIL)).andReturn(Observable.just(Boolean.TRUE));
        replay(candidateRepository);

        CreateCandidate createCandidate = new CreateCandidate(
                new CandidateName("candidate"), EMAIL);

        candidateService.save(createCandidate).subscribe(
                success -> Assert.fail(),
                error -> Assert.assertTrue(error instanceof CandidateException));
    }

    @Test
    public void expectGetByEmailReturnsExistingCandidate() {
        Candidate candidate = new Candidate(
                new CandidateName("candidate"),
                EMAIL,
                new CandidateCreationTime(LocalDateTime.parse("2022-12-21T22:35:00"))
        );

        expect(candidateRepository.getByEmail(EMAIL))
                .andReturn(Observable.just(candidate));
        replay(candidateRepository);

        candidateService.getByEmail(EMAIL).subscribe(
                returned -> Assert.assertEquals(candidate, returned),
                error -> Assert.fail());
    }
}
