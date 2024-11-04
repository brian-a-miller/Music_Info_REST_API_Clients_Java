package dev.brian_a_miller.restclient.spotify;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dev.brian_a_miller.properties.Properties;
import dev.brian_a_miller.restclient.RestClient;
import dev.brian_a_miller.restclient.RestResponse;
import dev.brian_a_miller.restclient.spotify.types.SpotifyAlbum;
import dev.brian_a_miller.restclient.spotify.types.SpotifyArtist;
import dev.brian_a_miller.restclient.spotify.types.SpotifyArtistAlbumsResponse;
import dev.brian_a_miller.restclient.spotify.types.SpotifySearchArtistResponse;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 *
 */
public class SpotifyClient {

    private final static String REQUEST_ACCESS_TOKEN_ENDPOINT_URL =
            "https://accounts.spotify.com/api/token";

    private final static String SPOTIFY_API_ROOT_URL = "https://api.spotify.com/v1/";

    private final static String PLAYLISTS_URL_PREFIX = "https://api.spotify.com/v1/playlists/";
    private final static String PLAYLIST_TRACKS_URL_SUFFIX = "/tracks";

    private final static String SPOTIFY_API_URL_SUFFIX_ARTISTS = "artists/";
    private final static String SPOTIFY_API_URL_SUFFIX_SEARCH = "search";

    private final static String SEARCH_PARAM_NAME_QUERY = "q";

    private final static String SEARCH_PARAM_NAME_LIMIT  = "limit";
    private final static String SEARCH_PARAM_NAME_MARKET = "market";
    private final static String SEARCH_PARAM_NAME_OFFSET = "offset";
    private final static String SEARCH_PARAM_NAME_TYPE   = "type";

    private final static String SEARCH_PARAM_TYPE_VALUE_ARTIST = "artist";
    private final static String SEARCH_PARAM_MARKET_VALUE_US   = "US";


    private final static String HEADER_ACCEPT_KEY   = "Accept";
    private final static String HEADER_ACCEPT_VALUE = "application/json";

    private final static String HEADER_CONTENT_TYPE_KEY = "Content-Type";
    private final static String HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";

    private final static String HEADER_AUTHORIZATION_KEY = "Authorization";
    private final static String HEADER_AUTHORIZATION_VALUE_PREFIX = "Bearer ";

    private final static String DATA_GRANT_TYPE_KEY = "grant_type";
    private final static String DATA_GRANT_TYPE_VALUE_CLIENT_CREDENTIALS = "client_credentials";

    private final static String DATA_CLIENT_ID_KEY = "client_id";
    private final static String DATA_CLIENT_SECRET_KEY = "client_secret";

    private final static String PROPERTY_KEY_CLIENT_ID = "spotify_client_id";
    private final static String PROPERTY_KEY_CLIENT_SECRET = "spotify_client_secret";

    private static String clientID;
    private static String clientSecret;
    private static String accessToken;
    private static LocalDateTime accessTokenExpiration;

    static {
        clientID = Properties.getValue(PROPERTY_KEY_CLIENT_ID);
        clientSecret = Properties.getValue(PROPERTY_KEY_CLIENT_SECRET);
        accessToken = null;
        accessTokenExpiration = null;
    }

    private RestClient restClient;

    public SpotifyClient() {
        this.restClient = new RestClient();
    }

    private String getAccessToken() {
        // TODO: reverse if/else
        if (accessToken == null || accessTokenExpiration == null ||
            LocalDateTime.now().isAfter(accessTokenExpiration)) {

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put(HEADER_CONTENT_TYPE_KEY, HEADER_CONTENT_TYPE_VALUE);

            Map<String, String> dataMap = new HashMap<>();
            dataMap.put(DATA_GRANT_TYPE_KEY, DATA_GRANT_TYPE_VALUE_CLIENT_CREDENTIALS);
            dataMap.put(DATA_CLIENT_ID_KEY, clientID);
            dataMap.put(DATA_CLIENT_SECRET_KEY, clientSecret);

            RestResponse restResponseRequestAccessToken =
                    restClient.post(REQUEST_ACCESS_TOKEN_ENDPOINT_URL,
                            headerMap, dataMap);

            if ((restResponseRequestAccessToken != null) &&
                    (restResponseRequestAccessToken.getCode() == HTTP_OK) &&
                    (restResponseRequestAccessToken.getBody() != null)) {

                Gson gson = new Gson();
                try {
                    SpotifyAccessTokenResponse spotifyAccessTokenResponse =
                            gson.fromJson(restResponseRequestAccessToken.getBody(),
                                    SpotifyAccessTokenResponse.class);
                    if ((spotifyAccessTokenResponse != null) &&
                            (spotifyAccessTokenResponse.access_token() != null)) {
                        accessToken = spotifyAccessTokenResponse.access_token();
                        accessTokenExpiration = LocalDateTime.now().plusSeconds(
                                spotifyAccessTokenResponse.expires_in()
                        );
                        return accessToken;
                    }
                } catch (JsonSyntaxException ex) {
                    System.err.println("Failed to parse access token response from Spotify");
                    ex.printStackTrace(System.err);
                }
            }

        } else {
            return accessToken;
        }
        return null;
    }

