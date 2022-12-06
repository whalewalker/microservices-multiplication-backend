package com.multiplication.challenge;

import com.multiplication.challenge.data.dto.ChallengeAttemptDto;
import com.multiplication.challenge.data.model.ChallengeAttempt;
import com.multiplication.challenge.service.ChallengeService;
import com.multiplication.challenge.web.controller.ChallengeAttemptController;
import com.multiplication.user.data.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(ChallengeAttemptController.class)
class ChallengeAttemptControllerTest {

    @MockBean
    @Qualifier("challengeServiceImpl")
    private ChallengeService challengeService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<ChallengeAttemptDto> jsonRequestAttempt;

    @Autowired
    private JacksonTester<ChallengeAttempt> jsonResultAttempt;

    @Autowired
    private JacksonTester<List<ChallengeAttempt>> jsonResultAttempts;

    private User user;
    ChallengeAttemptDto attemptDTO;
    ChallengeAttempt expectedResponse;

    @BeforeEach
    void setUp() {
        user = new User(1L, "john_doe");
        attemptDTO = new ChallengeAttemptDto(50, 70, 3500, "john_doe");
        expectedResponse = new ChallengeAttempt(1L, user, 50, 70, 3500, true);
    }

    @AfterEach
    void tearDown() {
        user = null;
        attemptDTO = null;
        expectedResponse = null;
    }

    @Test
    void postValidResult() throws Exception {
        given(challengeService.verifyAttempt(eq(attemptDTO)))
                .willReturn(expectedResponse);

        MockHttpServletResponse response = mockMvc.perform(
                post("/attempts").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAttempt.write(attemptDTO).getJson())
        ).andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResultAttempt.write(
                expectedResponse).getJson());
    }

    @Test
    void postInvalidResult() throws Exception {
        ChallengeAttemptDto attemptDTO = new ChallengeAttemptDto(2000, -70, 1,"john_doe");
        MockHttpServletResponse response = mockMvc.perform(
                        post("/attempts").contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestAttempt.write(attemptDTO).getJson()))
                .andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getUserStats() throws Exception {
        List<ChallengeAttempt> attempts = new ArrayList<>(List.of(expectedResponse));
        given(challengeService.getStatsForUser(eq("john_doe")))
                .willReturn(attempts);

        MockHttpServletResponse response = mockMvc.perform(
                get(format("/attempts?alias=%s", "john_doe"))
        ).andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResultAttempts.write(attempts).getJson());
    }


}