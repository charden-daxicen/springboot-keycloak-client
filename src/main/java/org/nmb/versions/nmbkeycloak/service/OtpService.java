package org.nmb.versions.nmbkeycloak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.components.KeycloakAdapter;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.otp.OtpDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class OtpService {

    private final KeycloakAdapter keycloakAdapter;

    public ApiResponse<Object> requestOtp(OtpDto.Inquiry otpRequest) {
        log.info("requesting keycloak otp");
        ApiResponse<Object> apiResponse = keycloakAdapter.requestOtp(otpRequest.getIdentifier());
        if(apiResponse.hasFailed()){
            return  ApiResponse.failure("Sorry, Request failed");
        }
        return ApiResponse.success("A ");
    }

    public ApiResponse<GoodAuthToken> verifyOtp(OtpDto.Verification verificationRequest) {
        log.info("Start to refresh access token");
        ApiResponse<GoodAuthToken> apiResponse = keycloakAdapter.verifyOtp(verificationRequest.getIdentifier(), verificationRequest.getOtp());
        if(apiResponse.hasFailed()){
            return ApiResponse.failure("Sorry, wrong token");
        }
        return apiResponse;
    }


}
