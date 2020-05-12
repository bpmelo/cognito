package br.com.melo.bruno.cognito.controller.controller;

import br.com.melo.bruno.cognito.controller.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class Beat extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    final String _API = "/public/beat";

    @Test
    public void testGetBeatNoAuthentication() {
        ResponseEntity<String> _response =
                restTemplate
                        .exchange(
                                getServerUri(_API),
                                HttpMethod.GET,
                                null,
                                String.class
                        );
        Assert.assertEquals(HttpStatus.OK.value(), _response.getStatusCode().value());
    }

}
