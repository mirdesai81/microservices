package com.microservices.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.multiplication.util.MockMvcUtil;
import com.microservices.multiplication.entity.Multiplication;
import com.microservices.multiplication.service.MultiplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * Created by mihir.desai on 3/5/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class MultiplicationControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<Multiplication> json;

    @Before
    public void setup() {
        JacksonTester.initFields(this,new ObjectMapper());
    }

    @Test
    public void getRandomMultiplicationTest() throws Exception {
        //given
        given(multiplicationService.createRandomMultiplication())
                .willReturn(new Multiplication(70,20));

        //when
        MockHttpServletResponse response = MockMvcUtil.performGet(mvc,"/multiplications/random");
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString()).isEqualTo(json.write(new Multiplication(70,20)).getJson());


    }
}
