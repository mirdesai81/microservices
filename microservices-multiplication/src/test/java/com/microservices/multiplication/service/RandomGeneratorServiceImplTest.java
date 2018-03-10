package com.microservices.multiplication.service;

import com.microservices.multiplication.service.RandomGeneratorService;
import com.microservices.multiplication.service.RandomGeneratorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by mihir.desai on 3/5/2018.
 */
@RunWith(SpringRunner.class)
public class RandomGeneratorServiceImplTest {


    private RandomGeneratorServiceImpl randomGeneratorServiceImpl;

    @Before
    public void setup() {
        randomGeneratorServiceImpl = new RandomGeneratorServiceImpl();
    }

    @Test
    public void generateRandomFactorIsBetweenExpectedRange() {
        List<Integer> randomFactors = IntStream.range(0,1000).map(x -> randomGeneratorServiceImpl.generateRandomFactor()).boxed().collect(Collectors.toList());

        assertThat(randomFactors).containsOnlyElementsOf(IntStream.range(11,100).boxed().collect(Collectors.toList()));
    }
}
