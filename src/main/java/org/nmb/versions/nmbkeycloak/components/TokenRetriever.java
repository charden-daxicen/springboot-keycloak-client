package org.nmb.versions.nmbkeycloak.components;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nmb.versions.nmbkeycloak.dto.LoginDto;
import org.nmb.versions.nmbkeycloak.dto.tokens.GoodAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Log4j2
@RequiredArgsConstructor
@Component
public class TokenRetriever {

    private static final String GRANT_TYPE_PASSWORD = "password";

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

    private final RestTemplate restTemplate;

    public GoodAuthToken getAccessToken(LoginDto request) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
        requestBody.add("client_id", kcClientId);
        requestBody.add("client_secret", kcClientSecret);
        requestBody.add("username", request.getUsername());
        requestBody.add("password", request.getPassword());
        return this.fetchTokenFromKeycloak(requestBody);
    }

    public GoodAuthToken getAdminAccessToken() {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
        requestBody.add("client_id", kcClientId);
        requestBody.add("client_secret", kcClientSecret);
        requestBody.add("username", keycloakClientUsername);
        requestBody.add("password", keycloakClientPassword);

        return this.fetchTokenFromKeycloak(requestBody);
    }

    public GoodAuthToken fetchTokenFromKeycloak(MultiValueMap<String, String> requestBody) {

        log.info("fetching access token... {} {}", requestBody, keyCloakGetTokenUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<GoodAuthToken> response = restTemplate.postForEntity(keyCloakGetTokenUrl,
                new HttpEntity<>(requestBody, headers),
                GoodAuthToken.class);

        log.info("access token fetched successful");
        return response.getBody();
    }

    public GoodAuthToken getTokenByRefresh(String refreshToken) {

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
