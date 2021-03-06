package com.microservices.multiplication.util;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * Created by mihir.desai on 3/6/2018.
 */
public class MockMvcUtil {

    public static MockHttpServletResponse performGet(MockMvc mvc, String url) throws Exception {
        return mvc.perform(get(url).accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andReturn().getResponse();
    }

    public static MockHttpServletResponse performPost(MockMvc mvc,String url,String postJson) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(postJson))
                .andDo(print()).andReturn().getResponse();
    }

    public static MockHttpServletResponse performGet(MockMvc mvc, String url,MultiValueMap<String,String> params) throws Exception {
        return mvc.perform(get(url).params(params).accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andReturn().getResponse();
    }
}
