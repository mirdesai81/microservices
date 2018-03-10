package com.microservices.gamification.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by mihir.desai on 3/7/2018.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class BadgeCard {
    @Id
    @GeneratedValue
    @Column(name = "BADGE_ID")
    private final Long badgeId;

    private final Long userId;
    private final long badgeTimestamp;
    private final Badge badge;

    public BadgeCard() {
        this(null,null,0,null);
    }

    public BadgeCard(final Long userId,final Badge badge) {
        this(null,userId,System.currentTimeMillis(),badge);
    }
}
