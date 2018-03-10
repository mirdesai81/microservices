package com.microservices.gamification.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.mapping.Collection;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mihir.desai on 3/7/2018.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class GameStats {

    private final Long userId;
    private final int score;
    private final List<Badge> badges;

    public GameStats() {
        this(0L,0,new ArrayList<>());
    }

    public static GameStats emptyStats(final Long userId) {
        return new GameStats(userId,0, Collections.emptyList());
    }

    public List<Badge> getBadges() {
        return Collections.unmodifiableList(badges);
    }

}
