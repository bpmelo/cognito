package br.com.melo.bruno.cognito.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    @LocalServerPort
    private int _PORT;

    private String _SCHEMA = "http";
    private String _URL_ADDRESS = "localhost";
    private String _PATH_API = "/api";

    protected URI getServerUri(String _path) {
        UriComponents _uri = UriComponentsBuilder.newInstance()
                .scheme(_SCHEMA)
                .host(_URL_ADDRESS)
                .port(_PORT)
                .path(_PATH_API)
                .path(_path)
                .build();
        return (_uri.toUri());
    }

}
