package com.microservices.gamification.client;

import com.microservices.gamification.client.dto.MultiplicationResultAttempt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mihir.desai on 3/9/2018.
 */
@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

    private final RestTemplate restTemplate;
    private final String multiplicationHost;
    private static final Logger logger = LogManager.getLogger(MultiplicationResultAttemptClientImpl.class);

    @Autowired
    public MultiplicationResultAttemptClientImpl(final RestTemplate restTemplate, @Value("${multiplicationHost}") final String multiplicationHost) {
        this.restTemplate = restTemplate;
        this.multiplicationHost = multiplicationHost;
    }

    @Override
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(Long multiplicationAttemptId) {
        logger.debug("Calling rest for {}",multiplicationHost);
        return restTemplate.getForObject(multiplicationHost + "/results/" + multiplicationAttemptId
                , MultiplicationResultAttempt.class);
    }
}
