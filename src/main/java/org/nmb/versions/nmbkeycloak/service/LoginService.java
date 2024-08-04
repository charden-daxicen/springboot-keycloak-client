package org.nmb.versions.nmbkeycloak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.components.KeycloakAdapter;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.tokens.TokenRefreshDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoginService {

    private final KeycloakAdapter keycloakAdapter;

    public ApiResponse<GoodAuthToken> login(LoginDto request) {
        log.info("fetch get access token");
        GoodAuthToken goodAuthToken = keycloakAdapter.getAccessToken(request);
        return ApiResponse.success(goodAuthToken, "Success");
    }

    public ApiResponse<GoodAuthToken> refreshToken(TokenRefreshDto refreshRequestDto) {
        log.info("refresh access token");
        GoodAuthToken goodAuthToken = keycloakAdapter.getAccessTokenByRefresh(refreshRequestDto.getRefreshToken());
        return ApiResponse.success(goodAuthToken, "Success");
    }


}
