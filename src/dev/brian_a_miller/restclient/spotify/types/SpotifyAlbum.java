package dev.brian_a_miller.restclient.spotify.types;

import java.util.Objects;

public record SpotifyAlbum(
        String album_type,
        int total_tracks,
        boolean is_playable,
        String[] available_markets,
        // SpotifyExternalUrls external_urls,
        String href,
        String id,
        // SpotifyImage[] images,
        String name,
        String release_date,
        String release_date_precision,
        // restrictions
        String type,
        String uri,
        SpotifyArtist[] artists
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpotifyAlbum that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(album_type, that.album_type) && Objects.equals(release_date, that.release_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(album_type, id, name, release_date, type);
    }
}
