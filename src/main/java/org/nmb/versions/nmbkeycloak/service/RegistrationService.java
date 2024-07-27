package org.nmb.versions.nmbkeycloak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.components.TokenRetriever;
import org.nmb.versions.nmbkeycloak.dto.RegistrationDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.keycloak.KeycloakRegistrationDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RegistrationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.get-token-url}")
    private String keyCloakGetTokenUrl;

    private final TokenRetriever tokenRetriever;

    public ResponseEntity<Object> register(RegistrationDto registrationDto) {
        log.info("Start to get access token");

        KeycloakRegistrationDto.Request keycloakRegistrationReq = KeycloakRegistrationDto.Request.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .enabled(true)
                .credentials(KeycloakRegistrationDto.Credentials.builder()
                        .temporary(false)
                        .type(KeycloakRegistrationDto.CredentialType.password)
                        .value(registrationDto.getPassword())
                        .build())
                .build();

        GoodAuthToken goodAuthToken = this.registerOnKeycloak(keycloakRegistrationReq);

        return ResponseEntity.ok().body(ApiResponse.builder()
                .status("SUCCESS")
                .data(goodAuthToken)
                .build());
    }

    private GoodAuthToken registerOnKeycloak(KeycloakRegistrationDto.Request keycloakRegistrationReq) {

        GoodAuthToken accessToken = tokenRetriever.getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.getAccessToken());

        HttpEntity<KeycloakRegistrationDto.Request> requestEntity = new HttpEntity<>(keycloakRegistrationReq, headers);

        ResponseEntity<GoodAuthToken> response = restTemplate.postForEntity(
                keyCloakGetTokenUrl,
                requestEntity,
                GoodAuthToken.class);

        return response.getBody();
    }


}
