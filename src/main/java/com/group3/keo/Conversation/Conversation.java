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
            if (participants.size() >= 2) {
                throw new IllegalStateException("Conversation cannot have more than 2 participants");
            }
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
        if (message.getConversation() != this) {
            throw new IllegalArgumentException("message.conversation must refer to this Conversation");
        }
        if (!messages.contains(message)) {
            messages.add(message);
        }
    }
}