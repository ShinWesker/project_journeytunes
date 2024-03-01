package dhbw.mosbach.trip;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public enum AuthorizationUtils {

    INSTANCE;

    private final HttpClient client = HttpClient.newHttpClient();

    public boolean isAuthorized(String bearerToken) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String requestBody = """
                              {
                	"token": "%s"
                }""".formatted(bearerToken.split(" ")[1]);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://host.docker.internal:9090/users/api/v1/token/verify"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            int statusCode = client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
            status = HttpStatus.resolve(statusCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public int getUserIdByToken(String bearerToken) {
        HttpStatus status;
        int userId = -1; // Default value if id is not found
        String requestBody = """
                              {
                	"token": "%s"
                }""".formatted(bearerToken.split(" ")[1]);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://host.docker.internal:9090/users/api/v1/get"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            status = HttpStatus.valueOf(response.statusCode());
            if (status == HttpStatus.OK) {
                JSONObject jsonBody = new JSONObject(responseBody);
                userId = jsonBody.getInt("id");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userId;
    }
}