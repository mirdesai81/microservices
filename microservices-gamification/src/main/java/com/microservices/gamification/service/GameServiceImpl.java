package com.microservices.gamification.service;

import com.microservices.gamification.client.MultiplicationResultAttemptClient;
import com.microservices.gamification.client.dto.MultiplicationResultAttempt;
import com.microservices.gamification.entity.Badge;
import com.microservices.gamification.entity.BadgeCard;
import com.microservices.gamification.entity.GameStats;
import com.microservices.gamification.entity.ScoreCard;
import com.microservices.gamification.repository.BadgeCardRepository;
import com.microservices.gamification.repository.ScoreCardRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by mihir.desai on 3/7/2018.
 */
@Service
public class GameServiceImpl implements GameService {
    private ScoreCardRepository scoreCardRepository;
    private BadgeCardRepository badgeCardRepository;
    private MultiplicationResultAttemptClient attemptClient;
    private static final int LUCKY_NUMBER = 42;
    private static final Logger logger = LogManager.getLogger(GameServiceImpl.class);

    public GameServiceImpl(ScoreCardRepository scoreCardRepository,
                           BadgeCardRepository badgeCardRepository,
                           MultiplicationResultAttemptClient attemptClient) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
        this.attemptClient = attemptClient;
    }

    @Override
    public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {

        if(correct) {
            ScoreCard scoreCard = new ScoreCard(userId,attemptId);
            scoreCardRepository.save(scoreCard);
            logger.debug("User with id {} scored {} points for attempt id {}",
            userId, scoreCard.getScore(), attemptId);

            List<BadgeCard> badgeCards = processForBadges(userId,attemptId);
            return new GameStats(userId,scoreCard.getScore(),
                    badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));

        }

        return GameStats.emptyStats(userId);
    }


    @Override
    public GameStats retrieveStatsForUser(Long userId) {
        int score = scoreCardRepository.getTotalScoreForUser(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

        return new GameStats(userId,score,
                badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
    }

    private List<BadgeCard> processForBadges(Long userId,Long attemptId) {
        List<BadgeCard> badgeCardList = new ArrayList<>();

        int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
        logger.debug("New score for user {} is {}",userId,totalScore);

        List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);

        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

        // Badges depending on score
        //checkAndGiveBadgeBasedOnScore(badgeCards,Badge.FIRST_WON,totalScore,10,userId).ifPresent(badgeCardList::add);

        checkAndGiveBadgeBasedOnScore(badgeCards,Badge.BRONZE_MULTIPLICATOR,totalScore,100,userId).ifPresent(badgeCardList::add);

        checkAndGiveBadgeBasedOnScore(badgeCards,Badge.SILVER_MULTIPLICATOR,totalScore,500,userId).ifPresent(badgeCardList::add);

        checkAndGiveBadgeBasedOnScore(badgeCards,Badge.GOLD_MULTIPLICATOR,totalScore,999,userId).ifPresent(badgeCardList::add);


        if(scoreCardList.size() == 1 &&
                !containsBadge(badgeCards, Badge.FIRST_WON)) {
            BadgeCard badgeCard = assignBadgeToUser(Badge.FIRST_WON,userId);
            badgeCardList.add(badgeCard);
        }

        MultiplicationResultAttempt resultAttempt = attemptClient.retrieveMultiplicationResultAttemptById(attemptId);
        if(!containsBadge(badgeCards,Badge.LUCKY_NUMBER) && (LUCKY_NUMBER == resultAttempt.getMultiplicationFactorA()
                || LUCKY_NUMBER == resultAttempt.getMultiplicationFactorB())) {
            BadgeCard badgeCard = assignBadgeToUser(Badge.LUCKY_NUMBER,userId);
            badgeCardList.add(badgeCard);
        }


        return badgeCardList;
    }

    /**
     * Method to check the current score against the different threshold
     * to gain badges.
     * It also assigns badge to user if the conditions are met.
     * @param badgeCards
     * @param badge
     * @param score
     * @param scoreThreshold
     * @param userId
     * @return Optional newly created badge
     */
    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCards, final Badge badge,
                                                              final int score, final int scoreThreshold,final Long userId) {
        if(score > scoreThreshold && !containsBadge(badgeCards,badge)) {
            return Optional.of(assignBadgeToUser(badge,userId));
        }

        return Optional.empty();
    }

    /**
     * Assigns a new {@link BadgeCard} to the user
     * @param badge
     * @param userId
     * @return newly created BadgeCard
     */
    private BadgeCard assignBadgeToUser(Badge badge,Long userId) {
        BadgeCard card = new BadgeCard(userId,badge);
        badgeCardRepository.save(card);
        logger.debug("User with id {} won a new badge {}",userId,badge);
        return card;
    }

    /**
     * Checks if a list of {@link BadgeCard}s contains a {@link BadgeCard}
     * @param badgeCardList
     * @param badge
     * @return
     */
    private boolean containsBadge(List<BadgeCard> badgeCardList,Badge badge) {
        return badgeCardList.stream().anyMatch(b -> b.getBadge().equals(badge));
    }
}
