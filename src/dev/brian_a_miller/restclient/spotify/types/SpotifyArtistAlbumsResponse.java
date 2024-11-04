package dev.brian_a_miller.restclient.spotify.types;

public record SpotifyArtistAlbumsResponse(
        String href,
        int limit,
        String next,
        int offset,
        String previous,
        int total,
        SpotifyAlbum[] items) { }
