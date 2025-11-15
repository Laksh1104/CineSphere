package app;

import entity.Movie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Real TMDB implementation of MovieDetailsGateway.
 * - Reads API key from env var TMDB_API_KEY (safer than hardcoding).
 * - Calls /movie/{id}?append_to_response=credits,reviews
 * - Parses details (title, release_date, director, rating, genres, overview, poster)
 * - Returns your current Movie entity (id + title). You can expand later.
 */
public class TMDBMovieDetailsGateway implements MovieDetailsGateway {

    private static final String API_KEY = System.getenv("TMDB_API_KEY"); // set this in your environment
    private static final String BASE = "https://api.themoviedb.org/3";
    // Image base kept for future use when you show posters:
    private static final String IMG_BASE = "https://image.tmdb.org/t/p/w500";

    private static final OkHttpClient HTTP = new OkHttpClient();

    @Override
    public Movie fetchMovieDetails(int movieId) throws Exception {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("TMDB_API_KEY is not set in environment variables.");
        }

        HttpUrl url = HttpUrl.parse(BASE + "/movie/" + movieId).newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("language", "en-US")
                .addQueryParameter("append_to_response", "credits,reviews")
                .build();

        Request req = new Request.Builder().url(url).get().build();

        try (Response res = HTTP.newCall(req).execute()) {
            if (!res.isSuccessful()) {
                throw new RuntimeException("TMDB error " + res.code() + " for movieId=" + movieId);
            }

            String body = res.body().string();
            JSONObject j = new JSONObject(body);

            // ----- Parse fields you’ll likely want (keeping for later UI updates) -----
            String title = j.optString("title", "(Untitled)");
            String releaseDate = j.optString("release_date", "");
            String overview = j.optString("overview", "");
            double vote10 = j.optDouble("vote_average", 0.0);
            double ratingOutOf5 = Math.round((vote10 / 2.0) * 10.0) / 10.0;

            List<String> genres = new ArrayList<>();
            JSONArray gj = j.optJSONArray("genres");
            if (gj != null) {
                for (int i = 0; i < gj.length(); i++) {
                    genres.add(gj.getJSONObject(i).optString("name", ""));
                }
            }

            String posterPath = j.optString("poster_path", null);
            String posterUrl = (posterPath == null || posterPath.isEmpty())
                    ? null : IMG_BASE + posterPath;

            String director = "";
            JSONObject credits = j.optJSONObject("credits");
            if (credits != null) {
                JSONArray crew = credits.optJSONArray("crew");
                if (crew != null) {
                    for (int i = 0; i < crew.length(); i++) {
                        JSONObject c = crew.getJSONObject(i);
                        if ("Director".equalsIgnoreCase(c.optString("job"))) {
                            director = c.optString("name", "");
                            break;
                        }
                    }
                }
            }

            // (Optional) Reviews if/when you want them:
            // JSONArray reviews = j.optJSONObject("reviews") != null
            //         ? j.getJSONObject("reviews").optJSONArray("results")
            //         : null;

            // ----- Return your current entity (id + name) -----
            // Later, when you expand Movie or add a MovieDetails DTO,
            // use the parsed variables above (title, releaseDate, director, ratingOutOf5, genres, overview, posterUrl).
            return new Movie(movieId, title);
        }
    }

    public static void main(String[] args) {

    }
}