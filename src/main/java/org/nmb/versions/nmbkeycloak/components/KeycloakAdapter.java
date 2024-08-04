package org.nmb.versions.nmbkeycloak.components;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.keycloak.otps.KeycloakOTPDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.nmb.versions.nmbkeycloak.utils.JHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Component
public class KeycloakAdapter {

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    @Value("${keycloak.auth.client-id}")
    private String kcClientId;

    @Value("${keycloak.auth.client-secret}")
    private String kcClientSecret;

    @Value("${keycloak.auth.client-username}")
    private String keycloakClientUsername;

    @Value("${keycloak.auth.client-password}")
    private String keycloakClientPassword;

    @Value("${keycloak.auth.token-url}")
    private String keyCloakGetTokenUrl;

    @Value("${keycloak.user.token-request-url}")
    private String keyCloakOtpRequestUrl;

    @Value("${keycloak.user.token-verification-url}")
    private String keyCloakOtpVerificationUrl;

    private final RestTemplate restTemplate;

    public GoodAuthToken getAccessToken(LoginDto request) {
        MultiValueMap<String, String> requestBody = buildBaseRequest();
        requestBody.add(KEY_USERNAME, request.getUsername());
        requestBody.add(KEY_PASSWORD, request.getPassword());
        return this.fetchAccessToken(requestBody);
    }

    public GoodAuthToken getAdminAccessToken() {
        MultiValueMap<String, String> requestBody = buildBaseRequest();
        requestBody.add(KEY_USERNAME, keycloakClientUsername);
        requestBody.add(KEY_PASSWORD, keycloakClientPassword);
        return this.fetchAccessToken(requestBody);
    }

    public GoodAuthToken fetchAccessToken(MultiValueMap<String, String> requestBody) {

        log.info("fetching access token... {} {}", requestBody, keyCloakGetTokenUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<GoodAuthToken> response = restTemplate.postForEntity(keyCloakGetTokenUrl,
                new HttpEntity<>(requestBody, headers),
                GoodAuthToken.class);

        log.info("access token fetched successful");
        return response.getBody();
    }

    public GoodAuthToken getAccessTokenByRefresh(String refreshToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("client_id", kcClientId);
        requestBody.add("client_secret", kcClientSecret);

        ResponseEntity<GoodAuthToken> response = restTemplate.postForEntity(
                keyCloakGetTokenUrl,
                new HttpEntity<>(requestBody, headers),
                GoodAuthToken.class);

        return response.getBody();
    }

    public ApiResponse<Object> requestOtp(String identifier) {

        KeycloakOTPDto.InquiryRequest otpRequest = KeycloakOTPDto.InquiryRequest
                .builder()
                .identifier(identifier)
                .build();

        return sendAuthenticatedPostRequest(otpRequest, keyCloakOtpRequestUrl, Object.class,"otp request");
    }

    public ApiResponse<GoodAuthToken> verifyOtp(String identifier, String otp) {
        KeycloakOTPDto.VerificationRequest otpRequest = KeycloakOTPDto.VerificationRequest
                .builder()
                .identifier(identifier)
                .otp(otp)
                .build();
        return sendAuthenticatedPostRequest(otpRequest, keyCloakOtpVerificationUrl,GoodAuthToken.class,"otp verification");
    }

    private MultiValueMap<String, String> buildBaseRequest() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
        requestBody.add("client_id", kcClientId);
        requestBody.add("client_secret", kcClientSecret);
        return requestBody;
    }

    private <T> ApiResponse<T> sendAuthenticatedPostRequest(Object requestBody, String url,Class<T> classOfT, String narration) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String requestBodyJson = JHelper.toJson(requestBody);
        log.info("{} request {}", narration, requestBodyJson);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBodyJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("{} response {}", narration, responseEntity.getBody());
        }

        log.error("{} request failed failed with status: {}", narration, responseEntity.getStatusCode());
        return ApiResponse.failure("Request failed");
    }

}
