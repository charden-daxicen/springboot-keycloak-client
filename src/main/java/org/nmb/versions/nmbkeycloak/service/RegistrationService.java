package org.nmb.versions.nmbkeycloak.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.components.TokenRetriever;
import org.nmb.versions.nmbkeycloak.constants.IdentifierType;
import org.nmb.versions.nmbkeycloak.constants.RespCodes;
import org.nmb.versions.nmbkeycloak.dto.RegistrationDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.keycloak.KeycloakRegistrationDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.nmb.versions.nmbkeycloak.dto.users.GoodUser;
import org.nmb.versions.nmbkeycloak.utils.JHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RegistrationService {


    @Value("${keycloak.user.registration-url}")
    private String keycloakUserRegistrationUrl;

    @Value("${keycloak.user.user-lookup-url}")
    private String userLookupUrl;

    private final TokenRetriever tokenRetriever;

    private final RestTemplate restTemplate;

    public ApiResponse<GoodAuthToken> register(RegistrationDto registrationDto) {

        /// Check user
        ApiResponse<GoodUser> goodUserApiResponse = this.checkUserOnKeycloak(registrationDto);
        if (goodUserApiResponse.getRespCode() == RespCodes.OK) {
            String message = this.mapMessage(goodUserApiResponse.getRespBody());
            return ApiResponse.failure("User exists. " + message);

        } else if (goodUserApiResponse.getRespCode() != RespCodes.USER_DOES_NOT_EXIST) {
            return ApiResponse.failure("Failed to verify user information");
        }

        KeycloakRegistrationDto.Credentials passwordCredentials = KeycloakRegistrationDto.Credentials.builder()
                .temporary(false)
                .type(KeycloakRegistrationDto.CredentialType.password)
                .value(registrationDto.getPassword())
                .build();

        HashMap<String, List<String>> attributes = new HashMap<>();
        attributes.put("phoneNumber", Collections.singletonList(registrationDto.getPhoneNumber()));
        attributes.put("countryAlpha3Code", Collections.singletonList(registrationDto.getCountryAlpha3Code()));
        attributes.put("countryCallingCode", Collections.singletonList(registrationDto.getCountryCallingCode()));

        KeycloakRegistrationDto.Request keycloakRegistrationReq = KeycloakRegistrationDto.Request.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .enabled(true)
                .credentials(List.of(passwordCredentials))
                .attributes(attributes)
                .build();

        GoodAuthToken goodAuthToken = this.registerOnKeycloak(keycloakRegistrationReq);
        return ApiResponse.success(goodAuthToken, "Authenticated");
    }

    private GoodAuthToken registerOnKeycloak(KeycloakRegistrationDto.Request keycloakRegistrationReq) {

        GoodAuthToken accessToken = tokenRetriever.getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.getAccessToken());

        String payload = JHelper.toJson(keycloakRegistrationReq);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
        log.info("registering users {}", payload);

        ResponseEntity<GoodAuthToken> response = restTemplate.postForEntity(
                keycloakUserRegistrationUrl,
                requestEntity,
                GoodAuthToken.class);

        return response.getBody();
    }

    private ApiResponse<GoodUser> checkUserOnKeycloak(RegistrationDto registrationDto) {

        GoodAuthToken accessToken = tokenRetriever.getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.getAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String url = String.format("%s?email=%s&phone=%s", userLookupUrl, registrationDto.getEmail(), registrationDto.getPhoneNumber());
        log.info("checking user {}", url);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return JHelper.fromJson(responseEntity.getBody(), new TypeReference<>() {});
        }
        return ApiResponse.failure("User not found");
    }

    private String mapMessage(GoodUser goodUser) {
        if (goodUser.getMatchedIdentifier() == IdentifierType.PHONE) {
            return String.format("Phone: %s exists", goodUser.getPhoneNumber());
        }

        if (goodUser.getMatchedIdentifier() == IdentifierType.EMAIL) {
            return String.format("Email: %s exists", goodUser.getEmail());
        }
        return "Identifier exists";
    }


}
