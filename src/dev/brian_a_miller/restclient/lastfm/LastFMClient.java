package dev.brian_a_miller.restclient.lastfm;

import static java.net.HttpURLConnection.HTTP_OK;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dev.brian_a_miller.properties.Properties;
import dev.brian_a_miller.restclient.RestClient;
import dev.brian_a_miller.restclient.RestResponse;
import dev.brian_a_miller.restclient.lastfm.types.*;

//
// TODO: refactor common code used in multiple methods to helpers
//       to avoid duplication
//
// TODO: read user agent from properties file ???
//
public class LastFMClient {

    private final static String LAST_FM_API_ROOT_URL = "http://ws.audioscrobbler.com/2.0/";

    private final static String HEADER_NAME_USER_AGENT = "User-Agent";
    private final static String USER_AGENT_VALUE       = "MyAlbumDJ/0.1"; // TODO: read from properties

    private final static String PROPERTY_NAME_API_KEY = "last_fm_api_key";

    private final static String REQ_PARAM_NAME_ALBUM  = "album";
    private final static String REQ_PARAM_NAME_API_KEY = "api_key";
    private final static String REQ_PARAM_NAME_ARTIST  = "artist";
    private final static String REQ_PARAM_NAME_FORMAT  = "format";
    private final static String REQ_PARAM_NAME_MBID    = "mbid";
    private final static String REQ_PARAM_NAME_METHOD  = "method";

    private final static String REQ_PARAM_FORMAT_VALUE_JSON = "json";

    private final static String REQ_METHOD_ALBUM_GET_INFO =
            "album.getinfo";
    private final static String REQ_METHOD_ARTIST_GET_INFO =
            "artist.getinfo";
    private final static String REQ_METHOD_ARTIST_GET_TOP_ALBUMS =
            "artist.gettopalbums";
    private final static String REQ_METHOD_CHART_GET_TOP_ARTISTS =
            "chart.gettopartists";

    private static final String apiKey;

    static {
        apiKey = Properties.getValue(PROPERTY_NAME_API_KEY);
    }

    private final RestClient restClient;

    public LastFMClient() {
        this.restClient = new RestClient();
    }

