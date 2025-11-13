package view;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

/**
 * Standalone login / signup window for your part.
 * Run main() here to test auth flow without touching AppBuilder.
 */
public class LoginView extends JFrame {

    private final AuthService authService;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;

    public LoginView() {
        UserFactory userFactory = new UserFactory();
        FileUserDataAccessObject userDAO =
                new FileUserDataAccessObject("users.txt", userFactory);
        this.authService = new AuthService(userDAO, userFactory);

        setTitle("CineSphere - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Log in", buildLoginPanel());
        tabs.addTab("Sign up", buildSignupPanel());
        add(tabs);
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        loginUsernameField = new JTextField(20);
        loginPasswordField = new JPasswordField(20);

        JButton loginButton = new JButton("Log in");
        loginButton.addActionListener(e -> handleLogin());

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(loginUsernameField, c);

        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(loginPasswordField, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        panel.add(loginButton, c);

        return panel;
    }

    private JPanel buildSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        signupUsernameField = new JTextField(20);
        signupPasswordField = new JPasswordField(20);

        JButton signupButton = new JButton("Sign up");
        signupButton.addActionListener(e -> handleSignup());

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(signupUsernameField, c);

        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(signupPasswordField, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        panel.add(signupButton, c);

        return panel;
    }

    private void handleLogin() {
        String u = loginUsernameField.getText().trim();
        String p = new String(loginPasswordField.getPassword());
        try {
            authService.login(u, p);
            JOptionPane.showMessageDialog(this, "Login successful!");
            openLoggedInView();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Login failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignup() {
        String u = signupUsernameField.getText().trim();
        String p = new String(signupPasswordField.getPassword());
        try {
            authService.signUp(u, p);
            JOptionPane.showMessageDialog(this, "Account created!");
            openLoggedInView();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Sign up failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLoggedInView() {
        this.dispose();                    // close login window
        LoggedInView.main(new String[]{}); // open the logged-in screen
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
