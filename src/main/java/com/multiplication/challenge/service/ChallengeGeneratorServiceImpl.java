package com.multiplication.challenge.service;

import com.multiplication.challenge.data.model.Challenge;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.multiplication.util.Constants.MAXIMUM_FACTOR;
import static com.multiplication.util.Constants.MINIMUM_FACTOR;

@Service
public class ChallengeGeneratorServiceImpl implements ChallengeGeneratorService{
    private final Random random;

    public ChallengeGeneratorServiceImpl() {
        this.random = new Random();
    }

    public ChallengeGeneratorServiceImpl(Random random) {
        this.random = random;
    }

    @Override
    public Challenge randomChallenge() {
        return new Challenge(next(), next());
    }

    private int next(){
        return random.nextInt(MAXIMUM_FACTOR - MINIMUM_FACTOR) + MINIMUM_FACTOR;
    }
}
