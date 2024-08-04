package org.nmb.versions.nmbkeycloak.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nmb.versions.nmbkeycloak.constants.IdentifierType;
import org.nmb.versions.nmbkeycloak.dto.common.ApiResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLookupResponse extends ApiResponse<GoodUser> {
   GoodUser respBody;
}
