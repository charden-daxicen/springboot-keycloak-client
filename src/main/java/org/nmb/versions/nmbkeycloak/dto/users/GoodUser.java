package org.nmb.versions.nmbkeycloak.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nmb.versions.nmbkeycloak.constants.IdentifierType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodUser {

    String email;
    String username;
    String phoneNumber;
    String firstName;
    String lastName;
    boolean enabled;
    IdentifierType matchedIdentifier;

}
