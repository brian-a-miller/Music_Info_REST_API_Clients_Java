package dev.brian_a_miller.restclient.discogs;

public class Main {
    public static void main(String[] args) {
        DiscogsClient discogsClient = new DiscogsClient();
        discogsClient.searchArtist("The Beatles");
    }
}
