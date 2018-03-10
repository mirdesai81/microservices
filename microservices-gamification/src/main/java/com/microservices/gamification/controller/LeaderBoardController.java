package com.microservices.gamification.controller;

import com.microservices.gamification.entity.LeaderBoardRow;
import com.microservices.gamification.service.LeaderBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mihir.desai on 3/8/2018.
 */
@RestController
@RequestMapping("/leaders")
public class LeaderBoardController {

    private LeaderBoardService leaderBoardService;

    public LeaderBoardController(LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping
    public List<LeaderBoardRow> getLeaderBoard() {
        return leaderBoardService.getCurrentLeaderBoard();
    }
}
