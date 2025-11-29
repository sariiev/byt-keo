package com.group3.keo.conversation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.users.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class Conversation {
    // region === EXTENT ===
    private static final Map<UUID, Conversation> extent = new HashMap<>();
    // endregion

    // region === FIELDS ===
    private final UUID uid;
    private final Set<User> participants = new HashSet<>();
    private final List<Message> messages = new ArrayList<>();
    // endregion

    // region === CONSTRUCTORS ===
    public Conversation(User user1, User user2) {
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("Participants cannot be null");
        }
        if (user1.equals(user2)) {
            throw new IllegalArgumentException("Conversation requires two different users");
        }

        for (Conversation conversation : extent.values()) {
            Set<User> _participants = conversation.participants;
            if (_participants.contains(user1) && _participants.contains(user2)) {
                throw new IllegalStateException("Conversation between these two users already exists");
            }
        }

        this.uid = UUID.randomUUID();
        this.participants.add(user1);
        this.participants.add(user2);

        extent.put(uid, this);
    }

    private Conversation(UUID uid, Set<User> participants) {
        if (participants.size() != 2) {
            throw new IllegalArgumentException("Conversation must have exactly two participants");
        }

        User user1 = null;
        User user2 = null;
        for (User user : participants) {
            if (user1 == null) {
                user1 = user;
            } else {
                user2 = user;
            }
        }

        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("Participants cannot be null");
        }
        if (user1.equals(user2)) {
            throw new IllegalArgumentException("Conversation requires two different users");
        }

        for (Conversation conversation : extent.values()) {
            Set<User> _participants = conversation.participants;
            if (_participants.contains(user1) && _participants.contains(user2)) {
                throw new IllegalStateException("Conversation between these two users already exists");
            }
        }

        this.uid = uid;
        this.participants.addAll(new HashSet<>(participants));

        extent.put(uid, this);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    public Set<User> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }
    // endregion

    // region === MUTATORS ===
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
    // endregion

    // region === EQUALS && HASHCODE ===
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Conversation that)) return false;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
    // endregion

    // region === EXTENT ACCESS ===
    public static Map<UUID, Conversation> getExtent() {
        return Collections.unmodifiableMap(extent);
    }
    // endregion

    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<ConversationDTO> dtos = new ArrayList<>();
            for (Conversation conversation : extent.values()) {
                dtos.add(conversation.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<ConversationDTO>>(){}.getType();
            List<ConversationDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (ConversationDTO dto : loaded) {
                    fromDto(dto);
                }
            }
        } catch (Exception ex) {
            extent.clear();
            ex.printStackTrace();
        }
    }
    // endregion

    // region === DTO CONVERSION ===
    private ConversationDTO toDto() {
        ConversationDTO dto = new ConversationDTO();
        dto.uid = uid;
        dto.participants = new HashSet<>();
        for (User participant : participants) {
            dto.participants.add(participant.getUid());
        }
        return dto;
    }

    private static Conversation fromDto(ConversationDTO dto) {
        Set<User> participants = new HashSet<>();
        for (UUID userUid : dto.participants) {
            User participant = User.getExtent().get(userUid);
            if (participant == null) {
                throw new IllegalStateException("user not found for UID: " + userUid);
            }
            participants.add(participant);
        }

        return new Conversation(dto.uid, participants);
    }
    // endregion
}