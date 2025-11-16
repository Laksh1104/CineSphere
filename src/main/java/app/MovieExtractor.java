package app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import entity.Movie;

public class MovieExtractor {
    public static List<Movie> extractPopularMovies(String json) {
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(json);
            JSONArray results = obj.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject item = results.getJSONObject(i);

                String title = item.optString("title");
                String poster = item.optString("poster_path");

                Movie m = new Movie(title, poster);
                movies.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }
}
