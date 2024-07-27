package org.nmb.versions.nmbkeycloak.dto.keycloak;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class KeycloakRegistrationDto {

    public enum CredentialType {
       password
    }

    @Data
    @Builder
    public static class Request{
        String username;
        boolean enabled;
        String email;

        String firstName;
        String lastName;
    }

    @Data
    @Builder
    public static class Credentials{
        CredentialType type;
        String value;
        boolean temporary;
    }



    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Response{
        String username;
    }



}
