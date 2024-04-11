package uta.cse3310;

import java.util.List;

public class Message {
    private String type;
    private String username;
    private List<String> activeUsers;
    private String message; // Field for chat messages

    // Constructors for different purposes
    public Message(String type, String username) {
        this.type = type;
        this.username = username;
    }

    public Message(String type, List<String> activeUsers) {
        this.type = type;
        this.activeUsers = activeUsers;
    }

    public Message(String type, String username, String message) {
        this.type = type;
        this.username = username;
        this.message = message;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<String> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
