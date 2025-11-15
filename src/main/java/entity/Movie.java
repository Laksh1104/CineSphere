package entity;

public class Movie {
    private final int film_id;
    private final String film_name;

    public Movie(int filmId, String filmName) {
        this.film_id = filmId;
        this.film_name = filmName;
    }

    public int getFilmId() {
        return film_id;
    }

    public String getFilmName() {
        return film_name;
    }

}
