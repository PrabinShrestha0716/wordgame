package uta.cse3310;

import java.util.ArrayList;
import java.util.List;

public class ChatBox {
    private List<String> messages;
    private static final int MAX_MESSAGES = 10;

    public ChatBox() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(String msg) {
        // Ensure the chatbox does not exceed maximum allowed messages
        if (messages.size() >= MAX_MESSAGES) {
            messages.remove(0); // Remove oldest message
        }
        messages.add(msg);
    }

    public void displayChat() {
        System.out.println("ChatBox Messages:");
        for (String msg : messages) {
            System.out.println(msg);
        }
    }
}
