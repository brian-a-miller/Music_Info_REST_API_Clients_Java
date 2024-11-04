package dev.brian_a_miller.restclient.spotify;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        SpotifyClient spotClient = new SpotifyClient();

        List<String> uris = spotClient.searchArtist("Frank Zappa");
        for (String uri : uris) {
            String spotifyArtistID = SpotifyClient.getSpotifyArtistIDFromURI(uri);

            spotClient.getArtistsAlbums(spotifyArtistID);
        }
    }
}
