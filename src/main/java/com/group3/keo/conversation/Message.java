package com.group3.keo.conversation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.users.User;
import com.group3.keo.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class Message {
    // region === CONSTANTS ===
    public static final int MAX_CAPTION_LENGTH = 2500;
    public static final int MAX_ATTACHMENTS_SIZE = 10;
    // endregion

    // region === EXTENT ===
    private static final Map<UUID, Message> extent = new HashMap<>();
    // endregion

    // region === FIELDS ===
    private final UUID uid;
    private String caption;
    private final LocalDateTime messageDateTime;

    private boolean isRead;
    private boolean wasEdited;
    private boolean isDeleted;

    private final List<MediaAttachment> attachments = new ArrayList<>();
    private final Conversation conversation;
    private final User sender;
    // endregion

    // region === CONSTRUCTORS ===
    public Message(User sender,
                   Conversation conversation,
                   String caption,
                   List<MediaAttachment> attachments) {

        if (sender == null) {
            throw new IllegalArgumentException("sender cannot be null");
        }
        if (conversation == null) {
            throw new IllegalArgumentException("conversation cannot be null");
        }

        uid = UUID.randomUUID();
        this.sender = sender;
        this.conversation = conversation;
        this.messageDateTime = LocalDateTime.now();

        addAttachments(attachments);
        setCaption(caption);

        ensureContentNotEmpty();

        conversation.addMessageInternal(this);
        extent.put(uid, this);
    }

    private Message(UUID uid,
                   User sender,
                   Conversation conversation,
                   String caption,
                   List<MediaAttachment> attachments,
                   LocalDateTime messageDateTime,
                   boolean isRead,
                   boolean wasEdited,
                   boolean isDeleted) {

        if (sender == null) {
            throw new IllegalArgumentException("sender cannot be null");
        }
        if (conversation == null) {
            throw new IllegalArgumentException("conversation cannot be null");
        }

        this.uid = uid;
        this.sender = sender;
        this.conversation = conversation;
        this.messageDateTime = messageDateTime;
        this.isRead = isRead;
        this.wasEdited = wasEdited;
        this.isDeleted = isDeleted;

        addAttachments(attachments);
        setCaption(caption);

        ensureContentNotEmpty();

        conversation.addMessageInternal(this);
        extent.put(uid, this);
    }
    // endregion

    // region === HELPERS ===
    private boolean hasCaption() {
        return caption != null && !caption.isBlank();
    }

    private void ensureContentNotEmpty() {
        if (!hasCaption() && attachments.isEmpty()) {
            throw new IllegalStateException("Message must have a non-empty caption or at least one attachment");
        }
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        String oldCaption = this.caption;

        if (caption == null || caption.trim().isEmpty()) {
            if (attachments.isEmpty()) {
                throw new IllegalStateException("Message must have a non-empty caption or at least one attachment");
            }

            this.caption = null;

            if (oldCaption != null) {
                wasEdited = true;
            }

            return;
        }

        Utils.validateMaxLength(caption, "caption", MAX_CAPTION_LENGTH);

        String trimmed = caption.trim();

        this.caption = trimmed;

        if (!trimmed.equals(oldCaption)) {
            this.wasEdited = true;
        }
    }

    public LocalDateTime getMessageDateTime() {
        return messageDateTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void markRead() {
        this.isRead = true;
    }

    public boolean isWasEdited() {
        return wasEdited;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void markDeleted() {
        this.isDeleted = true;
    }

    public List<MediaAttachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public Conversation getConversation() {
        return conversation;
    }

    public User getSender() {
        return sender;
    }
    // endregion

    // region === MUTATORS ===
    private void addAttachments(List<MediaAttachment> attachments) {
        if (attachments == null) return;

        for (MediaAttachment attachment : attachments) {
            addAttachment(attachment);
        }
    }

    public void addAttachment(MediaAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("attachment cannot be null");
        }

        if (attachments.size() >= MAX_ATTACHMENTS_SIZE) {
            throw new IllegalStateException("Message cannot have more than " + MAX_ATTACHMENTS_SIZE + " attachments");
        }

        attachments.add(attachment);
    }

    public void removeAttachment(MediaAttachment attachment) {
        if (attachment == null || !attachments.contains(attachment)) {
            return;
        }

        if (!hasCaption() && attachments.size() == 1) {
            throw new IllegalStateException("Message must have a non-empty caption or at least one attachment");
        }

        attachments.remove(attachment);
    }

    public static Message sendMessage(User sender, Conversation conversation, String caption, List<MediaAttachment> attachments) {
        return new Message(sender, conversation, caption, attachments);
    }

    public void editMessage(String newCaption) {
        if (isDeleted) {
            throw new IllegalStateException("Cannot edit a deleted message");
        }
        setCaption(newCaption);
    }

    public void readMessage() {
        if (!isDeleted) {
            this.isRead = true;
        }
    }

    public void deleteMessage() {
        this.isDeleted = true;
    }

    void removeFromConversationInternal() {
        extent.remove(this.uid);
    }

    public void delete() {
        if (conversation != null) {
            conversation.removeMessageInternal(this);
        }
        extent.remove(this.uid);
    }
    // endregion

    // region === EQUALS & HASHCODE ===
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message message)) return false;
        return Objects.equals(uid, message.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
    // endregion

    // region === EXTENT ACCESS ===
    public static Map<UUID, Message> getExtent() {
        return Collections.unmodifiableMap(extent);
    }
    // endregion

    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<MessageDTO> dtos = new ArrayList<>();
            for (Message message : extent.values()) {
                dtos.add(message.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<MessageDTO>>(){}.getType();
            List<MessageDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (MessageDTO dto : loaded) {
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
    private MessageDTO toDto() {
        MessageDTO dto = new MessageDTO();
        dto.uid = uid;
        dto.senderUid = sender.getUid();
        dto.conversationUid = conversation.getUid();
        dto.caption = caption;
        dto.messageDateTime = messageDateTime.toString();
        dto.isRead = isRead;
        dto.wasEdited = wasEdited;
        dto.isDeleted = isDeleted;

        dto.attachments = new ArrayList<>();
        for (MediaAttachment attachment : attachments) {
            dto.attachments.add(attachment.getUid());
        }

        return dto;
    }

    private static Message fromDto(MessageDTO dto) {
        User sender = User.getExtent().get(dto.senderUid);
        if (sender == null) {
            throw new IllegalStateException("user not found for UID: " + dto.senderUid);
        }
        Conversation conversation = Conversation.getExtent().get(dto.conversationUid);
        if (conversation == null) {
            throw new IllegalStateException("conversation not found for UID: " + dto.conversationUid);
        }

        List<MediaAttachment> attachments = new ArrayList<>();
        if (dto.attachments != null) {
            for (UUID attachmentUid : dto.attachments) {
                MediaAttachment attachment = MediaAttachment.getExtent().get(attachmentUid);
                if (attachment == null) {
                    throw new IllegalStateException("media attachment not found for UID: " + attachmentUid);
                }
                attachments.add(attachment);
            }
        }

        LocalDateTime messageDateTime = LocalDateTime.parse(dto.messageDateTime);

        return new Message(
                dto.uid,
                sender,
                conversation,
                dto.caption,
                attachments,
                messageDateTime,
                dto.isRead,
                dto.wasEdited,
                dto.isDeleted
        );
    }
    // endregion
}
