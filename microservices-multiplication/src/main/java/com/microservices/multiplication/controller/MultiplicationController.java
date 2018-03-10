package com.microservices.multiplication.controller;

import com.microservices.multiplication.entity.Multiplication;
import com.microservices.multiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mihir.desai on 3/5/2018.
 */
@RestController
@RequestMapping("/multiplications")
public class MultiplicationController {

    private MultiplicationService multiplicationService;

    @Autowired
    public MultiplicationController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @GetMapping("/random")
    Multiplication getRandomMultiplication() {
        return multiplicationService.createRandomMultiplication();
    }

}
