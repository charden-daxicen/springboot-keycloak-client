package org.nmb.versions.nmbkeycloak.dto.keycloak.otps;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class KeycloakOTPDto {


    @Data
    @Builder
    public static class InquiryRequest{
        String identifier;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Response{
        String username;
    }


    @Data
    @Builder
    public static class VerificationRequest{
        String identifier;
        String otp;
    }


}
