package app;

import entity.Movie;
import view.MovieDetailsView;

import javax.swing.*;

public class MovieDetailsController {

    private final MovieDetailsGateway gateway;

    // Constructor: pass in the gateway (so we can call TMDB)
    public MovieDetailsController(MovieDetailsGateway gateway) {
        this.gateway = gateway;
    }

    // Called when a movie poster/button is clicked
    public void onMovieClicked(int movieId) {
        // Run this in a background thread so UI doesn’t freeze
        new SwingWorker<Movie, Void>() {
            @Override
            protected Movie doInBackground() throws Exception {
                return gateway.fetchMovieDetails(movieId);
            }

            @Override
            protected void done() {
                try {
                    Movie movie = get(); // result from background thread
                    new MovieDetailsView(movie); // open the details window
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Failed to load movie details: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}