    /**
     *
     * @param playlistURL fully-qualified URL
     * @return
     */
    public List<SpotifyAlbum> getListAlbumsInPlaylist(String playlistURL) {
        List<SpotifyAlbum> albumsList = new ArrayList<>();

        String accessToken = getAccessToken();
        System.out.println("accessToken: " + accessToken);

        String url = playlistURL.trim();
        if (!url.endsWith("tracks") && !url.endsWith("tracks/")) {
            if (url.endsWith("/")) {
                url = url + "tracks";
            } else {
                url = url + "/tracks";
            }
        }

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE);
        headerMap.put(HEADER_AUTHORIZATION_KEY, HEADER_AUTHORIZATION_VALUE_PREFIX + accessToken);

        Gson gson = new Gson();
        RestResponse restResponse = restClient.get(url, headerMap);
        if (restResponse == null) {
            System.err.println("null response to " + url);
        } else {
            // System.out.println(url);
            // System.out.println("Response Code: " + restResponse.getCode());
            // System.out.println("Response Body: " + restResponse.getBody());
        }
        boolean readNextPage = true;
        while (readNextPage && (restResponse != null) && (restResponse.getCode() == HTTP_OK) &&
                (restResponse.getBody() != null)) {

            try {
                SpotifyPlaylistTracksResponse spotResponse =
                        gson.fromJson(restResponse.getBody(), SpotifyPlaylistTracksResponse.class);

                if (spotResponse == null) {
                    readNextPage = false;
                } else {

                    var items = spotResponse.items();
                    if (items != null) {
                        for (var item : items) {
                            var track = item.track();
                            if (track != null) {
                                var album = track.album();
                                if (album != null) {
                                    if (!albumsList.contains(album)) {
                                        albumsList.add(album);
                                    }
                                }
                            }
                        }
                    }

                    String nextURL = spotResponse.next();
                    if ((nextURL == null) || nextURL.isBlank()) {
                        readNextPage = false;
                    } else {
                        restResponse = restClient.get(nextURL, headerMap);
                        if (restResponse == null) {
                            //System.err.println("null response to " + url);
                        } else {
                            //System.out.println(url);
                            //System.out.println("Response Code: " + restResponse.getCode());
                        }

                    }
                }

            } catch (Exception ex) {
                //
                readNextPage = false;
            }
        }


