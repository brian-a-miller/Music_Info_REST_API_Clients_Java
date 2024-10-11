package dev.brian_a_miller.restclient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class RestClient {

    private final HttpClient httpClient;

    public RestClient() {
        httpClient = HttpClient.newHttpClient();
    }

    public String getAsString(String urlString) {

        String result = "";

        try {
            URL url = new URL(urlString);

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url.toURI())
                    // .header("User-Agent", "Chrome")
                    .headers("Accept", "application/json") //, "Accept","text/html")
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString()); //.ofLines());
            if (response.statusCode() != HTTP_OK) {
                result = "ERROR: " + response.statusCode();
                System.err.println("Status code: " + response.statusCode());
            } else {
                result = response.body();
            //response.body().collect(Collectors.joining());
            }

        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace(System.err);
            return "ERROR: " + e;
        }

        return result;
    }

    public RestResponse get(String urlString, Map<String, String> httpHeaders) {
        RestResponse restResponse = null;
        try {
            URL url = new URL(urlString);

            var builder = HttpRequest.newBuilder()
                    .GET()
                    .uri(url.toURI())
                    .timeout(Duration.ofSeconds(30));
            if (httpHeaders != null) {
                for (Map.Entry<String, String> header : httpHeaders.entrySet()) {
                    builder.header(header.getKey(), header.getValue());
                    // System.out.println("Added header: " + header.getKey() + ": " + header.getValue());
                }
            }
            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            restResponse = new RestResponse(response.statusCode(),
                    response.body());
            if (response.statusCode() != HTTP_OK) {
                System.err.println(response.statusCode());
                System.err.println(response.body());
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace(System.err);
            // TODO: logging
            // TODO: throw runtime exception ??
        }
        return restResponse;
    }

    public RestResponse post(String urlString, Map<String, String> httpHeaders, Map<String, String> dataKeyValuePairs) {

        RestResponse restResponse = null;
        StringBuilder bodySB = new StringBuilder();
        for (Map.Entry<String, String> entry : dataKeyValuePairs.entrySet()) {
            if (!bodySB.isEmpty()) {
                bodySB.append("&");
            }
            bodySB.append(entry.getKey());
            bodySB.append("=");
            bodySB.append(entry.getValue());
        }
        String body = bodySB.toString();

        try {
            URL url = new URL(urlString);

            var builder = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(url.toURI())
                    .timeout(Duration.ofSeconds(30));
            for (Map.Entry<String, String> header : httpHeaders.entrySet()) {
                builder.header(header.getKey(), header.getValue());
                // System.out.println("Added header: " + header.getKey() + ": " + header.getValue());
            }


            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            restResponse = new RestResponse(response.statusCode(),
                    response.body());
            if (response.statusCode() != HTTP_OK) {
                System.err.println(response.statusCode());
                System.err.println(response.body());
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace(System.err);
            // TODO: logging
            // TODO: throw runtime exception ??
        }

        return restResponse;
    }

    public static String buildURL(String baseURL, HashMap<String, String> getParameterKeyValuePairs) {
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(baseURL);
        if ((getParameterKeyValuePairs != null) && !getParameterKeyValuePairs.isEmpty()) {
            urlSB.append("?");
            boolean isFirst = true;
            for (Map.Entry<String, String> parameter : getParameterKeyValuePairs.entrySet()) {
                if (!isFirst) {
                    urlSB.append("&");
                }
                urlSB.append(parameter.getKey());
                urlSB.append("=");
                urlSB.append(parameter.getValue());

                isFirst = false;
            }
        }
        return urlSB.toString();
    }
}
