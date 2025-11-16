package app;

import entity.Movie;

public interface MovieDetailsGateway {
    Movie fetchMovieDetails(int movieId) throws Exception;
}
