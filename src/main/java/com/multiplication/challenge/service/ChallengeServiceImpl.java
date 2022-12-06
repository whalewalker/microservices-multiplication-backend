package com.multiplication.challenge.service;

import com.multiplication.challenge.data.dto.ChallengeAttemptDto;
import com.multiplication.challenge.data.model.ChallengeAttempt;
import com.multiplication.challenge.data.repository.ChallengeAttemptRepository;
import com.multiplication.serviceclients.GamificationServiceClient;
import com.multiplication.user.data.model.User;
import com.multiplication.user.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeServiceImpl implements ChallengeService{

    private final UserRepository userRepository;
    private final ChallengeAttemptRepository challengeAttemptRepository;
    private final GamificationServiceClient gameClient;
    @Override
    public ChallengeAttempt verifyAttempt(ChallengeAttemptDto resultAttempt) {
        User user = userRepository.findByAlias(resultAttempt.getAlias())
                .orElseGet(()-> {
                    log.info("Creating new user with alias {}", resultAttempt.getAlias());
                    return userRepository.save(new User(resultAttempt.getAlias()));
                });

        boolean isCorrect = resultAttempt.getGuess() == resultAttempt.getFactorA() * resultAttempt.getFactorB();
        ChallengeAttempt challengeAttempt = new ChallengeAttempt(
                null,
                user,
                resultAttempt.getFactorA(),
                resultAttempt.getFactorB(),
                resultAttempt.getGuess(),
                isCorrect
        );
        ChallengeAttempt storeAttempt = challengeAttemptRepository.save(challengeAttempt);

        // Sends the attempt to gamification and prints the response
        HttpStatus status = gameClient.sendAttempt(storeAttempt);

        log.info("Gamification service response: {}", status);
        return storeAttempt;
    }

    @Override
    public List<ChallengeAttempt> getStatsForUser(final String alias) {
        return challengeAttemptRepository.findTop10ByUserAliasOrderByIdDesc(alias);
    }
}