        return albumsList;
    }

    public List<String> searchArtist(String artistName) {

        List<String> spotifyArtistURIs = new ArrayList<>();

        String accessToken = getAccessToken();
        // TODO: error checking if access token is null, empty, bad, expired

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE);
        headerMap.put(HEADER_AUTHORIZATION_KEY, HEADER_AUTHORIZATION_VALUE_PREFIX + accessToken);

        StringBuilder urlSB = new StringBuilder();

        urlSB.append(SPOTIFY_API_ROOT_URL);
        urlSB.append(SPOTIFY_API_URL_SUFFIX_SEARCH);

        urlSB.append("?");
        urlSB.append(SEARCH_PARAM_NAME_QUERY);
        urlSB.append("=");
        urlSB.append(URLEncoder.encode(artistName, StandardCharsets.UTF_8));

        urlSB.append("&");
        urlSB.append(SEARCH_PARAM_NAME_TYPE);
        urlSB.append("=");
        urlSB.append(SEARCH_PARAM_TYPE_VALUE_ARTIST);

        urlSB.append("&");
        urlSB.append(SEARCH_PARAM_NAME_MARKET);
        urlSB.append("=");
        urlSB.append(SEARCH_PARAM_MARKET_VALUE_US);

        urlSB.append("&");
        urlSB.append(SEARCH_PARAM_NAME_LIMIT);
        urlSB.append("=10");

        urlSB.append("&");
        urlSB.append(SEARCH_PARAM_NAME_OFFSET);
        urlSB.append("=0");

        RestResponse restResponse = restClient.get(urlSB.toString(),
                headerMap);

        if (restResponse != null) {

            // System.out.println(restResponse.getBody());

            Gson gson = new Gson();

            SpotifySearchArtistResponse spotResponse =
                    gson.fromJson(restResponse.getBody(), SpotifySearchArtistResponse.class);
            if (spotResponse != null) {
                var artists = spotResponse.artists();
                if (artists != null) {
                    SpotifyArtist[] artistsArray = artists.items();
                    if (artistsArray != null) {
                        for (SpotifyArtist artist : artistsArray) {
                            if (artist.name().equalsIgnoreCase(artistName) ||
                                    artist.name().equalsIgnoreCase("The " + artistName) ||
                                    ("The " + artist.name()).equalsIgnoreCase(artistName)) {

                                spotifyArtistURIs.add(artist.uri());

                                // System.out.println("name: " + artist.name() + ", uri: " + artist.uri());
                            }
                        }
                    }
                }
            }

        }

        return spotifyArtistURIs;
    }

    public static String getSpotifyArtistIDFromURI(String uri) {

        if ((uri != null) && (uri.length() > 15) &&
                (uri.startsWith("spotify:artist:"))) {

            return uri.substring(15);
        }

        return null;
    }

    /**
     *
     * @param spotifyArtistID example: "5qV8qZrppecEsKTWwCCtA7"
     */
    public void getArtistsAlbums(String spotifyArtistID) {

        if ((spotifyArtistID == null) || spotifyArtistID.isBlank()) {
            throw new IllegalArgumentException("ID must not be null or empty string");
        }

        String accessToken = getAccessToken();
        // TODO: error checking if access token is null, empty, bad, expired

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE);
        headerMap.put(HEADER_AUTHORIZATION_KEY, HEADER_AUTHORIZATION_VALUE_PREFIX + accessToken);

        StringBuilder urlSB = new StringBuilder();

        urlSB.append(SPOTIFY_API_ROOT_URL);
        urlSB.append(SPOTIFY_API_URL_SUFFIX_ARTISTS);
        urlSB.append(spotifyArtistID);
        urlSB.append("/albums?include_groups=album,compilation");

        urlSB.append("&");
        urlSB.append(SEARCH_PARAM_NAME_MARKET);
        urlSB.append("=");
        urlSB.append(SEARCH_PARAM_MARKET_VALUE_US);

        urlSB.append("&");
        urlSB.append(SEARCH_PARAM_NAME_LIMIT);
        urlSB.append("=50");

        // urlSB.append("&");
        // urlSB.append(SEARCH_PARAM_NAME_OFFSET);
        // urlSB.append("=0");

        RestResponse restResponse = restClient.get(urlSB.toString(),
                headerMap);

        if (restResponse != null) {
            // System.out.println(restResponse.getBody());

            Gson gson = new Gson();

            SpotifyArtistAlbumsResponse spotResponse =
                    gson.fromJson(restResponse.getBody(), SpotifyArtistAlbumsResponse.class);

            int albumCount = 0;

            if (spotResponse != null) {

                SpotifyAlbum[] albums = spotResponse.items();
                if (albums != null) {
                    for (SpotifyAlbum album : albums) {
                        albumCount++;
                        System.out.println(albumCount + ". " + album.artists()[0].name() + ": " + album.name() + " (" + album.album_type()
                                + "), released " + album.release_date() + " [ source: Spotify ]");
                    }
                }

                while ((spotResponse != null) && (spotResponse.next() != null) && (!spotResponse.next().isBlank())) {

                    restResponse = restClient.get(spotResponse.next(), headerMap);
                    if (restResponse == null) {
                        spotResponse = null;
                    } else {
                        spotResponse =
                                gson.fromJson(restResponse.getBody(), SpotifyArtistAlbumsResponse.class);
                        if (spotResponse != null) {
                            albums = spotResponse.items();
                            if (albums != null) {
                                for (SpotifyAlbum album : albums) {
                                    albumCount++;
                                    System.out.println(albumCount + ". " + album.artists()[0].name() + ": " + album.name() + " (" + album.album_type()
                                            + "), released " + album.release_date() + " [ source: Spotify ]");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
