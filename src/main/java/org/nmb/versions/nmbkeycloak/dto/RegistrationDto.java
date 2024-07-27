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

    private String password;

}
