package org.nmb.versions.nmbkeycloak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.components.TokenRetriever;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.tokens.TokenRefreshDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoginService {

    private final TokenRetriever tokenRetriever;

    public ResponseEntity<Object> login(LoginDto request) {
        log.info("Start to get access token");
        GoodAuthToken goodAuthToken = tokenRetriever.getAccessToken(request);
        return ResponseEntity.ok().body(ApiResponse.success(goodAuthToken,"Success"));
    }

    public ApiResponse refreshToken(TokenRefreshDto refreshRequestDto) {
        log.info("Start to refresh access token");
        GoodAuthToken goodAuthToken = tokenRetriever.getTokenByRefresh(refreshRequestDto.getRefreshToken());
        return ApiResponse.success(goodAuthToken,"Success");
    }


}
