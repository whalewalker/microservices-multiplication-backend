package com.multiplication.challenge.web.controller;

import com.multiplication.challenge.data.dto.ChallengeAttemptDto;
import com.multiplication.challenge.data.model.ChallengeAttempt;
import com.multiplication.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attempts")
@RequiredArgsConstructor
public class ChallengeAttemptController {

    private final ChallengeService challengeService;

    @PostMapping
    ResponseEntity<ChallengeAttempt> postResult(@RequestBody @Valid ChallengeAttemptDto challengeAttemptDTO) {
        return ResponseEntity.ok(challengeService.verifyAttempt(challengeAttemptDTO));
    }

    @GetMapping
    ResponseEntity<List<ChallengeAttempt>> getLastAttempts(@RequestParam("alias") String alias){
        return ResponseEntity.ok(challengeService.getStatsForUser(alias));
    }

}
