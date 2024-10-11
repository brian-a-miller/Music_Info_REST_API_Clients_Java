package dev.brian_a_miller.restclient.musicbrainz;

import com.google.gson.Gson;
import dev.brian_a_miller.restclient.RestClient;
import dev.brian_a_miller.restclient.RestResponse;
import dev.brian_a_miller.restclient.musicbrainz.types.MusicBrainzReleaseGroup;
import dev.brian_a_miller.restclient.musicbrainz.types.MusicBrainzSearchReleaseGroupResponse;

import java.util.HashMap;
import java.util.Map;

public class MusicBrainzClient {

    private final static String MUSIC_BRAINZ_API_ROOT_URL =
            "http://musicbrainz.org/ws/2/";

    private final static String SEARCH_TYPE_RELEASE_GROUP = "release-group";

    private final static String REQUEST_PARAM_FORMAT_JSON = "fmt=json";

    private final static String REQUEST_PARAM_NAME_QUERY  = "query";

    private final static String SEARCH_PARAM_NAME_ARTIST_MBID = "arid";
    private final static String SEARCH_PARAM_NAME_RELEASE_GROUP_MBID = "reid";

    private final RestClient restClient;

    public MusicBrainzClient() {
        restClient = new RestClient();
    }

    /**
     * Query Release Group on MuscBrainz
     *
     * @param artistMbid artist MBID
     * @param releaseGroupMbid release group MBID
     */
    public MusicBrainzReleaseGroup getReleaseGroupInfo(String artistMbid, String releaseGroupMbid) {

        StringBuilder urlSB = new StringBuilder();
        urlSB.append(MUSIC_BRAINZ_API_ROOT_URL);
        urlSB.append(SEARCH_TYPE_RELEASE_GROUP);
        urlSB.append("/?");
        urlSB.append(REQUEST_PARAM_FORMAT_JSON);
        urlSB.append("&");
        urlSB.append(REQUEST_PARAM_NAME_QUERY);
        urlSB.append("=");
        urlSB.append(SEARCH_PARAM_NAME_ARTIST_MBID);
        urlSB.append(":");
        urlSB.append(artistMbid);
        urlSB.append("%20AND%20");
        urlSB.append(SEARCH_PARAM_NAME_RELEASE_GROUP_MBID);
        urlSB.append(":");
        urlSB.append(releaseGroupMbid);

        Map<String, String> headers = new HashMap<>();

        RestResponse restResponse = restClient.get(urlSB.toString(), headers);
        if (restResponse == null) {
            System.err.println("restResponse == null");
            return null;
        }
        System.out.println("Response code: " + restResponse.getCode());
        System.out.println("Response reason: " + restResponse.getReason());
        System.out.println("Response body:");
        System.out.println(restResponse.getBody());

        if (restResponse.getBody() != null) {
            Gson gson = new Gson();
            MusicBrainzSearchReleaseGroupResponse releaseGroupResponse =
                    gson.fromJson(restResponse.getBody(),
                            MusicBrainzSearchReleaseGroupResponse.class);
            if (releaseGroupResponse != null) {
                return releaseGroupResponse.release_groups()[0];
            }
        }
        return null;
    }
}
