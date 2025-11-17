package use_case.book_movie;

import entity.Movie;

import java.util.List;

public interface BookMovieDataAccessInterface {
    List<Movie> getNowShowingMovies();

}
