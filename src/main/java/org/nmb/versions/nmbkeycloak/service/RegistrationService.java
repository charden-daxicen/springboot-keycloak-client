package org.nmb.versions.nmbkeycloak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.nmb.versions.nmbkeycloak.components.KeycloakAdapter;
import org.nmb.versions.nmbkeycloak.constants.IdentifierType;
import org.nmb.versions.nmbkeycloak.constants.RespCodes;
import org.nmb.versions.nmbkeycloak.dto.RegistrationDto;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;
import org.nmb.versions.nmbkeycloak.dto.keycloak.KeycloakRegistrationDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.nmb.versions.nmbkeycloak.dto.users.GoodUser;
import org.nmb.versions.nmbkeycloak.dto.users.UserLookupResponse;
import org.nmb.versions.nmbkeycloak.utils.JHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Slf4j
@Service
public class RegistrationService {


    @Value("${keycloak.user.registration-url}")
    private String keycloakUserRegistrationUrl;

    @Value("${keycloak.user.user-lookup-url}")
    private String userLookupUrl;

    private final RestTemplate restTemplate;

    private final KeycloakAdapter keycloakAdapter;

    public ApiResponse<?> register(RegistrationDto registrationDto) {

        /// Check user
        ApiResponse<GoodUser> goodUserApiResponse = this.checkUserOnKeycloak(registrationDto);
        if (goodUserApiResponse.getRespCode() == RespCodes.OK) {
            String message = this.formatRegistrationErrorMessage(goodUserApiResponse.getRespBody());
            return ApiResponse.failure(message);

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
                .username(registrationDto.getEmail())
                .email(registrationDto.getEmail())
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .enabled(true)
                .credentials(List.of(passwordCredentials))
                .attributes(attributes)
                .build();

        return this.registerOnKeycloak(keycloakRegistrationReq);
    }

    private ApiResponse<?> registerOnKeycloak(KeycloakRegistrationDto.Request keycloakRegistrationReq) {

        GoodAuthToken accessToken = keycloakAdapter.getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.getAccessToken());

        String payload = JHelper.toJson(keycloakRegistrationReq);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
        log.info("registering users {}", payload);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(keycloakUserRegistrationUrl, requestEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("registration api response {}",responseEntity.getBody());
            return ApiResponse.success("User registered successfully");
        }

        return ApiResponse.failure("Failed to register user");
    }

    private ApiResponse<GoodUser> checkUserOnKeycloak(RegistrationDto registrationDto) {

        GoodAuthToken accessToken = keycloakAdapter.getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.getAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String url = String.format("%s?email=%s&phone=%s", userLookupUrl, registrationDto.getEmail(), registrationDto.getPhoneNumber());
        log.info("checking user {}", url);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("api response {}",responseEntity.getBody());
            return JHelper.fromJson(responseEntity.getBody(), UserLookupResponse.class);
        }

        return ApiResponse.failure("User not found");
    }

    private String formatRegistrationErrorMessage(GoodUser goodUser) {
        if (goodUser.getMatchedIdentifier() == IdentifierType.PHONE) {
            return String.format("Phone number %s already exists. Try logging in or resetting the password", goodUser.getPhoneNumber());
        }

        if (goodUser.getMatchedIdentifier() == IdentifierType.EMAIL) {
            return String.format("Email %s is already registered. Try logging in or resetting the password", goodUser.getEmail());
        }
        return "Information is already registered";
    }


}
