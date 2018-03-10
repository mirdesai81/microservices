package com.microservices.gamification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.gamification.entity.LeaderBoardRow;
import com.microservices.gamification.service.LeaderBoardService;
import com.microservices.gamification.util.MockMvcUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by mihir.desai on 3/8/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(LeaderBoardController.class)
public class LeaderBoardControllerTest {

    @MockBean
    private LeaderBoardService leaderBoardService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<List<LeaderBoardRow>> jsonLeaderBoardList;

    @Before
    public void setup() {
        JacksonTester.initFields(this,new ObjectMapper());
    }

    @Test
    public void retrieveLeaderBoardTest() throws Exception {
        //given
        List<LeaderBoardRow> expected = createNLeaderBoard(10);
        given(leaderBoardService.getCurrentLeaderBoard()).willReturn(expected);

        //when
        MockHttpServletResponse response = MockMvcUtil.performGet(mvc,"/leaders");

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonLeaderBoardList.write(expected).getJson());
    }

    private List<LeaderBoardRow> createNLeaderBoard(int n) {
        return IntStream.rangeClosed(1,n).mapToObj(i -> new LeaderBoardRow((long) i,(long)(i * 1000))).collect(Collectors.toList());
    }

}
