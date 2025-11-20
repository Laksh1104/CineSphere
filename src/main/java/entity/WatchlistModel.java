package entity;

import java.util.List;
import java.net.URL;
//eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5MGEzMjJmYzgyYzEwYWFkMzlmODBjYzA1Y2YzNTgzNCIsIm5iZiI6MTc2MzI0NzI0NS44NSwic3ViIjoiNjkxOTA0OGQwOGE2ZjFlZjY2MzFkNjc4Iiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.GTI3zDG6giw-BKOs9FqXz8nVaagPqXtqw2-GwALEEuA
//90a322fc82c10aad39f80cc05cf35834 - api key

public class WatchlistModel {
    private List<String> watchlistPosters;

    public String[] getWatchlistPosters() {
        try {
            String watchlistURL = "https://api.themoviedb.org/3/account/{account_id}/watchlist/movies";
            URL url = new URL(watchlistURL);

        } catch (java.net.MalformedURLException e){
            System.out.println("Invalid URL");
        }
        return watchlistPosters.toArray(new String[0]);
    }


    public WatchlistModel(){

    }
}
