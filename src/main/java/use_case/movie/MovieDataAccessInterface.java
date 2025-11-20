package use_case.movie;

import entity.Movie;

import java.util.List;

public interface MovieDataAccessInterface {
    List<Movie> getPopularMovies();
    List<String> getPosterUrls(List<Movie> movies);
}
