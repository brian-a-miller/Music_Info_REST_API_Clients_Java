package dev.brian_a_miller.restclient.spotify;

public record SpotifyPlaylistContentItem(
        String added_at,
        boolean is_local,
        SpotifyTrack track) {
}
