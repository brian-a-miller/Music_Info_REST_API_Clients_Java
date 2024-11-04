package dev.brian_a_miller.restclient.spotify;

public record SpotifyAccessTokenResponse(String access_token, String token_type,
        int expires_in) {
}
