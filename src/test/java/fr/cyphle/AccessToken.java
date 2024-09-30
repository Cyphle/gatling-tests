package fr.cyphle;

import com.fasterxml.jackson.annotation.JsonProperty;

record AccessToken(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("expires_in")
    Integer expiresIn,
    @JsonProperty("refresh_expires_in")
    Integer refreshExpiresIn,
    @JsonProperty("refresh_token")
    String refreshToken,
    @JsonProperty("token_type")
    String tokenType,
    String scope
) {
}
