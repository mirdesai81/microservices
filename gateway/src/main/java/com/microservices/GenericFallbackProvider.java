package com.microservices;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mihir.desai on 3/15/2018.
 */
public class GenericFallbackProvider implements FallbackProvider {
    private String route = "*";
    private HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
    private String statusText = "Service Unavailable";
    private int rawStatusCode = 503;
    private HttpHeaders headers = null;
    private String responseBody = "{\"message\":\"Service Unavailable. Please try after sometime\"}";

    @Override
    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public void setRawStatusCode(int rawStatusCode) {
        this.rawStatusCode = rawStatusCode;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public ClientHttpResponse fallbackResponse(String s, Throwable throwable) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                if(status == null) {
                    status = HttpStatus.SERVICE_UNAVAILABLE;
                }
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return rawStatusCode;
            }

            @Override
            public String getStatusText() throws IOException {
                if(statusText == null) {
                    statusText = "Service Unavailable";
                }
                return statusText;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                if (responseBody == null)
                    responseBody ="{\"message\":\"Service Unavailable. Please try after sometime\"}";
                return new ByteArrayInputStream(responseBody.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                if(headers == null) {
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccessControlAllowCredentials(true);
                    headers.setAccessControlAllowOrigin("*");
                }
                return headers;
            }
        };
    }

}
