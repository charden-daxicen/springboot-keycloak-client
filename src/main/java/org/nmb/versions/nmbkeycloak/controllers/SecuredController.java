package org.nmb.versions.nmbkeycloak.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class SecuredController {


    @PostMapping(value = "/service/secret", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> enableOnlinePurchase() {
        log.info("access to protected endpoint");
        return new ResponseEntity<>("In the building", HttpStatus.OK);
    }

}
