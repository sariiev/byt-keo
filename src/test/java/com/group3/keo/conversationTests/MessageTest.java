package com.group3.keo.conversationTests;

import com.group3.keo.conversation.Conversation;
import com.group3.keo.conversation.Message;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {
    private PersonalUser sender;
    private PersonalUser receiver;
    private Conversation conversation;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    public MessageTest() {
    }

    private void clearMessageExtent() {
        try {
            Field field = Message.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    @BeforeEach
    public void setup() {
        this.sender = new PersonalUser("user13", "Michael Jackson", "bio", this.address, this.location);
        this.receiver = new PersonalUser("kate2222", "Kate", "bio2", this.address, this.location);
        this.conversation = new Conversation(this.sender, this.receiver);
        this.clearMessageExtent();
    }

    @Test
    public void testMessageCreation() {
        Message m = new Message(this.sender, this.conversation, "Hello!", new ArrayList());
        Assertions.assertEquals(this.sender, m.getSender());
        Assertions.assertEquals(this.conversation, m.getConversation());
        Assertions.assertEquals("Hello!", m.getCaption());
        Assertions.assertNotNull(m.getUid());
        Assertions.assertFalse(m.isDeleted());
        Assertions.assertFalse(m.isRead());
        Assertions.assertTrue(this.conversation.getMessages().contains(m));
    }

    @Test
    public void testNullSenderException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Message((User)null, this.conversation, "hi", new ArrayList());
        });
    }

    @Test
    public void testNullConversationException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Message(this.sender, (Conversation)null, "hi", new ArrayList());
        });
    }

    @Test
    public void testCaptionTooLongException() {
        String longCaption = "a".repeat(2501);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Message(this.sender, this.conversation, longCaption, new ArrayList());
        });
    }

    @Test
    public void testCaptionEdits() {
        Message m = new Message(this.sender, this.conversation, "Hello", new ArrayList());
        m.setCaption("Hello world");
        Assertions.assertTrue(m.isWasEdited());
    }

    @Test
    public void testNoCaptionIfAttachmentsExist() {
        Message m = new Message(this.sender, this.conversation, "Caption", new ArrayList());
        Assertions.assertThrows(IllegalStateException.class, () -> {
            m.setCaption((String)null);
        });
    }

    @Test
    public void testAddAttachment() {
        Message m = new Message(this.sender, this.conversation, "Hello", new ArrayList());
        Picture pic = new Picture("file.png", 100, 200, 200, false);
        m.addAttachment(pic);
        Assertions.assertEquals(1, m.getAttachments().size());
        Assertions.assertTrue(m.getAttachments().contains(pic));
    }

    @Test
    public void testAttachmentNullException() {
        Message m = new Message(this.sender, this.conversation, "Hello", new ArrayList());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            m.addAttachment((MediaAttachment)null);
        });
    }

    @Test
    public void testNoModifyAttachmentList() {
        Message m = new Message(this.sender, this.conversation, "Hello", new ArrayList());
        Picture pic = new Picture("file.png", 100, 200, 200, false);
        m.addAttachment(pic);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            m.getAttachments().clear();
        });
    }

    @Test
    public void testMarkRead() {
        Message m = new Message(this.sender, this.conversation, "Hello", new ArrayList());
        m.markRead();
        Assertions.assertTrue(m.isRead());
    }

    @Test
    public void testMarkDeleted() {
        Message m = new Message(this.sender, this.conversation, "Hello", new ArrayList());
        m.markDeleted();
        Assertions.assertTrue(m.isDeleted());
    }

    @Test
    public void testEncapsulation() {
        Message m = new Message(this.sender, this.conversation, "Hello", new ArrayList());
        String externalCaption = "Different";
        externalCaption = "Another";
        Assertions.assertNotEquals(externalCaption, m.getCaption(), "Changing local variable should not affect Message object");
    }

    @Test
    public void testExtentContainsMessage() {
        Message m = new Message(this.sender, this.conversation, "Hi", new ArrayList());
        Map<?, ?> extent = Message.getExtent();
        Assertions.assertEquals(1, extent.size());
        Assertions.assertTrue(extent.containsKey(m.getUid()));
    }
}
