package com.microservices.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.multiplication.util.MockMvcUtil;
import com.microservices.multiplication.entity.Multiplication;
import com.microservices.multiplication.entity.MultiplicationResultAttempt;
import com.microservices.multiplication.entity.User;
import com.microservices.multiplication.service.MultiplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
/**
 * Created by mihir.desai on 3/6/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResult;

    private JacksonTester<MultiplicationResultAttempt> jsonResponse;

    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @Before
    public void setup() {
        JacksonTester.initFields(this,new ObjectMapper());
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParameterizedTest(true);
    }

    @Test
    public void postResultReturnNotCorrect() throws Exception {
        genericParameterizedTest(false);
    }

    private void genericParameterizedTest(boolean correct) throws Exception {

        //given
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
                .willReturn(correct);
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50,70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3500,correct);

        //when
        MockHttpServletResponse response = MockMvcUtil.performPost(mvc,"/results",
                jsonResult.write(attempt).getJson());

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResponse.write(new MultiplicationResultAttempt(attempt.getUser(),
                attempt.getMultiplication(),attempt.getResultAttempt(),correct)).getJson());
    }

    @Test
    public void getUserStats() throws Exception {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50,70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3500,true);

        List<MultiplicationResultAttempt> attempts = Arrays.asList(attempt,attempt);
        given(multiplicationService.getStatsForUser("john_doe")).willReturn(attempts);
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("alias","john_doe");

        //when
        MockHttpServletResponse response = MockMvcUtil.performGet(mvc,"/results",multiValueMap);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResultAttemptList.write(attempts).getJson());


    }

    @Test
    public void getResultByIdTest() throws Exception {
        //given
        Long attemptId = 1L;
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50,70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3500,true);

        given(multiplicationService.getResultById(attemptId)).willReturn(attempt);
        //when
        MockHttpServletResponse response = MockMvcUtil.performGet(mvc,"/results/"+attemptId);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResponse.write(attempt).getJson());
    }
}
