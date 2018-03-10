package com.microservices.gamification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.gamification.entity.Badge;
import com.microservices.gamification.entity.GameStats;
import com.microservices.gamification.service.GameService;
import com.microservices.gamification.util.MockMvcUtil;
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

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mihir.desai on 3/8/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserStatsController.class)
public class UserStatsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameService gameService;

    private JacksonTester<GameStats> jsonGameStatsResponse;

    @Before
    public void setup() {
        JacksonTester.initFields(this,new ObjectMapper());
    }

    @Test
    public void retrieveGameStatsForUserTest() throws Exception {
        //given
        Long userId = 1L;
        int totalScore = 3000;
        List<Badge> badges = Arrays.asList(Badge.FIRST_WON, Badge.BRONZE_MULTIPLICATOR, Badge.SILVER_MULTIPLICATOR,Badge.GOLD_MULTIPLICATOR);
        GameStats gameStats = new GameStats(userId,3000, badges);
        given(gameService.retrieveStatsForUser(userId)).willReturn(gameStats);

        //when
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("userId",String.valueOf(userId));
        MockHttpServletResponse response = MockMvcUtil.performGet(mvc,"/stats",multiValueMap);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonGameStatsResponse.write(gameStats).getJson());
    }
}
