package dhbw.mosbach.routes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public enum RequestUtils {
    INSTANCE;
    private final HttpClient client = HttpClient.newHttpClient();

    public JSONObject doRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .setHeader("Authorization", Configuration.INSTANCE.getAccessToken())
                .build();
        try {
            return new JSONObject(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}