package com.group3.keo.Conversation;

import com.group3.keo.MediaAttachments.Picture;
import com.group3.keo.Users.PersonalUser;
import com.group3.keo.Users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private PersonalUser sender;
    private Conversation conversation;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    public void setup() {
        sender = new PersonalUser("user13", "Michael Jackson", "bio", address, location);
        conversation = new Conversation();
        conversation.addParticipant(sender);
        conversation.addParticipant(new PersonalUser("kate2222", "Kate", "bio2", address, location));
    }

    @Test
    public void testMessageCreation() {
        LocalDateTime now = LocalDateTime.now();
        Message m = new Message(sender, conversation, "Hello!", now);
        assertEquals("Hello!", m.getCaption());
        assertEquals(sender, m.getSender());
        assertEquals(conversation, m.getConversation());
        assertEquals(now, m.getMessageDateTime());
        assertFalse(m.isDeleted());
        assertFalse(m.isRead());
        assertFalse(m.isWasEdited());
        assertTrue(conversation.getMessages().contains(m));
    }

    @Test
    public void testNullSenderException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Message(null, conversation, "hi", LocalDateTime.now()));
    }

    @Test
    public void testNullConversationException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Message(sender, null, "hi", LocalDateTime.now()));
    }

    @Test
    public void testNullDateException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Message(sender, conversation, "hi", null));
    }

    @Test
    public void testCaptionTooLongException() {
        String longCaption = "a".repeat(Message.MaximumCaptionLength + 1);

        assertThrows(IllegalArgumentException.class,
                () -> new Message(sender, conversation, longCaption, LocalDateTime.now()));
    }

    @Test
    public void testCaptionEdits() {
        Message m = new Message(sender, conversation, "Hello", LocalDateTime.now());
        m.setCaption("Hello world");
        assertTrue(m.isWasEdited());
    }

    @Test
    public void testNoCaptionIfAttachmentsExist() {
        Message m = new Message(sender, conversation, "Caption", LocalDateTime.now());
        assertThrows(IllegalStateException.class, () -> m.setCaption(null));
    }

    @Test
    public void testAddAttachment() {
        Message m = new Message(sender, conversation, "Hello", LocalDateTime.now());
        Picture pic = new Picture("file.png", 100, 200, 200, false);
        m.addAttachment(pic);
        assertEquals(1, m.getAttachments().size());
        assertTrue(m.getAttachments().contains(pic));
    }

    @Test
    public void testAttachmentNullException() {
        Message m = new Message(sender, conversation, "Hello", LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> m.addAttachment(null));
    }

    @Test
    public void testNoModifyAttachmentList() {
        Message m = new Message(sender, conversation, "Hello", LocalDateTime.now());
        Picture pic = new Picture("file.png", 100, 200, 200, false);
        m.addAttachment(pic);
        assertThrows(UnsupportedOperationException.class, () -> m.getAttachments().clear());
    }

    @Test
    public void testMarkRead() {
        Message m = new Message(sender, conversation, "Hello", LocalDateTime.now());
        m.markRead();
        assertTrue(m.isRead());
    }

    @Test
    public void testMarkDeleted() {
        Message m = new Message(sender, conversation, "Hello", LocalDateTime.now());
        m.markDeleted();
        assertTrue(m.isDeleted());
    }
}
