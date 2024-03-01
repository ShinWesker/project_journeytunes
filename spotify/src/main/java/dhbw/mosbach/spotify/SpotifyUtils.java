package dhbw.mosbach.spotify;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public enum SpotifyUtils {
    INSTANCE;
    private final HttpClient client = HttpClient.newHttpClient();
    private final String clientId = "2828d9ddeea548a3b9a6a9dae736ca82";
    private final String clientSecret = "9b2af5e8aaf24bd79f074f7d0a859b0f";
    private String token;

    public void startTokenTimer() {

        Calendar today = Calendar.getInstance();
        today.getTime();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://accounts.spotify.com/api/token"))
                        .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret))
                        .setHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();

                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    token = new JSONObject(response.body()).getString("access_token");
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(task, today.getTime(), TimeUnit.MILLISECONDS.convert(59, TimeUnit.MINUTES)); // period: 59 Minutes
    }

    public JSONObject doSpotifyRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .setHeader("Authorization", "Bearer " + token)
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            return new JSONObject(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
