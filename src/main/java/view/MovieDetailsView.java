package view;

import entity.Movie;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MovieDetailsView extends JFrame {

    public MovieDetailsView(Movie movie) {
        super(movie.getFilmName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(16, 16));
        getContentPane().setBackground(new Color(0xFFFBE6));
        getRootPane().setBorder(new EmptyBorder(16, 16, 16, 16));

        // ---- Header (Title • Date • Directed by) ----
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        header.setOpaque(false);
        JLabel title = bold(new JLabel(movie.getFilmName()));
        JLabel date = new JLabel(" –  (Year placeholder)  –  ");
        JLabel director = new JLabel("Directed by: (placeholder)");
        header.add(title);
        header.add(date);
        header.add(director);
        add(header, BorderLayout.NORTH);

        // ---- Center (Poster left, facts & button right) ----
        JPanel center = new JPanel(new BorderLayout(16, 0));
        center.setOpaque(false);

        // Poster placeholder
        JLabel poster = new JLabel("Movie Image", SwingConstants.CENTER);
        poster.setPreferredSize(new Dimension(300, 420));
        poster.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        center.add(poster, BorderLayout.WEST);

        // Facts + Add to Watchlist
        Box facts = Box.createVerticalBox();
        facts.setOpaque(false);
        facts.add(new JLabel("Rating: (placeholder)"));
        facts.add(Box.createVerticalStrut(6));
        facts.add(new JLabel("Genres: (placeholder)"));
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
                "Description: (placeholder for " + movie.getFilmName() + ")");
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel reviewsHeader = bold(new JLabel("Popular Reviews:"));
        JTextArea reviews = new JTextArea(
                "user1 — \"Review placeholder...\"\n\nuser2 — \"Review placeholder...\"");
        reviews.setLineWrap(true);
        reviews.setWrapStyleWord(true);
        reviews.setEditable(false);
        reviews.setBorder(new EmptyBorder(8, 8, 8, 8));

        bottom.add(desc);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(reviewsHeader);
        bottom.add(reviews);

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
}