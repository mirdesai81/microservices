package com.microservices.gamification.service;

import com.microservices.gamification.entity.LeaderBoardRow;

import java.util.List;

/**
 * Created by mihir.desai on 3/7/2018.
 * Provides methods to access LeaderBoard with users and scores
 */
public interface LeaderBoardService {

    /**
     * Retrieves the current leader board with top score users
     * @return users with the highest scores
     */
    List<LeaderBoardRow> getCurrentLeaderBoard();
}
