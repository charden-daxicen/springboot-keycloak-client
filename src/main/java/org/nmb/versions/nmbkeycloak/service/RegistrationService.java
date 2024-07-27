package org.nmb.versions.nmbkeycloak.service;

import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.dto.BaseResponseDto;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
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
public class RegistrationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.client-id}")
    private String kcClientId;

    @Value("${keycloak.client-secret}")
    private String kcClientSecret;

    @Value("${keycloak.get-token-url}")
    private String keyCloakGetTokenUrl;

    private static final String GRANT_TYPE_PASSWORD = "password";

    public ResponseEntity<Object> register(LoginDto request) {
        log.info("Start to get access token");

        GoodAuthToken goodAuthToken = this.registerOnKeycloak(request);
        return ResponseEntity.ok().body(BaseResponseDto.builder()
                .status("SUCCESS")
                .data(goodAuthToken)
                .build());
    }

    private GoodAuthToken registerOnKeycloak(LoginDto request) {

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


}
