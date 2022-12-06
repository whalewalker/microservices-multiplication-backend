package com.multiplication.serviceclients;

import com.multiplication.challenge.data.dto.ChallengeSolvedDto;
import com.multiplication.challenge.data.model.ChallengeAttempt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Slf4j
@Service
public class GamificationServiceClient {
    private final RestTemplate restTemplate;
    private final String gamificationHostUrl;

    public GamificationServiceClient(final RestTemplateBuilder builder,
                                     @Value("${service.gamification.host}") final String gamificationHostUrl) {
        restTemplate = builder.build();
        this.gamificationHostUrl = gamificationHostUrl;
    }

    public HttpStatus sendAttempt(final ChallengeAttempt attempt){
        try{
            ChallengeSolvedDto dto = new ChallengeSolvedDto(
                    attempt.getId(),
                    attempt.isCorrect(),
                    attempt.getFactorA(),
                    attempt.getFactorB(),
                    attempt.getUser().getId(),
                    attempt.getUser().getAlias());

            ResponseEntity<String> response = restTemplate.postForEntity(
                    format("%s/attempts", gamificationHostUrl), dto, String.class);

            log.info("Gamification service response {}", response.getStatusCode());

            return response.getStatusCode();
        }catch (Exception e){
            log.error("There was a problem sending the attempt.", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
