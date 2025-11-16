package view;

public class MoviePosterBuilder {
    private static final String base_URL = "https://image.tmdb.org/t/p/w500";

    public static String buildPosterUrl(String posterPath) {
//        if (posterPath == null || posterPath.isEmpty()) {
//            return null;
//        }
        return base_URL + posterPath;
    }
}
