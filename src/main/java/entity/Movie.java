package entity;

public class Movie {
    private final int film_id;
    private final String film_name;

    public Movie(int filmId, String filmName) {
        film_id = filmId;
        film_name = filmName;
    }

    public String getFilmName() {
        return film_name;
    }
    public int getFilmId() {
        return film_id;
    }
}
