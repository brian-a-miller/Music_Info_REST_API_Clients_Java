package dev.brian_a_miller.restclient.lastfm;

import dev.brian_a_miller.restclient.lastfm.types.LastFMAlbum;
import dev.brian_a_miller.restclient.lastfm.types.LastFMAlbumInfo;
import dev.brian_a_miller.restclient.lastfm.types.LastFMArtist;
import dev.brian_a_miller.restclient.musicbrainz.MusicBrainzClient;
import dev.brian_a_miller.restclient.musicbrainz.types.MusicBrainzReleaseGroup;

public class Main {

    public static void main(String[] args) {

        LastFMClient client = new LastFMClient();

        String artistMBID = null;
        String albumMBID = null;
        LastFMArtist artistInfo = client.getArtistInfo("King Crimson");
        if (artistInfo != null) {
            artistMBID = artistInfo.mbid();
        }
        LastFMAlbumInfo albumInfo = client.getAlbumInfo("King Crimson", "In the Court of the Crimson King");
        if (albumInfo != null) {
            albumMBID = albumInfo.mbid();
        }
        if ((artistMBID != null) && (albumMBID != null)) {
            MusicBrainzClient mbClient = new MusicBrainzClient();
            MusicBrainzReleaseGroup releaseGroup = mbClient.getReleaseGroupInfo(artistMBID, albumMBID);
            if (releaseGroup != null) {
                System.out.println("First release date: " + releaseGroup.first_release_date());
            }
        }

        // client.getAlbumInfo("Frank Zappa", "Hot Rats");

        /*LastFMAlbumInfo albumInfo = client.getAlbumInfo("King Crimson", "In the Court of the Crimson King");
        if (albumInfo == null) {
            System.err.println("ERROR: LastFMClient method getAlbumInfo(artistName, albumName) returned null");
        } else {
            System.out.println("Artist: " + albumInfo.artist());
            System.out.println("Album Name: " + albumInfo.name());
            System.out.println("Album MBID: " + albumInfo.mbid());
        }*/

        /*
        LastFMArtist[] topArtists = client.getTopArtists();

        for (int i = 0; i < topArtists.length; i++) {
            String artistName = topArtists[i].name();
            System.out.println("=========================================");
            // System.out.println("Artist name: " + artistName);
            String mbid = topArtists[i].mbid();
            LastFMAlbum[] albums = null;
            if ((mbid != null) && !mbid.isBlank()) {
                albums = client.getTopAlbumsForArtistMbid(mbid);
            } else {
                albums = client.getTopAlbumsForArtistName(artistName);
            }
            if (albums != null) {
                for (int j = 0; j < albums.length; j++) {
                    // System.out.println("    Album name: " + albums[j].name());
                    String albumName = albums[j].name();
                    System.out.println(artistName + ": " + albumName + " (album mbid=" + albums[j].mbid() + ")");
                }
            }
        }
        */
    }
}
