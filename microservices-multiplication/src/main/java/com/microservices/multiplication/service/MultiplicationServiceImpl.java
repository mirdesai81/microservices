package com.microservices.multiplication.service;

import com.microservices.multiplication.entity.Multiplication;
import com.microservices.multiplication.entity.MultiplicationResultAttempt;
import com.microservices.multiplication.entity.User;
import com.microservices.multiplication.event.EventDispatcher;
import com.microservices.multiplication.event.MultiplicationSolvedEvent;
import com.microservices.multiplication.repository.MultiplicationRepository;
import com.microservices.multiplication.repository.MultiplicationResultAttemptRepository;
import com.microservices.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * Created by mihir.desai on 2/28/2018.
 */
@Service
public class MultiplicationServiceImpl implements MultiplicationService {
    private RandomGeneratorService randomGeneratorService;
    private UserRepository userRepository;
    private MultiplicationResultAttemptRepository attemptRepository;
    private MultiplicationRepository multiplicationRepository;
    private EventDispatcher eventDispatcher;


    @Autowired
    public MultiplicationServiceImpl(final
                                     RandomGeneratorService randomGeneratorService,
                                     final MultiplicationResultAttemptRepository
                                     attemptRepository,
                                     final UserRepository userRepository,
                                     final MultiplicationRepository multiplicationRepository,
                                     final EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();

        return new Multiplication(factorA,factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(MultiplicationResultAttempt attempt) {
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

        Optional<Multiplication> multiplication = multiplicationRepository.findByFactorAAndFactorB(attempt.getMultiplication().getFactorA(),attempt.getMultiplication().getFactorB());
        Assert.isTrue(!attempt.isCorrect(),"You can't send an attempt marked as correct!!");

        boolean correct = attempt.getResultAttempt() == attempt.getMultiplication().getFactorA()
                * attempt.getMultiplication().getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(user.orElse(attempt.getUser()),multiplication.orElse(attempt.getMultiplication())
                ,attempt.getResultAttempt(),correct);

        attemptRepository.save(checkedAttempt);
        eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId()
                ,checkedAttempt.getUser().getId()
                ,checkedAttempt.isCorrect()));

        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

    @Override
    public MultiplicationResultAttempt getResultById(Long resultId) {
        return attemptRepository.findOne(resultId);
    }
}
