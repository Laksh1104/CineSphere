package view;

import service.TmdbService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilteredView extends JFrame {

    // UI components
    private JComboBox<String> yearDropdown;
    private JComboBox<String> ratingDropdown;
    private JComboBox<String> genreDropdown;
    private JTextField searchField;

    private JLabel filteredByLabel;
    private JPanel gridPanel;
    private JLabel pageLabel;

    // Movie posters for the current page
    private final List<String> moviePosterUrls = new ArrayList<>();

    // caching for poster icons
    private final Map<String, ImageIcon> iconCache = new HashMap<>();

    private final TmdbService tmdbService = new TmdbService();

    private int currentPage = 1;
    private static final int PAGE_SIZE = 8;
    private static final int TOTAL_PAGES = 10;   // we keep 10 pages for now

    public FilteredView() {
        setTitle("CineSphere - Filtered Results");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);

        buildUI();
        updateFilteredByLabel();
        loadMoviesFromApi();
        updateGrid();
    }

    private void buildUI() {
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(255, 255, 224));
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("CineSphere", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 255, 224));
        buttonPanel.setPreferredSize(new Dimension(800, 50));
        buttonPanel.setMaximumSize(new Dimension(800, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton watchlistButton = new JButton("Watchlist");
        JButton bookButton = new JButton("Booking");
        JButton logoutButton = new JButton("Logout");
        JButton homeButton = new JButton("Home");

        buttonPanel.add(title);
        buttonPanel.add(homeButton);
        buttonPanel.add(watchlistButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(logoutButton);

        // Tempoprary: just close this window on logout.
        logoutButton.addActionListener(e -> this.dispose());

        JPanel filterPanel = new JPanel();
        filterPanel.setPreferredSize(new Dimension(800, 40));
        filterPanel.setMaximumSize(new Dimension(800, 40));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton filterButton = new JButton("Filter");
        JLabel browseTitle = new JLabel("Browse by: ", SwingConstants.LEFT);

        String[] years = {"All Years", "2025", "2024", "2023", "2022"};
        yearDropdown = new JComboBox<>(years);

        String[] ratings = {"All Ratings", "4.5+", "4.0+", "3.5+", "3.0+", "2.5+", "2.0+", "1.5+", "1.0+"};
        ratingDropdown = new JComboBox<>(ratings);

        String[] genres = {"All Genres", "Action", "Comedy", "Drama", "Sci-Fi", "Romance"};
        genreDropdown = new JComboBox<>(genres);

        searchField = new JTextField(10);
        JLabel findFilm = new JLabel("Find a Film: ");

        filterPanel.add(browseTitle);
        filterPanel.add(yearDropdown);
        filterPanel.add(ratingDropdown);
        filterPanel.add(genreDropdown);
        filterPanel.add(filterButton);
        filterPanel.add(findFilm);
        filterPanel.add(searchField);

        filterButton.addActionListener(e -> {
            updateFilteredByLabel();
            currentPage = 1;
            loadMoviesFromApi();
            updateGrid();
        });

        JPanel filteredByPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filteredByPanel.setBackground(new Color(255, 255, 224));
        filteredByPanel.setPreferredSize(new Dimension(800, 30));
        filteredByPanel.setMaximumSize(new Dimension(800, 30));
        filteredByPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        filteredByLabel = new JLabel();
        filteredByPanel.add(filteredByLabel);

        gridPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        gridPanel.setBackground(new Color(255, 255, 224));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Enough space for 200x300 posters x 2 rows
        gridPanel.setPreferredSize(new Dimension(850, 650));
        gridPanel.setMaximumSize(new Dimension(850, 650));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pagingPanel = new JPanel(new BorderLayout());
        pagingPanel.setBackground(new Color(255, 255, 224));
        pagingPanel.setPreferredSize(new Dimension(800, 40));
        pagingPanel.setMaximumSize(new Dimension(800, 40));
        pagingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton prevButton = new JButton("<<");
        JButton nextButton = new JButton(">>");
        pageLabel = new JLabel("", SwingConstants.CENTER);

        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadMoviesFromApi();
                updateGrid();
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage < TOTAL_PAGES) {
                currentPage++;
                loadMoviesFromApi();
                updateGrid();
            }
        });

        pagingPanel.add(prevButton, BorderLayout.WEST);
        pagingPanel.add(pageLabel, BorderLayout.CENTER);
        pagingPanel.add(nextButton, BorderLayout.EAST);

        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(buttonPanel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backgroundPanel.add(filterPanel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backgroundPanel.add(filteredByPanel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backgroundPanel.add(gridPanel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backgroundPanel.add(pagingPanel);

        add(backgroundPanel);
    }

    private void updateFilteredByLabel() {
        String year = (String) yearDropdown.getSelectedItem();
        String rating = (String) ratingDropdown.getSelectedItem();
        String genre = (String) genreDropdown.getSelectedItem();
        String search = searchField.getText().trim();

        StringBuilder sb = new StringBuilder("Filtered by: ");
        sb.append("Year = ").append(year).append("   ");
        sb.append("Rating = ").append(rating).append("   ");
        sb.append("Genre = ").append(genre);
        if (!search.isEmpty()) {
            sb.append("   Search = \"").append(search).append("\"");
        }

        filteredByLabel.setText(sb.toString());
    }

    /** Calls TMDB and fills moviePosterUrls for the current page + filters. */
    private void loadMoviesFromApi() {
        moviePosterUrls.clear();

        String year = (String) yearDropdown.getSelectedItem();
        String rating = (String) ratingDropdown.getSelectedItem();
        String genre = (String) genreDropdown.getSelectedItem();
        String search = searchField.getText().trim();

        List<String> posters = tmdbService.fetchPosterUrls(year, rating, genre, search, currentPage);
        moviePosterUrls.addAll(posters);
    }

    private void updateGrid() {
        gridPanel.removeAll();

        int count = Math.min(PAGE_SIZE, moviePosterUrls.size());
        for (int i = 0; i < count; i++) {
            String url = moviePosterUrls.get(i);
            gridPanel.add(createPosterLabel(url));
        }

        for (int i = count; i < PAGE_SIZE; i++) {
            JPanel empty = new JPanel();
            empty.setBackground(new Color(255, 255, 224));
            empty.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
            gridPanel.add(empty);
        }

        pageLabel.setText(currentPage + " / " + TOTAL_PAGES);

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JLabel createPosterLabel(String urlString) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            ImageIcon cached = iconCache.get(urlString);
            if (cached == null) {
                ImageIcon original = new ImageIcon(new java.net.URL(urlString));
                Image scaled = original.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                cached = new ImageIcon(scaled);
                iconCache.put(urlString, cached);
            }
            label.setIcon(cached);
        } catch (Exception e) {
            label.setText("No Image");
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }

        label.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        return label;
    }

    // quick manual test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FilteredView().setVisible(true));
    }
}
