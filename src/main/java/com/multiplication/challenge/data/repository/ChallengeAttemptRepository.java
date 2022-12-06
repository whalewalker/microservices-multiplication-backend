package com.multiplication.challenge.data.repository;

import com.multiplication.challenge.data.model.ChallengeAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeAttemptRepository extends JpaRepository<ChallengeAttempt, Long> {
    List<ChallengeAttempt> findTop10ByUserAliasOrderByIdDesc(String userAlias);
}
