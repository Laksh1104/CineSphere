package data_access;

import entity.User;
import entity.UserFactory;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * File-based DAO for user data.
 * Each line in file: username;password
 */
public class FileUserDataAccessObject implements
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private final File file;
    private final UserFactory userFactory;
    private String currentUsername;

    public FileUserDataAccessObject(String filePath, UserFactory userFactory) {
        this.file = new File(filePath);
        this.userFactory = userFactory;
        loadFromFile();
    }

    private void loadFromFile() {
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    String name = parts[0];
                    String password = parts[1];
                    users.put(name, userFactory.create(name, password));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read users file", e);
        }
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (User u : users.values()) {
                pw.println(u.getName() + ";" + u.getPassword());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users file", e);
        }
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
        saveToFile();
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }
}
