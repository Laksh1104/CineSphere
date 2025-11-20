package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TmdbService {

    private static final String API_KEY = "6289d1f5d1b8e2d2a78614fc9e48742b";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    /**
     * Return a list of poster image URLs using the selected filters.
     */
    public List<String> fetchPosterUrls(String year,
                                        String rating,
                                        String genre,
                                        String search,
                                        int page) {

        List<String> posters = new ArrayList<>();

        try {
            // ----- build URL -----
            boolean hasSearch = search != null && !search.trim().isEmpty();

            StringBuilder url = new StringBuilder(BASE_URL);
            if (hasSearch) {
                url.append("/search/movie?api_key=").append(API_KEY);
                url.append("&language=en-US");
                url.append("&query=").append(URLEncoder.encode(search.trim(),
                        StandardCharsets.UTF_8.name()));
            } else {
                url.append("/discover/movie?api_key=").append(API_KEY);
                url.append("&language=en-US");
                url.append("&sort_by=popularity.desc");
                url.append("&include_adult=false&include_video=false");
            }

            // year filter
            if (year != null && !year.startsWith("All")) {
                try {
                    int y = Integer.parseInt(year.trim());
                    url.append("&primary_release_year=").append(y);
                } catch (NumberFormatException ignored) {}
            }

            // rating filter (e.g. "4.0+" -> 4.0)
            if (rating != null && !rating.startsWith("All")) {
                String num = rating.replace("+", "").trim();
                try {
                    double r = Double.parseDouble(num);
                    url.append("&vote_average.gte=").append(r);
                } catch (NumberFormatException ignored) {}
            }

            // genre filter
            int genreId = mapGenreToId(genre);
            if (genreId > 0) {
                url.append("&with_genres=").append(genreId);
            }

            url.append("&page=").append(page);

            // ----- call API -----
            String json = doGet(url.toString());

            // ----- pull poster_path fields -----
            posters = extractPosterUrls(json);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posters;
    }

    // Map combo-box text -> TMDB genre id
    private int mapGenreToId(String genre) {
        if (genre == null || genre.startsWith("All")) return 0;

        switch (genre) {
            case "Action":  return 28;
            case "Comedy":  return 35;
            case "Drama":   return 18;
            case "Sci-Fi":  return 878;
            case "Romance": return 10749;
            default:        return 0;
        }
    }

    private String doGet(String urlString) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } finally {
            if (reader != null) reader.close();
            if (conn != null) conn.disconnect();
        }
    }

    /**
     * Very simple parser: finds every "poster_path":"...".
     */
    private List<String> extractPosterUrls(String json) {
        List<String> posters = new ArrayList<>();
        if (json == null) return posters;

        String key = "\"poster_path\"";
        int index = 0;

        while (true) {
            index = json.indexOf(key, index);
            if (index == -1) break;

            int colon = json.indexOf(":", index);
            int firstQuote = json.indexOf("\"", colon + 1);
            int secondQuote = json.indexOf("\"", firstQuote + 1);
            if (firstQuote == -1 || secondQuote == -1) break;

            String path = json.substring(firstQuote + 1, secondQuote);
            if (!"null".equals(path) && !path.isEmpty()) {
                posters.add(IMAGE_BASE_URL + path);
            }

            index = secondQuote + 1;
        }

        return posters;
    }
}
