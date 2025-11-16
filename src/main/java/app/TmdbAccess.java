package app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TmdbAccess {

    private HttpClient client = HttpClient.newHttpClient();
    private String bearerToken;

    public TmdbAccess(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String accessPopularMovieJson() {
        String url = "https://api.themoviedb.org/3/movie/popular";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", bearerToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
//
//    public static void main(String[] args) {
//        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMWVlMTM2Mzg0ZTcxYmM2ODFkOTYwOTNkYmJmMmFmNyIsIm5iZiI6MTc2MzI4MTgxNS45MDgsInN1YiI6IjY5MTk4Yjk3MmI5OWU3Yzc2OTRjYTU4MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.v-d4uaS-AsAb2Ymka2E5VFc9dsZ7xdQTuHAwNdSTOzI";
//        TmdbAccess tmdb = new TmdbAccess(token);
//
//        System.out.println(tmdb.accessPopularMovieJson());
//    }
}

