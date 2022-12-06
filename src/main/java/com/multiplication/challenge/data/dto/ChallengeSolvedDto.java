package com.multiplication.challenge.data.dto;

import lombok.Value;

@Value
public class ChallengeSolvedDto {
    long attemptId;
    boolean correct;
    int factorA;
    int factorB;
    long userId;
    String userAlias;
}
