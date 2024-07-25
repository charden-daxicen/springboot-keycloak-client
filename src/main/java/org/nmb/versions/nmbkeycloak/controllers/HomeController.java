package org.nmb.versions.nmbkeycloak.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class HomeController {

    @GetMapping(value = "/public",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> home() {
        return ResponseEntity.ok().body("This is a Public opinion");
    }

    @PreAuthorize("hasAuthority('crook')")
    @GetMapping(value = "/admin",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> homeAdmin() {
        return ResponseEntity.ok().body("This is admin speaking");
    }

    @GetMapping(value = "/error",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> error() {
        return ResponseEntity.ok().body("Error Page");
    }


}
