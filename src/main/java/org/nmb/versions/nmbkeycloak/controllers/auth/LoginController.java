package org.nmb.versions.nmbkeycloak.controllers.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nmb.versions.nmbkeycloak.dto.BaseResponseDto;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
import org.nmb.versions.nmbkeycloak.dto.TokenRefreshDto;
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
public class LoginController {

    private final LoginService loginService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
        return loginService.login(loginDto);
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestBody TokenRefreshDto refreshRequestDto) {
        log.info("refreshing auth token... ");
        BaseResponseDto baseResponseDto = loginService.refreshToken(refreshRequestDto);
        return ResponseEntity.ok().body(baseResponseDto);
    }

}
