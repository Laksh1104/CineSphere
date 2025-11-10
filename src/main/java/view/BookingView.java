package view;

import javax.swing.*;
import java.awt.*;

public class BookingView {
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

        buttonPanel.setBackground(new Color(255, 255, 224));
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

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(255, 255, 224));
        JLabel titleLabel = new JLabel("Booking Page");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        headerPanel.add(titleLabel);

        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerButtonPanel.setBackground(new Color(255, 255, 224));

        String[] movies = {"Movies", "YAYAYA", "SNNAN"};
        JComboBox<String> movieDropdown = new JComboBox<>(movies);
        headerButtonPanel.add(movieDropdown);

    }
}
