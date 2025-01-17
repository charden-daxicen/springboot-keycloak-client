package org.nmb.versions.nmbkeycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    private String username;
    private String email;

    private String firstName;
    private String lastName;

    private String password;

    private String phoneNumber;
    private String countryAlpha3Code;
    private String countryCallingCode;

}
