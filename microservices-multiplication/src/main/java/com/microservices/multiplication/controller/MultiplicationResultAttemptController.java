package com.microservices.multiplication.controller;

import com.microservices.multiplication.entity.MultiplicationResultAttempt;
import com.microservices.multiplication.service.MultiplicationService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mihir.desai on 3/6/2018.
 */
@RestController
@RequestMapping("/results")
public class MultiplicationResultAttemptController {

    private  MultiplicationService multiplicationService;

    @Autowired
    MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
        boolean isCorrect = multiplicationService.checkAttempt(multiplicationResultAttempt);
        MultiplicationResultAttempt attemptCopy = new MultiplicationResultAttempt
                (multiplicationResultAttempt.getUser(),multiplicationResultAttempt.getMultiplication(),
                        multiplicationResultAttempt.getResultAttempt(),isCorrect);
        return ResponseEntity.ok(attemptCopy);
    }


    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
    }

    @GetMapping("/{resultId}")
    ResponseEntity<MultiplicationResultAttempt> getResultById(@PathVariable("resultId") final Long resultId) {
        return ResponseEntity.ok(multiplicationService.getResultById(resultId));
    }

    @RequiredArgsConstructor
    @NoArgsConstructor(force = true)
    @Getter
    public static class ResultResponse {
        private final boolean correct;
    }
}
