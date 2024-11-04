package dev.brian_a_miller.restclient.spotify.types;

public record SpotifyArtist(
        // external_urls
        // followers
        String[] genres,
        String href,
        String id,
        // images
        String name,
        int popularity,
        String type,
        String uri
) {
}
