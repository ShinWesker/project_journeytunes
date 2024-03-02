package dhbw.mosbach.routes.controller;

import dhbw.mosbach.routes.Configuration;
import dhbw.mosbach.routes.RequestUtils;
import dhbw.mosbach.routes.modules.Location;
import dhbw.mosbach.routes.modules.Route;
import dhbw.mosbach.routes.modules.RouteRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/routes/api/v1")
public class RouteController {

    @PostMapping("/create")
    public ResponseEntity<Object> createRoute(@RequestBody RouteRequest request) {


        Route route = new Route(request.getStart(), request.getEnd());
        JSONObject jsonObject = RequestUtils.INSTANCE.doRequest(Configuration.INSTANCE.getApiUrlDirections() + "?start="
                + route.getStart().getLng() + "," + route.getStart().getLat() + "&end=" + route.getEnd().getLng() + ","
                + route.getEnd().getLat());

        JSONArray featuresArray = jsonObject.getJSONArray("features");

        for (int i = 0; i < featuresArray.length(); i++) {
            JSONObject feature = featuresArray.getJSONObject(i);

            JSONObject geometry = feature.getJSONObject("geometry");

            JSONArray coordinatesArray = geometry.getJSONArray("coordinates");

            for (int j = 0; j < coordinatesArray.length(); j++) {
                JSONArray coordinatePair = coordinatesArray.getJSONArray(j);

                double longitude = coordinatePair.getDouble(0);
                double latitude = coordinatePair.getDouble(1);

                route.addWayPoint(new Location(latitude, longitude));
            }
        }
        return new ResponseEntity<>(route, HttpStatus.OK);
    }

    @GetMapping("/coordinates")
    public ResponseEntity<Location> getCoordinates(@RequestParam("text") String text) {
        String url = Configuration.INSTANCE.getApiUrlGeocode() + "?api_key=" + Configuration.INSTANCE.getAccessToken()
                + "&text=" + URLEncoder.encode(text + " Germany", StandardCharsets.UTF_8);

        JSONObject jsonObject = RequestUtils.INSTANCE.doRequest(url);
        if (jsonObject == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        JSONArray features = jsonObject.getJSONArray("features");

        List<Location> locations = new ArrayList<>();

        // Extract coordinates
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            double longitude = coordinates.getDouble(0);
            double latitude = coordinates.getDouble(1);
            locations.add(new Location(latitude, longitude));
        }

        return new ResponseEntity<>(locations.get(0), HttpStatus.OK);
    }

}
