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

import static org.easymock.EasyMock.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(EasyMockRunner.class)
public class CandidateServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    TimeSource timeSource;

    @Mock
    private CandidateRepository candidateRepository;

    private CandidateService candidateService;

    private CandidateEmail EMAIL = new CandidateEmail("candidate@email.com");

    @Before
    public void setup() {
        candidateService = new CandidateService(candidateRepository, timeSource);
    }

    @Test
    public void expectSaveWithDate() {
        LocalDateTime now = LocalDateTime.parse("2022-12-21T22:35:00");

        expect(timeSource.now()).andReturn(now).once();
        replay(timeSource);

        expect(candidateRepository.save(anyObject())).andAnswer(() -> {
            return getCurrentArgument(0);
        });
        expect(candidateRepository.exists(EMAIL)).andReturn(Boolean.FALSE);
        replay(candidateRepository);

        CreateCandidate createCandidate = new CreateCandidate(
                new CandidateName("candidate"), EMAIL);

        Candidate result = candidateService.save(createCandidate);

        Assert.assertEquals(now, result.getCreationTime().getValue());
    }

    @Test
    public void expectSaveThrowsExceptionWhenCandidateAlreadyExists() {
        thrown.expect(CandidateException.class);
        expect(candidateRepository.exists(EMAIL)).andReturn(Boolean.TRUE);
        replay(candidateRepository);

        CreateCandidate createCandidate = new CreateCandidate(
                new CandidateName("candidate"), EMAIL);

        candidateService.save(createCandidate);
    }

    @Test
    public void expectGetByEmailReturnsExistingCandidate() {
        Candidate candidate = new Candidate(
                new CandidateName("candidate"),
                EMAIL,
                new CandidateCreationTime(LocalDateTime.parse("2022-12-21T22:35:00"))
        );

        expect(candidateRepository.getByEmail(EMAIL))
                .andReturn(Optional.of(candidate));
        replay(candidateRepository);

        Assert.assertEquals(candidate, candidateService.getByEmail(EMAIL).get());
    }
}
