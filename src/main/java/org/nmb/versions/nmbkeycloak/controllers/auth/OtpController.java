package org.nmb.versions.nmbkeycloak.controllers.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.tokens.TokenRefreshDto;
import org.nmb.versions.nmbkeycloak.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class OtpController {

    private final LoginService loginService;

    @PostMapping(value = "/request")
    public ResponseEntity<Object> requestOtp(@RequestBody TokenRefreshDto refreshRequestDto) {
        log.info("refreshing auth token... ");
        ApiResponse<?> apiResponse = loginService.refreshToken(refreshRequestDto);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<Object> verifyOtp(@RequestBody TokenRefreshDto refreshRequestDto) {
        log.info("refreshing auth token... ");
        ApiResponse<?> apiResponse = loginService.refreshToken(refreshRequestDto);
        return ResponseEntity.ok().body(apiResponse);
    }

}
