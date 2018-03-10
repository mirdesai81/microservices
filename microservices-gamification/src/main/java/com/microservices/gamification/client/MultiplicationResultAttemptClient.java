package com.microservices.gamification.client;

import com.microservices.gamification.client.dto.MultiplicationResultAttempt;

/**
 * Created by mihir.desai on 3/9/2018.
 * This interface allows us to connect to Multiplication microservice.
 * Note that it's agnostic to the way of communication
 */
public interface MultiplicationResultAttemptClient {
    MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long multiplicationAttemptId);
}
