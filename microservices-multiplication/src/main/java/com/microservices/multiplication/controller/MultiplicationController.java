package com.microservices.multiplication.controller;

import com.microservices.multiplication.entity.Multiplication;
import com.microservices.multiplication.service.MultiplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mihir.desai on 3/5/2018.
 */
@Slf4j
@RestController
@RequestMapping("/multiplications")
public class MultiplicationController {

    private MultiplicationService multiplicationService;

    private int serverPort;

    @Autowired
    public MultiplicationController(MultiplicationService multiplicationService, @Value("${server.port}") int serverPort) {
        this.multiplicationService = multiplicationService;
        this.serverPort = serverPort;
    }

    @GetMapping("/random")
    Multiplication getRandomMultiplication() {
        log.debug("Generating multiplication from server @ {}",this.serverPort);
        return multiplicationService.createRandomMultiplication();
    }

}
