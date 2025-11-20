package view;

import data_access.TmdbMovieDataAccessObject;
import entity.Movie;
import use_case.movie.MovieDataAccessInterface;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoggedInView {


        public static void main(String[] args) {
            JFrame frame = new JFrame("Background Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 800);

            JPanel backgroundPanel = new JPanel();
            backgroundPanel.setBackground(new Color(255, 255, 224));
            backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

            JLabel title = new JLabel("CineSphere", SwingConstants.CENTER);
            title.setForeground(Color.BLACK);
            title.setFont(new Font("Arial", Font.BOLD, 20));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setPreferredSize(new Dimension(800, 50));
            buttonPanel.setMaximumSize(new Dimension(800, 50));
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JButton watchlistButton = new JButton("Watchlist");
            JButton bookButton = new JButton("Book");
            JButton logoutButton = new JButton("Logout");
            JButton homeButton = new JButton("Home");
            buttonPanel.add(title);
            buttonPanel.add(homeButton);
            buttonPanel.add(watchlistButton);
            buttonPanel.add(bookButton);
            buttonPanel.add(logoutButton);

            JPanel filterPanel = new JPanel();
            filterPanel.setPreferredSize(new Dimension(800, 40));
            filterPanel.setMaximumSize(new Dimension(800, 40));
            filterPanel.setBackground(Color.WHITE);
            filterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton filterButton = new JButton("Filter");
            JLabel browseTitle = new JLabel("Browse Films By: ", SwingConstants.LEFT);


            String[] years = {"All Years", "2025", "2024", "2023", "2022"};
            JComboBox<String> yearDropdown = new JComboBox<>(years);

            String[] genres = {"All Genres", "Action", "Comedy", "Drama", "Sci-Fi", "Romance"};
            JComboBox<String> genreDropdown = new JComboBox<>(genres);

            String[] ratings = {"All Ratings", "4.5+", "4.0+", "3.5+", "3.0+", "2.5+", "2.0+", "1.5+", "1.0+"};
            JComboBox<String> ratingDropdown = new JComboBox<>(ratings);

            JTextField searchField = new JTextField(10);

            JLabel findFilm = new JLabel("Find Film: ");

            filterPanel.add(browseTitle);
            filterPanel.add(yearDropdown);
            filterPanel.add(ratingDropdown);
            filterPanel.add(genreDropdown);
            filterPanel.add(filterButton);
            filterPanel.add(findFilm);
            filterPanel.add(searchField);

            JPanel searchPanel = new JPanel();
            searchPanel.setPreferredSize(new Dimension(800, 50));

            JPanel popularFilmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            popularFilmPanel.setBackground(new Color(255, 255, 224));
            popularFilmPanel.setPreferredSize(new Dimension(300, 30));
            popularFilmPanel.setMaximumSize(new Dimension(300, 30));
            popularFilmPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel popularFilmTitle = new JLabel("Popular Films This Week: ");
            popularFilmTitle.setFont(new Font("Open Sans", Font.BOLD, 15));
            popularFilmPanel.add(popularFilmTitle);


            String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMWVlMTM2Mzg0ZTcxYmM2ODFkOTYwOTNkYmJmMmFmNyIsIm5iZiI6MTc2MzI4MTgxNS45MDgsInN1YiI6IjY5MTk4Yjk3MmI5OWU3Yzc2OTRjYTU4MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.v-d4uaS-AsAb2Ymka2E5VFc9dsZ7xdQTuHAwNdSTOzI";

            MovieDataAccessInterface movieDAO = new TmdbMovieDataAccessObject(token);

            List<Movie> movies = movieDAO.getPopularMovies();
            List<String> posterUrls = movieDAO.getPosterUrls(movies);

            JPanel moviePanel = new JPanel();
            moviePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
            moviePanel.setBackground(new Color(255, 255, 224));

            for (String posterUrl: posterUrls) {
                try {
                    ImageIcon icon = new ImageIcon(new URL(posterUrl));
                    Image scaled = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                    JLabel movieLabel = new JLabel(new ImageIcon(scaled));
                    moviePanel.add(movieLabel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            JScrollPane scrollPane = new JScrollPane(
                    moviePanel,
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            );
            scrollPane.setPreferredSize(new Dimension(850, 340));
            scrollPane.setMaximumSize(new Dimension(850, 340));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

            backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            backgroundPanel.add(buttonPanel);
            backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            backgroundPanel.add(filterPanel);
            backgroundPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            backgroundPanel.add(popularFilmPanel);
            backgroundPanel.add(scrollPane);
            frame.add(backgroundPanel);

            frame.setVisible(true);



        }
    }

