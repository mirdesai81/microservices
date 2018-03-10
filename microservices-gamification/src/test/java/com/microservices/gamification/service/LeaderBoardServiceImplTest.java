package com.microservices.gamification.service;

import com.microservices.gamification.entity.LeaderBoardRow;
import com.microservices.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by mihir.desai on 3/8/2018.
 */
public class LeaderBoardServiceImplTest {

    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void retrieveCurrentLeaderBoardTest() {
        //given
        Long userId = 1L;
        Long totalScore = 1000L;
        List<LeaderBoardRow> expectedLeaderBoardRows = generateNLeaderBoard(10,totalScore);
        given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoardRows);

        //when
        List<LeaderBoardRow> leaderBoardRows = leaderBoardService.getCurrentLeaderBoard();
        //then
        assertThat(leaderBoardRows).isEqualTo(expectedLeaderBoardRows);
    }

    private List<LeaderBoardRow> generateNLeaderBoard(int n, Long totalScore) {
        return IntStream.range(0,n).mapToObj(i -> new LeaderBoardRow((long)i,totalScore)).collect(Collectors.toList());
    }
}
