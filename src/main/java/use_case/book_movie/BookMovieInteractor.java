package use_case.book_movie;

import entity.MovieTicket;
import entity.Movie;
import entity.Cinema;
import entity.ShowTime;

/**
 * The Book Movie Interactor.
 */
public class BookMovieInteractor implements BookMovieInputBoundary {

    private final BookMovieDataAccessInterface ticketDataAccessObject;
    private final BookMovieOutputBoundary bookingPresenter;

    public BookMovieInteractor(BookMovieDataAccessInterface dao,
                               BookMovieOutputBoundary presenter) {
        this.ticketDataAccessObject = dao;
        this.bookingPresenter = presenter;
    }

    @Override
    public void execute(BookMovieInputData inputData) {
        if (inputData == null) {
            bookingPresenter.prepareFailView("Missing booking information");
            return;
        }

        Movie movie = inputData.getMovie();
        Cinema cinema = inputData.getCinema();
        String date = inputData.getDate();
        ShowTime showtime = inputData.getShowtime();
        int seatCount = inputData.getSeats().size();

        // Each seat costs $20
        int costPerSeat = 20;
        int totalCost = seatCount * costPerSeat;

        // Create ticket entity
        MovieTicket ticket = new MovieTicket(
                movie,
                cinema,
                date,
                showtime,
                inputData.getSeats(),
                totalCost
        );

        // Save ticket using DAO
        // ticketDataAccessObject.save(ticket);

        // Create Output Data for presenter
        BookMovieOutputData outputData = new BookMovieOutputData(
                movie,
                date,
                cinema,
                showtime,
                inputData.getSeats(),
                totalCost
        );

        bookingPresenter.prepareSuccessView(outputData);
    }
}