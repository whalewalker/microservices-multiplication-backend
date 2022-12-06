package com.multiplication.challenge;

import com.multiplication.challenge.data.model.Challenge;
import com.multiplication.challenge.service.ChallengeGeneratorService;
import com.multiplication.challenge.service.ChallengeGeneratorServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChallengeGeneratorServiceImplTest {
    private ChallengeGeneratorService challengeGeneratorService;

    @Spy
    private Random random;

    @BeforeEach
    void setUp() {
        challengeGeneratorService = new ChallengeGeneratorServiceImpl(random);
    }

    @AfterEach
    void tearDown() {
        random = null;
        challengeGeneratorService = null;
    }

    @Test
    void generateRandomFactorIsBetweenExpectedLimits(){
        given(random.nextInt(89)).willReturn(20, 30);

        Challenge challenge = challengeGeneratorService.randomChallenge();

        then(challenge).isEqualTo(new Challenge(31, 41));
    }
}