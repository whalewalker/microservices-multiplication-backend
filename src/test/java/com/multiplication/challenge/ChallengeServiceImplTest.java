package com.multiplication.challenge;

import com.multiplication.challenge.data.dto.ChallengeAttemptDto;
import com.multiplication.challenge.data.model.ChallengeAttempt;
import com.multiplication.challenge.data.repository.ChallengeAttemptRepository;
import com.multiplication.challenge.service.ChallengeServiceImpl;
import com.multiplication.serviceclients.GamificationServiceClient;
import com.multiplication.user.data.model.User;
import com.multiplication.user.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChallengeServiceImplTest {

    @InjectMocks
    private ChallengeServiceImpl challengeService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GamificationServiceClient gameClient;
    @Mock
    private ChallengeAttemptRepository attemptRepository;

    @AfterEach
    void tearDown() {
        challengeService = null;
    }

    @Test
    void checkCorrectAttemptTest() {
        ChallengeAttemptDto attemptDto =
                new ChallengeAttemptDto(50, 60, 3000, "john_doe");
        given(attemptRepository.save(any())).will(returnsFirstArg());


        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDto);

        then(resultAttempt.isCorrect()).isTrue();

        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(resultAttempt);
        verify(gameClient).sendAttempt(resultAttempt);
    }

    @Test
    void checkWrongAttemptTest() {
        ChallengeAttemptDto attemptDto =
                new ChallengeAttemptDto(50, 60, 5000, "john_doe");
        given(attemptRepository.save(any())).will(returnsFirstArg());


        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDto);

        then(resultAttempt.isCorrect()).isFalse();
    }

    @Test
    void checkExistingUserTest() {
        User existingUser = new User(1L, "john_doe");
        given(attemptRepository.save(any())).will(returnsFirstArg());
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.of(existingUser));

        ChallengeAttemptDto attemptDTO =
                new ChallengeAttemptDto(50, 60,  5000, "john_doe");

        ChallengeAttempt resultAttempt =
                challengeService.verifyAttempt(attemptDTO);

        then(resultAttempt.isCorrect()).isFalse();
        then(resultAttempt.getUser()).isEqualTo(existingUser);

        verify(userRepository, never()).save(any());
        verify(attemptRepository).save(resultAttempt);
    }

    @Test
    void retrieveStatsTest(){
        ChallengeAttempt expectedAttempt = new ChallengeAttempt(1L, new User(), 50, 60,  5000, true);
        given(attemptRepository.findTop10ByUserAliasOrderByIdDesc("john_doe"))
                .willReturn(List.of(expectedAttempt));

        List<ChallengeAttempt> resultAttempts = challengeService.getStatsForUser("john_doe");

        then(resultAttempts.size()).isEqualTo(1);

        verify(attemptRepository).findTop10ByUserAliasOrderByIdDesc(anyString());
    }
}