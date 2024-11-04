package com.myalbumdj.restclient.setlistfm;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.myalbumdj.properties.Properties;
import com.myalbumdj.restclient.RestClient;
import com.myalbumdj.restclient.RestResponse;
import com.myalbumdj.restclient.setlistfm.data.UserAttendedResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class SetlistFMClient {

    private final static String HEADER_ACCEPT_KEY   = "Accept";
    private final static String HEADER_ACCEPT_VALUE = "application/json";

    private final static String HEADER_XAPIKEY_KEY  = "x-api-key";

    private final static String PROPERTIES_NAME_API_KEY = "setlist_fm_api_key";

    private static String apiKey;

    private final static Map<String, String> requestHeaders = new HashMap<>();

    static {
        apiKey = Properties.getValue(PROPERTIES_NAME_API_KEY);
        System.out.println("Read property: PROPERTIES_NAME_API_KEY: " + apiKey);
        requestHeaders.put(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE);
        requestHeaders.put(HEADER_XAPIKEY_KEY, apiKey);
    }

    private final RestClient restClient;

    private SetlistFMClient() {
        restClient = new RestClient();
    }

    private static SetlistFMClient instance = new SetlistFMClient();

    public static SetlistFMClient getInstance() {
        return instance;
    }

    public UserAttendedResponse getConcertsAttendedByUser(String username) {

        UserAttendedResponse response = getConcertsAttendedByUser(username, 1);

        if (response != null) {
            int lastPage = (int) Math.ceil((double) response.total / (double) response.itemsPerPage);
            if (response.setlist == null) {
                response.setlist = new ArrayList<>();
            }

            for (int page = 2; page <= lastPage; page++) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                UserAttendedResponse nextPageResponse = getConcertsAttendedByUser(username, page);

                if (nextPageResponse != null) {
                    if ((nextPageResponse.code < 400) &&
                        (nextPageResponse.setlist != null) && !nextPageResponse.setlist.isEmpty()) {

                        response.setlist.addAll(nextPageResponse.setlist);
                    } else {
                        System.out.println("code: " + nextPageResponse.code);
                        System.out.println("status: " + nextPageResponse.status);
                        System.out.println("message: " + nextPageResponse.message);
                    }
                }
            }
        }
        return response;
    }

    public UserAttendedResponse getConcertsAttendedByUser(String username, int page) {
        UserAttendedResponse result = null;
        String urlStr = "https://api.setlist.fm/rest/1.0/user/%s/attended?p=%d"
                .formatted(username, page);
        RestResponse restResponse = restClient.get(urlStr, requestHeaders);
        System.out.println("Request: GET " + urlStr);
//        System.out.println("Response: " + restResponse.getCode() + " " +
//                restResponse.getReason());
        if ((restResponse.getCode() == HTTP_OK) &&
                (restResponse.getBody() != null)) {

            Gson gson = new Gson();
            try {
                result = gson.fromJson(restResponse.getBody(), UserAttendedResponse.class);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return result;
    }
}

