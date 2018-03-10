package com.microservices.gamification.repository;

import com.microservices.gamification.entity.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mihir.desai on 3/7/2018.
 */
public interface BadgeCardRepository extends CrudRepository<BadgeCard,Long> {

    /**
     * Retrieves All BadgeCards for a gvien user.
     * @param userId the id of user to look for BadgeCard
     * @return the list of BadgeCards, sorted by most recent.
     */
    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
