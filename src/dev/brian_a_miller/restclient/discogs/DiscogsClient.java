package dev.brian_a_miller.restclient.discogs;

import dev.brian_a_miller.restclient.RestClient;
import dev.brian_a_miller.restclient.RestResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DiscogsClient {

    private final static String DISCOGS_API_BASE_URL = "https://api.discogs.com/";

    // TODO: move to properties file, should NOT be hard-coded in Java here
    private final static String USER_AGENT = "UniqueUserAgentStringForYourApp";

    private final RestClient restClient;

    private Map<String, String> headers;

    public DiscogsClient() {
        restClient = new RestClient();

        //
        // TODO: move from constructor, don't hardcode values like this
        // TODO: move key and secret to separate properties text file
        //        use Properties class to read from properties text file
        //
        headers = new HashMap<>();
        headers.put("User-Agent", "UniqueUserAgentStringForYourApp"); // TODO
        headers.put("Authorization", "Discogs key=YOUR-KEY-HERE secret=YOUR-SECRET-HERE"); // TODO

    }

    private void getRequestToken() {

        String currentTimestamp = ""; // TODO

        String myCallback = ""; // TODO

        Map<String, String> hdrs = new HashMap<>();
        hdrs.put("Content-Type", "application/x-www-form-urlencoded");

// TODO: USE PROPERTIES INSTEAD OF HARD-CODING
        hdrs.put("Authorization",
                "OAuth oauth_consumer_key=\"YOUR_CONSUMER_KEY_HERE\"," +
                "oauth_nonce=\"UNIQUE_STRING_OR_DATE_TIME_STAMP_HERE\"," +
                "oauth_signature=\"UNIQUE_STRING_ENDING_WITH_AMPERSAND\"," +
                "oauth_signature_method=\"PLAINTEXT\"," +
                "oauth_timestamp=\"" + currentTimestamp + "\"" +
                "oauth_callback=\"" + myCallback + "\"");
        hdrs.put("User-Agent", "UNIQUE_USER_AGENT_STRING_FOR_YOUR_APP");
    }

    public void searchArtist(String artistName) {

        String url = DISCOGS_API_BASE_URL + "database/search?q=" +
                URLEncoder.encode(artistName, StandardCharsets.UTF_8) +
                "&type=artist";

        RestResponse restResponse = restClient.get(url, headers);
        System.out.println(restResponse.getCode());
        System.out.println(restResponse.getReason());
        System.out.println(restResponse.getBody());
    }
}
