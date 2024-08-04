package org.nmb.versions.nmbkeycloak.controllers.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.otp.OtpDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;

import org.nmb.versions.nmbkeycloak.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth")
public class OtpController {

    private final OtpService otpService;

    @PostMapping(value = "/account/lookup")
    public ResponseEntity<Object> lookupAccount(@RequestBody OtpDto.Inquiry otpRequest) {
        log.info("lookup account... ");
        ApiResponse<Object> apiResponse = otpService.requestOtp(otpRequest);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping(value = "/otp/request")
    public ResponseEntity<Object> requestOtp(@RequestBody OtpDto.Inquiry otpRequest) {
        log.info("request otp... ");
        ApiResponse<Object> apiResponse = otpService.requestOtp(otpRequest);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping(value = "/otp/verify")
    public ResponseEntity<Object> verifyOtp(@RequestBody OtpDto.Verification verificationDto) {
        log.info("verifying otp... ");
        ApiResponse<GoodAuthToken> apiResponse = otpService.verifyOtp(verificationDto);
        return ResponseEntity.ok().body(apiResponse);
    }


}
