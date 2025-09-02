package com.web.jaru.security.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthDTO {
    public record GoogleOAuthTokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") int expiresIn,
            @JsonProperty("scope") String scope,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("id_token") String idToken,
            @JsonProperty("refresh_token") String refreshToken
    ) {}

    public record GoogleOAuthRequestParams(
            @JsonProperty("grant_type") String grantType,
            @JsonProperty("client_id") String clientId,
            @JsonProperty("client_secret") String clientSecret,
            @JsonProperty("redirect_uri") String redirectUri,
            @JsonProperty("code") String code
    ) {}

}
