package uta.cse3310;
public class UserAction {
    String action;
    String username;

    // Constructor
    public UserAction(String action, String username) {
        this.action = action;
        this.username = username;
    }

    // Getters and Setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
