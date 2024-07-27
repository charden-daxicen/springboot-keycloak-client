package org.nmb.versions.nmbkeycloak.dto.tokens;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshDto {

    private String refreshToken;

}
