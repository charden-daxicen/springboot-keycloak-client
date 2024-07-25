package org.nmb.versions.nmbkeycloak.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> hom1e() {
        return ResponseEntity.ok().body("Can View Public");
    }

    @GetMapping(value = "/public",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> home() {
        return ResponseEntity.ok().body("Can View Public");
    }

    @GetMapping(value = "/admin",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> homeAdmin() {
        return ResponseEntity.ok().body("Can View Admi");
    }

    @GetMapping(value = "/error/access-denied",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> error() {
        return ResponseEntity.ok().body("Error Page");
    }


}
