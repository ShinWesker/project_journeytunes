package dhbw.mosbach.spotify.controller;

import dhbw.mosbach.spotify.SpotifyUtils;
import dhbw.mosbach.spotify.controller.responseclasses.Category;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/spotify/api/v1")
public class SpotifyController {

    @GetMapping("/playlist")
    public ResponseEntity<Map<String, Integer>> getPlaylist(
            @RequestParam(value = "category", defaultValue = "rock") String category,
            @RequestParam(value = "duration", defaultValue = "240") String duration) {

        Map<String, Integer> outPlaylists = new HashMap<>();
        JSONObject playlistsJson = SpotifyUtils.INSTANCE
                .doSpotifyRequest("https://api.spotify.com/v1/browse/categories/" + category + "/playlists?limit=50");
        if (playlistsJson == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        JSONArray playlists = playlistsJson.getJSONObject("playlists").getJSONArray("items");

        Map<String, Integer> playlistList = new HashMap<>();
        for (int i = 0; i < playlists.length(); i++) {
            JSONObject playlist = playlists.getJSONObject(i);
            String playlist_id = playlist.getString("id");
            int trackCount = playlist.getJSONObject("tracks").getInt("total");
            int avgTrackDuration = 3; // avg track duration are 3 minutes
            int playlistDuration = trackCount * avgTrackDuration;
            playlistList.put("https://open.spotify.com/playlist/" + playlist_id, playlistDuration);
        }

        for (Map.Entry<String, Integer> entry : playlistList.entrySet()) {
            if (entry.getValue() >= Integer.parseInt(duration)
                    && entry.getValue() <= (Integer.parseInt(duration) * 1.25)) {
                outPlaylists.put(entry.getKey(), entry.getValue());
                return ResponseEntity.status(HttpStatus.OK).body(outPlaylists);
            }
        }

        for (Map.Entry<String, Integer> entry : playlistList.entrySet()) {
            int wert = entry.getValue();
            if (wert < Integer.parseInt(duration)) {
                int summe = 0;
                for (Map.Entry<String, Integer> innerEntry : playlistList.entrySet()) {
                    summe += innerEntry.getValue();
                    outPlaylists.put(innerEntry.getKey(), innerEntry.getValue());
                    if (summe >= Integer.parseInt(duration)) {
                        return ResponseEntity.status(HttpStatus.OK).body(outPlaylists);
                    }
                }

            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {

        JSONArray items = Objects
                .requireNonNull(
                        SpotifyUtils.INSTANCE.doSpotifyRequest("https://api.spotify.com/v1/browse/categories?limit=50"))
                .getJSONObject("categories").getJSONArray("items");
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject category = items.getJSONObject(i);
            categories.add(new Category(category.getString("id"), category.getString("name")));
        }
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
