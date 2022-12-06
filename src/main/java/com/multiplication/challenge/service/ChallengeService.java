package com.multiplication.challenge.service;

import com.multiplication.challenge.data.dto.ChallengeAttemptDto;
import com.multiplication.challenge.data.model.ChallengeAttempt;

import java.util.List;

public interface ChallengeService {
    /**
     * Verifies if an attempt coming from the presentation layer is correct or
     not. *
     * @return the resulting ChallengeAttempt object
     */
    ChallengeAttempt verifyAttempt(ChallengeAttemptDto resultAttempt);

    /**
     * Gets the statistics for a given user.
     *
     * @param alias the user's alias
     * @return a list of the last 10 {@link ChallengeAttempt} * objects created by the user.
     */
    List<ChallengeAttempt> getStatsForUser(String alias);
}
