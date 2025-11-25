package com.group3.keo.Conversation;

import com.group3.keo.MediaAttachments.MediaAttachment;
import com.group3.keo.Users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {

    public static final int MaximumCaptionLength = 2500;

    private String caption;
    private final LocalDateTime messageDateTime;

    private boolean isRead;
    private boolean wasEdited;
    private boolean isDeleted;

    private final List<MediaAttachment> attachments = new ArrayList<>();
    private final Conversation conversation;
    private final User sender;

    public Message(User sender,
                   Conversation conversation,
                   String caption,
                   LocalDateTime messageDateTime) {

        if (sender == null) {
            throw new IllegalArgumentException("sender cannot be null");
        }
        if (conversation == null) {
            throw new IllegalArgumentException("conversation cannot be null");
        }
        if (messageDateTime == null) {
            throw new IllegalArgumentException("messageDateTime cannot be null");
        }

        this.sender = sender;
        this.conversation = conversation;
        this.messageDateTime = messageDateTime;

        setCaption(caption);
        conversation.addMessage(this);
    }

    private boolean hasNonEmptyCaption() {
        return caption != null && !caption.isBlank();
    }

    private void ensureContentNotEmpty() {
        if (!hasNonEmptyCaption() && attachments.isEmpty()) {
            throw new IllegalStateException(
                    "Message must have a non-empty caption or at least one attachment."
            );
        }
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        if (caption == null) {
            this.caption = null;
            ensureContentNotEmpty();
            return;
        }

        String trimmed = caption.trim();
        if (trimmed.isEmpty()) {
            this.caption = null;
            ensureContentNotEmpty();
            return;
        }

        if (trimmed.length() > MaximumCaptionLength) {
            throw new IllegalArgumentException(
                    "Caption length must be ≤ " + MaximumCaptionLength
            );
        }

        if (this.caption != null && !trimmed.equals(this.caption)) {
            this.wasEdited = true;
        }

        this.caption = trimmed;
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

    public void addAttachment(MediaAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("attachment cannot be null");
        }
        attachments.add(attachment);
    }

    public Conversation getConversation() {
        return conversation;
    }

    public User getSender() {
        return sender;
    }
}
