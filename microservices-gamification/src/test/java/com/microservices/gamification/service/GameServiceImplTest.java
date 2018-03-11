package com.microservices.gamification.service;

import com.microservices.gamification.client.MultiplicationResultAttemptClient;
import com.microservices.gamification.client.dto.MultiplicationResultAttempt;
import com.microservices.gamification.entity.Badge;
import com.microservices.gamification.entity.BadgeCard;
import com.microservices.gamification.entity.GameStats;
import com.microservices.gamification.entity.ScoreCard;
import com.microservices.gamification.repository.BadgeCardRepository;
import com.microservices.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by mihir.desai on 3/7/2018.
 */

public class GameServiceImplTest {

    private GameServiceImpl gameService;

    @Mock
    private BadgeCardRepository badgeCardRepository;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Mock
    private MultiplicationResultAttemptClient attemptClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(scoreCardRepository,badgeCardRepository,attemptClient);
    }

    @Test
    public void processFirstCorrectAttemptTest() {
        //given
        Long userId = 1L;
        Long attemptId = 1L;
        boolean correct = true;
        int totalScore = 10;
        ScoreCard scoreCard = new ScoreCard(userId,attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(Collections.emptyList());
        // the attempt
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 10, 10, 100, true);
        given(attemptClient.retrieveMultiplicationResultAttemptById(attemptId))
                .willReturn(attempt);

        //when
        GameStats stats = gameService.newAttemptForUser(userId,attemptId,true);

        //then
        assertThat(stats.getScore()).isEqualTo(scoreCard.getScore());
        assertThat(stats.getBadges()).containsOnly(Badge.FIRST_WON);
    }

    @Test
    public void processCorrectAttemptForBronzeBadgeTest() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        int totalScore = 101;
        BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(10, userId));
        // the first won badge is already there
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 10, 10, 100, true);
        given(attemptClient.retrieveMultiplicationResultAttemptById(attemptId))
                .willReturn(attempt);

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // assert - should score one card and win the badge BRONZE
        assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(iteration.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
    }

    @Test
    public void processCorrectAttemptForGoldBadgeTest() {
        //given
        Long userId = 1L;
        Long attemptId = 1L;
        int totalScore = 1000;
        BadgeCard firstBadge = new BadgeCard(userId,Badge.FIRST_WON);

        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);

        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(createNScoreCards(10,userId));

        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(Collections.singletonList(firstBadge));
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 10, 10, 100, true);
        given(attemptClient.retrieveMultiplicationResultAttemptById(attemptId))
                .willReturn(attempt);

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // assert - should score one card and win the badge BRONZE
        assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(iteration.getBadges()).contains(Badge.GOLD_MULTIPLICATOR);
    }

    @Test
    public void processCorrectAttemptForLuckyNumberBadgeTest() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        int totalScore = 10;
        BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(1, userId));
        // the first won badge is already there
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));
        // the attempt includes the lucky number
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 42, 10, 420, true);
        given(attemptClient.retrieveMultiplicationResultAttemptById(attemptId))
                .willReturn(attempt);

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // assert - should score one card and win the badge LUCKY NUMBER
        assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(iteration.getBadges()).containsOnly(Badge.LUCKY_NUMBER);
    }

    @Test
    public void processWrongAttemptTest() {
        // given
        Long userId = 1L;
        Long attemptId = 8L;
        int totalScore = 10;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.emptyList());
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 10, 10, 101, false);
        given(attemptClient.retrieveMultiplicationResultAttemptById(attemptId))
                .willReturn(attempt);

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, false);

        // assert - shouldn't score anything
        assertThat(iteration.getScore()).isEqualTo(0);
        assertThat(iteration.getBadges()).isEmpty();
    }

    @Test
    public void retrieveStatsForUserTest() {
        // given
        Long userId = 1L;
        int totalScore = 1000;
        BadgeCard badgeCard = new BadgeCard(userId, Badge.SILVER_MULTIPLICATOR);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(badgeCard));

        // when
        GameStats stats = gameService.retrieveStatsForUser(userId);

        // assert - should score one card and win the badge FIRST_WON
        assertThat(stats.getScore()).isEqualTo(totalScore);
        assertThat(stats.getBadges()).containsOnly(Badge.SILVER_MULTIPLICATOR);
    }

    private List<ScoreCard> createNScoreCards(int n, Long userId) {
        return IntStream.range(0, n)
                .mapToObj(i -> new ScoreCard(userId, (long)i))
                .collect(Collectors.toList());
    }
}
