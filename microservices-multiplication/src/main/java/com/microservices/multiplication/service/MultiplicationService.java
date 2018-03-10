package com.microservices.multiplication.service;

import com.microservices.multiplication.entity.Multiplication;
import com.microservices.multiplication.entity.MultiplicationResultAttempt;

import java.util.List;

/**
 * Created by mihir.desai on 2/28/2018.
 */
public interface MultiplicationService {

    /** Generates a random {@link Multiplication} object.
     *
     * @return a multiplication of randomly generated number
     */
    Multiplication createRandomMultiplication();

    /**
     * Checks the result for {@link MultiplicationResultAttempt}
     * @param multiplicationResultAttempt
     * @return  true if attempt matches the result of
     *          multiplication, false otherwise.
     */
    boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt);

    /**
     * Returns top 5 {@link MultiplicationResultAttempt} for a given user
     * @param userAlias
     * @return list of top 5 multiplication result attempt for a given user
     */
    List<MultiplicationResultAttempt> getStatsForUser(String userAlias);

    MultiplicationResultAttempt getResultById(final Long resultId);
}
