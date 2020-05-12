package br.com.melo.bruno.cognito.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class Beat {

    @GetMapping(
            path = "/beat",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity getBeat() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
