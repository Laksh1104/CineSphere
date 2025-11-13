package service;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;

/**
 * Simple auth service for login / signup / logout.
 */
public class AuthService {

    private final FileUserDataAccessObject userDAO;
    private final UserFactory userFactory;

    public AuthService(FileUserDataAccessObject userDAO, UserFactory userFactory) {
        this.userDAO = userDAO;
        this.userFactory = userFactory;
    }

    public void signUp(String username, String password) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            throw new IllegalArgumentException("All fields are required.");
        }
        if (userDAO.existsByName(username)) {
            throw new IllegalArgumentException("Account already exists.");
        }
        User user = userFactory.create(username, password);
        userDAO.save(user);
        userDAO.setCurrentUsername(username);
    }

    public void login(String username, String password) {
        if (!userDAO.existsByName(username)) {
            throw new IllegalArgumentException("Invalid username/password.");
        }
        User user = userDAO.get(username);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username/password.");
        }
        userDAO.setCurrentUsername(username);
    }

    public void logout() {
        userDAO.setCurrentUsername(null);
    }

    public User getCurrentUser() {
        String name = userDAO.getCurrentUsername();
        return name == null ? null : userDAO.get(name);
    }
}
