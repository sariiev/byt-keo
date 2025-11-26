package com.group3.keo.conversation;

import java.util.List;
import java.util.UUID;

public class MessageDTO {
    public UUID uid;
    public UUID senderUid;
    public UUID conversationUid;

    public String caption;
    public String messageDateTime;

    public boolean isRead;
    public boolean wasEdited;
    public boolean isDeleted;

    public List<UUID> attachments;
}
