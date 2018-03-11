package com.microservices.multiplication.service;

import com.microservices.multiplication.entity.Multiplication;
import com.microservices.multiplication.entity.MultiplicationResultAttempt;
import com.microservices.multiplication.entity.User;
import com.microservices.multiplication.event.EventDispatcher;
import com.microservices.multiplication.event.MultiplicationSolvedEvent;
import com.microservices.multiplication.repository.MultiplicationRepository;
import com.microservices.multiplication.repository.MultiplicationResultAttemptRepository;
import com.microservices.multiplication.repository.UserRepository;
import com.microservices.multiplication.service.MultiplicationService;
import com.microservices.multiplication.service.MultiplicationServiceImpl;
import com.microservices.multiplication.service.RandomGeneratorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static  org.assertj.core.api.Assertions.*;

/**
 * Created by mihir.desai on 2/28/2018.
 */
@RunWith(SpringRunner.class)
public class MultiplicationServiceImplTest {
    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService,attemptRepository
                ,userRepository,multiplicationRepository,eventDispatcher);
    }

    @Test
    public void createRandomMultiplicationTest() {
        // given (our mocked random generator service will return 50 and then 30
        given(randomGeneratorService.generateRandomFactor()).willReturn(50,30);

        //when
        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        //assert
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
        assertThat(multiplication.getResult()).isEqualTo(1500);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50,60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3000,false);

        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user,multiplication,3000,true);

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
        given(multiplicationRepository.findByFactorAAndFactorB(50,60)).willReturn(Optional.empty());
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(),user.getId(),true);

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50,60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3001,false);
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(),user.getId(),false);

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
        given(multiplicationRepository.findByFactorAAndFactorB(50,60)).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void retrieveStatsForUserTest() {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50,60);
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user,multiplication,3010,false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user,multiplication,3052,false);

        List<MultiplicationResultAttempt> attempts = Arrays.asList(attempt1,attempt2);
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe"))
                .willReturn(attempts);

        //when
        List<MultiplicationResultAttempt> verifiedAttempts = multiplicationServiceImpl.getStatsForUser("john_doe");

        //then
        assertThat(attempts).isEqualTo(verifiedAttempts);

    }

    @Test
    public void retrieveResultByIdTest() {
        //given
        Long attemptId = 1L;
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(new User("john_doe"),new Multiplication(50,60),3000,true);
        Optional<MultiplicationResultAttempt> expected = Optional.of(multiplicationResultAttempt);
        given(attemptRepository.findById(attemptId)).willReturn(expected);

        //when
        MultiplicationResultAttempt result = multiplicationServiceImpl.getResultById(attemptId);

        //then
        assertThat(result).isEqualTo(multiplicationResultAttempt);
    }
}
