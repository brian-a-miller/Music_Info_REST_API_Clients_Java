package dev.brian_a_miller.restclient.lastfm.types;

public record LastFMArtist(String name, String playcount,
                           String listeners, String mbid,
                           String url, String streamable
                           /* LastFMImage[] image */) {
}
