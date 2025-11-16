package view;

import app.TMDBMovieDetailsGateway;
import entity.Movie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MovieDetailsView extends JFrame {

    public MovieDetailsView(Movie movie) {
        super(movie.filmName());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(16, 16));
        getContentPane().setBackground(new Color(0xFFFBE6));
        getRootPane().setBorder(new EmptyBorder(16, 16, 16, 16));

        // ---- Header (Title • Date • Directed by) ----
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        header.setOpaque(false);
        JLabel title = bold(new JLabel(movie.filmName()));
        JLabel date = new JLabel(" –  %s  –  ".formatted(movie.releaseDate()));
        JLabel director = new JLabel("Directed by: %s".formatted(movie.director()));
        header.add(title);
        header.add(date);
        header.add(director);
        add(header, BorderLayout.NORTH);

        // ---- Center (Poster left, facts & button right) ----
        JPanel center = new JPanel(new BorderLayout(16, 0));
        center.setOpaque(false);

        // Poster with actual movie image
        JLabel poster = new JLabel("Loading...", SwingConstants.CENTER);
        poster.setPreferredSize(new Dimension(300, 420));
        poster.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Load the movie poster image
        loadPosterImage(poster, movie);
        
        center.add(poster, BorderLayout.WEST);

        // Facts + Add to Watchlist
        Box facts = Box.createVerticalBox();
        facts.setOpaque(false);
        facts.add(new JLabel("Rating: %s".formatted(movie.ratingOutOf5())));
        facts.add(Box.createVerticalStrut(6));
        facts.add(new JLabel("Genres: %s".formatted(String.join(", ", movie.genres()))));
        facts.add(Box.createVerticalStrut(12));

        JButton watchlistBtn = new JButton("Add To Watchlist");
        watchlistBtn.addActionListener(e -> watchlistBtn.setText("Already Watchlisted"));
        facts.add(watchlistBtn);

        center.add(facts, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // ---- Bottom (Description + Reviews in a scroll) ----
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JTextArea desc = new JTextArea(
                "Description: " + movie.description());
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel reviewsHeader = bold(new JLabel("Popular Reviews:"));
        
        // Extract and format only the first 2 reviews
        String reviewsText = formatReviews(movie.reviews(), 2);
        
        JTextArea reviews = new JTextArea(reviewsText);
        reviews.setLineWrap(true);
        reviews.setWrapStyleWord(true);
        reviews.setEditable(false);
        reviews.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        // Create a scrollable pane for reviews with fixed height
        JScrollPane reviewsScroll = new JScrollPane(reviews,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        reviewsScroll.setPreferredSize(new Dimension(0, 120)); // Fixed height for reviews
        reviewsScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        bottom.add(desc);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(reviewsHeader);
        bottom.add(reviewsScroll);

        JScrollPane scroll = new JScrollPane(bottom,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        add(scroll, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static JLabel bold(JLabel l) {
        l.setFont(l.getFont().deriveFont(Font.BOLD, l.getFont().getSize2D() + 1f));
        return l;
    }

    private static String formatReviews(org.json.JSONArray reviewsArray, int maxReviews) {
        if (reviewsArray == null || reviewsArray.isEmpty()) {
            return "No reviews available.";
        }
        
        StringBuilder sb = new StringBuilder();
        int count = Math.min(maxReviews, reviewsArray.length());
        
        for (int i = 0; i < count; i++) {
            org.json.JSONObject review = reviewsArray.getJSONObject(i);
            String author = review.optString("author", "Anonymous");
            String content = review.optString("content", "");
            
            // Don't truncate - let scrolling handle long reviews
            sb.append(author).append(" - \"").append(content).append("\"");
            if (i < count - 1) {
                sb.append("\n\n");
            }
        }
        
        return sb.toString();
    }
    
    private void loadPosterImage(JLabel posterLabel, Movie movie) {
        // Load image in a background thread to avoid blocking the UI
        new Thread(() -> {
            try {
                String posterPath = movie.posterUrl();
                if (posterPath != null && !posterPath.isEmpty()) {
                    URL url = new URL(posterPath);
                    BufferedImage originalImage = ImageIO.read(url);
                    
                    if (originalImage != null) {
                        // Scale the image to fit the label
                        Image scaledImage = originalImage.getScaledInstance(
                            300, 420, Image.SCALE_SMOOTH);
                        
                        // Update UI on the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> {
                            posterLabel.setIcon(new ImageIcon(scaledImage));
                            posterLabel.setText(null); // Remove "Loading..." text
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> 
                            posterLabel.setText("Image not available"));
                    }
                } else {
                    SwingUtilities.invokeLater(() -> 
                        posterLabel.setText("No poster available"));
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> 
                    posterLabel.setText("Failed to load image"));
                e.printStackTrace();
            }
        }).start();
    }
}