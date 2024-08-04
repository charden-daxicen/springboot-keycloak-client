package org.nmb.versions.nmbkeycloak.dto.otp;

import lombok.Data;

public class OtpDto {

    @Data
    public static class Inquiry {
        private String identifier;
    }

    @Data
    public static class Verification {
        private String identifier;
        private String otp;
    }

}
