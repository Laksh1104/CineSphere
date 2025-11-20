package entity;


public class Movie {

    private String title;
    private String posterPath;

    public Movie(String title, String posterPath) {
        this.title = title;
        this.posterPath = posterPath;
    }

    public String getTitle() {

        return title;
    }

    public String getPosterPath() {

        return posterPath;
    }
}
