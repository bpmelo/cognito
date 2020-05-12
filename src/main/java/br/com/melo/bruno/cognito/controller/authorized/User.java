package br.com.melo.bruno.cognito.controller.authorized;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authorized")
public class User {

    @GetMapping(
            path = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity getUser() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
