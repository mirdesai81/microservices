package com.microservices.gamification.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Created by mihir.desai on 3/7/2018.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class LeaderBoardRow {

    private final Long userId;
    private final Long totalScore;

    public LeaderBoardRow() {
        this(0L, 0L);
    }
}
