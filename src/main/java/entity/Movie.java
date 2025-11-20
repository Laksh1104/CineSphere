package entity;

import org.json.JSONArray;
import java.util.List;

public record Movie(
        int filmId,
        String filmName,
        String director,
        String releaseDate,
        double ratingOutOf5,
        List<String> genres,
        String description,
        JSONArray reviews,
        String posterUrl
) {

}
