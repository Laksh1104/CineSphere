package use_case.book_movie;

/**
 * Input Boundary for actions related to booking a movie.
 */
public interface BookMovieInputBoundary {
    /**
     * Executes the Book Ticket use case.
     * @param inputData the input data for booking a ticket
     */
    void execute(BookMovieInputData inputData);
}
