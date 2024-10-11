package dev.brian_a_miller.restclient.lastfm.types;

public record LastFMAlbum(
        String name,
        long playcount,
        String mbid,
        String url,
        LastFMArtist artist
) {
}
