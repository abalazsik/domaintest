package org.abalazsik.domaintest;

import org.abalazsik.domaintest.data.CreateCandidate;
import org.abalazsik.domaintest.data.Candidate;
import org.abalazsik.domaintest.data.CandidateName;
import org.abalazsik.domaintest.data.CandidateEmail;
import org.abalazsik.domaintest.data.CandidateCreationTime;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.easymock.EasyMock.*;

@RunWith(EasyMockRunner.class)
public class AsyncCandidateServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private TimeSource timeSource;

    @Mock
    private AsyncCandidateRepository candidateRepository;

    private AsyncCandidateService candidateService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private static final CandidateEmail EMAIL = new CandidateEmail("candidate@email.com");

    @Before
    public void setup() {
        candidateService = new AsyncCandidateService(executorService, candidateRepository, timeSource);
    }

    @Test
    public void expectSaveWithDate() {
        LocalDateTime now = LocalDateTime.parse("2022-11-29T09:35:00");

        expect(timeSource.now()).andReturn(now).once();
        replay(timeSource);

        expect(candidateRepository.save(anyObject())).andAnswer(() -> {
            return immediately(getCurrentArgument(0));
        });
        expect(candidateRepository.exists(EMAIL)).andReturn(immediately(Boolean.FALSE));
        replay(candidateRepository);

        CreateCandidate createCandidate = new CreateCandidate(
                new CandidateName("candidate"), EMAIL);

        Future<Candidate> result = candidateService.save(createCandidate);

        Assert.assertEquals(now, get(result).getCreationTime().getValue());
    }

    @Test
    public void expectSaveThrowsExceptionWhenCandidateAlreadyExists() {
        thrown.expect(RuntimeException.class);
        expect(candidateRepository.exists(EMAIL)).andReturn(immediately(Boolean.TRUE));
        replay(candidateRepository);

        CreateCandidate createCandidate = new CreateCandidate(
                new CandidateName("candidate"), EMAIL);

        get(candidateService.save(createCandidate));
    }

    @Test
    public void expectGetByEmailReturnsExistingCandidate() {

        Candidate candidate = new Candidate(
                new CandidateName("candidate"),
                EMAIL,
                new CandidateCreationTime(LocalDateTime.parse("2022-11-29T09:35:00"))
        );

        expect(candidateRepository.getByEmail(EMAIL))
                .andReturn(immediately(candidate));
        replay(candidateRepository);

        Assert.assertEquals(candidate, get(candidateService.getByEmail(EMAIL)));
    }

    private <T> Future<T> immediately(T value) {
        return executorService.submit(() -> {
            return value;
        });
    }

    private <T> T get(Future<T> future) {
        try {
            return (T) future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