    public LastFMAlbumInfo getAlbumInfo(String artist, String album) {
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(LAST_FM_API_ROOT_URL);
        urlSB.append("?");
        urlSB.append(REQ_PARAM_NAME_METHOD);
        urlSB.append("=");
        urlSB.append(REQ_METHOD_ALBUM_GET_INFO);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_API_KEY);
        urlSB.append("=");
        urlSB.append(apiKey);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_ARTIST);
        urlSB.append("=");
        urlSB.append(URLEncoder.encode(artist, StandardCharsets.UTF_8));
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_ALBUM);
        urlSB.append("=");
        urlSB.append(URLEncoder.encode(album, StandardCharsets.UTF_8));
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_FORMAT);
        urlSB.append("=");
        urlSB.append(REQ_PARAM_FORMAT_VALUE_JSON);

        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_NAME_USER_AGENT, USER_AGENT_VALUE);

        RestResponse restResponse = restClient.get(urlSB.toString(), headers);
        if (restResponse == null) {
            System.err.println("restResponse == null");
            return null;
        }
        System.out.println("Response code: " + restResponse.getCode());
        System.out.println("Response reason: " + restResponse.getReason());
        System.out.println("Response body:");
        System.out.println(restResponse.getBody());

        if (restResponse.getBody() == null || restResponse.getBody().isBlank()) {
            return null;
        }

        Gson gson = new Gson();
        try {
            LastFMGetAlbumInfoResponse getAlbumInfoResponse =
                    gson.fromJson(restResponse.getBody(), LastFMGetAlbumInfoResponse.class);

            if (getAlbumInfoResponse == null) {
                return null;
            }

            return getAlbumInfoResponse.album();

        } catch (JsonSyntaxException jsEx) {
            System.err.println("Error parsing Get Album Info response to JSON");
            jsEx.printStackTrace(System.err);
        }

        return null;
    }

    /**
     * Query last.fm for artist info
     *
     * @param artistName artist name
     */
    public LastFMArtist getArtistInfo(String artistName) {

        HashMap<String, String> getRequestParams = new HashMap<>();
        getRequestParams.put(REQ_PARAM_NAME_METHOD, REQ_METHOD_ARTIST_GET_INFO);
        getRequestParams.put(REQ_PARAM_NAME_ARTIST, URLEncoder.encode(artistName, StandardCharsets.UTF_8));
        getRequestParams.put(REQ_PARAM_NAME_API_KEY, apiKey);
        getRequestParams.put(REQ_PARAM_NAME_FORMAT, REQ_PARAM_FORMAT_VALUE_JSON);

        // TODO: move URLEncode.encode call to inside of buildURL method ?!?!
        String url = RestClient.buildURL(LAST_FM_API_ROOT_URL, getRequestParams);

        var restResponse = restClient.get(url, null);
        if (restResponse != null) {
            System.out.println("Response Code: " + restResponse.getCode());
            System.out.println("Response Reason: " + restResponse.getReason());
            System.out.println("Response Body: " + restResponse.getBody());
            Gson gson = new Gson();
            LastFMGetArtistInfoResponse artistInfoResponse = gson.fromJson(restResponse.getBody(),
                    LastFMGetArtistInfoResponse.class);
            if (artistInfoResponse != null) {
                return artistInfoResponse.artist();
            }
        }

        /*String responseBodyString = restClient.getAsString(url);
        if ((responseBodyString == null) || responseBodyString.startsWith("ERROR")) {
            System.out.println(responseBodyString);
        }*/
        return null;
    }

    /**
     * Get array of top (most popular) artists
     *
     * @return
     */
    public LastFMArtist[] getTopArtists() {

        StringBuilder urlSB = new StringBuilder();
        urlSB.append(LAST_FM_API_ROOT_URL);
        urlSB.append("?");
        urlSB.append(REQ_PARAM_NAME_METHOD);
        urlSB.append("=");
        urlSB.append(REQ_METHOD_CHART_GET_TOP_ARTISTS);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_API_KEY);
        urlSB.append("=");
        urlSB.append(apiKey);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_FORMAT);
        urlSB.append("=");
        urlSB.append(REQ_PARAM_FORMAT_VALUE_JSON);

        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_NAME_USER_AGENT, USER_AGENT_VALUE);

        RestResponse restResponse = restClient.get(urlSB.toString(), headers);
        if (restResponse == null) {
            System.err.println("restResponse == null");
        } else {
            System.out.println("Response code: " + restResponse.getCode());
            System.out.println("Response reason: " + restResponse.getReason());
            System.out.println("Response body:");
            System.out.println(restResponse.getBody());
        }

        Gson gson = new Gson();
        try {
            LastFMArtists getTopArtistsResponse =
                    gson.fromJson(restResponse.getBody(),
                            LastFMArtists.class);
            if (getTopArtistsResponse == null) {
                System.err.println("ERROR: gson.fromJson returned null");
            } else {
                LastFMArtistArray artistArray = getTopArtistsResponse.artists();
                if (artistArray == null) {
                    System.err.println("ERROR: artistArray == null");
                } else {
                    LastFMArtist[] topArtists = artistArray.artist();
                    if (topArtists == null) {
                        System.err.println("ERROR: topArtists == null");
                    } else {
                        System.out.println("topArtists.length: " + topArtists.length);
                        for (int i = 0; i < topArtists.length; i++) {
                            System.out.println("topArtists[" + i + "].name() = " + topArtists[i].name());
                            System.out.println("topArtists[" + i + "].mbid() = " + topArtists[i].mbid());
                        }
                    }
                    return topArtists;
                }
            }

        } catch (JsonSyntaxException ex) {
            System.err.println("ERROR: Failed to parse response via Gson");
            ex.printStackTrace(System.err);
        }

        return null;
    }

    public LastFMAlbum[] getTopAlbumsForArtistMbid(String artistMbid) {

        StringBuilder urlSB = new StringBuilder();
        urlSB.append(LAST_FM_API_ROOT_URL);
        urlSB.append("?");
        urlSB.append(REQ_PARAM_NAME_METHOD);
        urlSB.append("=");
        urlSB.append(REQ_METHOD_ARTIST_GET_TOP_ALBUMS);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_MBID);
        urlSB.append("=");
        urlSB.append(artistMbid);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_API_KEY);
        urlSB.append("=");
        urlSB.append(apiKey);
        urlSB.append("&limit=100");
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_FORMAT);
        urlSB.append("=");
        urlSB.append(REQ_PARAM_FORMAT_VALUE_JSON);

        return processArtistGetTopAlbums(urlSB.toString());
    }

    public LastFMAlbum[] getTopAlbumsForArtistName(String artistName) {

        StringBuilder urlSB = new StringBuilder();
        urlSB.append(LAST_FM_API_ROOT_URL);
        urlSB.append("?");
        urlSB.append(REQ_PARAM_NAME_METHOD);
        urlSB.append("=");
        urlSB.append(REQ_METHOD_ARTIST_GET_TOP_ALBUMS);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_ARTIST);
        urlSB.append("=");
        urlSB.append(URLEncoder.encode(artistName, StandardCharsets.UTF_8));
        urlSB.append("&limit=100");
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_API_KEY);
        urlSB.append("=");
        urlSB.append(apiKey);
        urlSB.append("&");
        urlSB.append(REQ_PARAM_NAME_FORMAT);
        urlSB.append("=");
        urlSB.append(REQ_PARAM_FORMAT_VALUE_JSON);

        return processArtistGetTopAlbums(urlSB.toString());
    }

    private LastFMAlbum[] processArtistGetTopAlbums(String url) {

        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_NAME_USER_AGENT, USER_AGENT_VALUE);

        RestResponse restResponse = restClient.get(url, headers);

        if (restResponse == null) {
            System.err.println("restResponse == null");
        } else {
            // System.out.println("Response code: " + restResponse.getCode());
            // System.out.println("Response reason: " + restResponse.getReason());
            //System.out.println("Response body:");
            //System.out.println(restResponse.getBody());

            if (restResponse.getCode() == HTTP_OK) {

                Gson gson = new Gson();
                LastFMTopAlbumsResponse topAlbumsResponse =
                        gson.fromJson(restResponse.getBody(),
                                LastFMTopAlbumsResponse.class);
                if ((topAlbumsResponse != null) &&
                        (topAlbumsResponse.topalbums() != null)) {
                    return topAlbumsResponse.topalbums().album();
                }
            }
        }
        return null;
    }
}
