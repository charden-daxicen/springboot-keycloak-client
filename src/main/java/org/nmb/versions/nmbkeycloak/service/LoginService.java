package org.nmb.versions.nmbkeycloak.service;

import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.dto.BaseResponseDto;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
import org.nmb.versions.nmbkeycloak.dto.TokenRefreshDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class LoginService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.client-id}")
    private String kcClientId;

    @Value("${keycloak.client-secret}")
    private String kcClientSecret;

    @Value("${keycloak.get-token-url}")
    private String keyCloakGetTokenUrl;

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String EXPIRES_IN = "Expires-In";

    public ResponseEntity<Object> login(LoginDto request) {
        log.info("Start to get access token");

        GoodAuthToken goodAuthToken = this.getAccessToken(request);
        return ResponseEntity.ok().body(BaseResponseDto.builder()
                .status("SUCCESS")
                .data(goodAuthToken)
                .build());
    }

    public BaseResponseDto refreshToken(TokenRefreshDto refreshRequestDto) {

        log.info("Start to refresh access token");

        GoodAuthToken goodAuthToken = this.getRefreshToken(refreshRequestDto.getRefreshToken());
        return BaseResponseDto.builder()
                .status("SUCCESS")
                .data(goodAuthToken)
                .build();
    }




    private GoodAuthToken getAccessToken(LoginDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
        requestBody.add("client_id", kcClientId);
        requestBody.add("client_secret", kcClientSecret);
        requestBody.add("username", request.getUsername());
        requestBody.add("password", request.getPassword());

        ResponseEntity<GoodAuthToken> response = restTemplate.postForEntity(keyCloakGetTokenUrl,
                new HttpEntity<>(requestBody, headers),
                GoodAuthToken.class);

        return response.getBody();
    }


    private GoodAuthToken getRefreshToken(String refreshToken) {

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


}
