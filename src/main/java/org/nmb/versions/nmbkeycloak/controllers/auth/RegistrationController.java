package org.nmb.versions.nmbkeycloak.controllers.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nmb.versions.nmbkeycloak.dto.RegistrationDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody RegistrationDto registrationDto) {
        ApiResponse<?> apiResponse = registrationService.register(registrationDto);
        return ResponseEntity.ok(apiResponse);
    }

}
