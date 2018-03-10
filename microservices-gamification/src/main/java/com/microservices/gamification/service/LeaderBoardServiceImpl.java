package com.microservices.gamification.service;

import com.microservices.gamification.entity.LeaderBoardRow;
import com.microservices.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mihir.desai on 3/7/2018.
 */
@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {
    private ScoreCardRepository scoreCardRepository;

    LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
    }

    @Override
    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        return scoreCardRepository.findFirst10();
    }
}
