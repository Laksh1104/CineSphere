package entity;

import view.WatchlistView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class WatchlistController {
    private WatchlistView watchlistView;
    private WatchlistModel watchlistModel;
    private int currentpage = 0;
    private int moviesperpage = 12;


    public void forward () {
        if ((currentpage + 1) * moviesperpage < (watchlistModel.getWatchlistPosters().length)) {
            currentpage++;
            loadpage();
        }
    }

    public void back () {
        if (currentpage > 0) {
            currentpage--;
            loadpage();
        }
    }

    public void loadpage() {
        String [] allPosters = watchlistModel.getWatchlistPosters();
        ArrayList <String> currentposters = new ArrayList<>();
        if ((watchlistModel.getWatchlistPosters().length - currentpage) < moviesperpage) {
            moviesperpage = (watchlistModel.getWatchlistPosters().length - currentpage);
        }
        for(int i = currentpage; i < (currentpage + moviesperpage); i++) {
            currentposters.add(allPosters[i]);
        }
    }

    public WatchlistController(WatchlistView watchlistView, WatchlistModel watchlistModel) {
        this.watchlistView = watchlistView;
        this.watchlistModel = watchlistModel;

    }
}