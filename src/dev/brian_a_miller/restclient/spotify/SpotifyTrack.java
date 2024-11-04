package dev.brian_a_miller.restclient.spotify;

import dev.brian_a_miller.restclient.spotify.types.SpotifyAlbum;
import dev.brian_a_miller.restclient.spotify.types.SpotifyArtist;

public record SpotifyTrack(
    SpotifyAlbum album,
    SpotifyArtist[] artists,
    String[] available_markets,
    int disc_number,
    int duration_ms,
    boolean explicit,
    // external_ids
    // external_urls
    String href,
    String id,
    boolean is_playable,
    // linked_from
    // restrictions
    String name,
    int popularity,
    String preview_url,
    int track_number,
    String type,
    String uri,
    boolean is_local
) {


}
