package com.group3.keo.Conversation;

import com.group3.keo.Users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conversation { private final List<User> participants = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();

    public List<User> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public void addParticipant(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (!participants.contains(user)) {
            participants.add(user);
        }
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    void addMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        if (!messages.contains(message)) {
            messages.add(message);
        }
    }
}