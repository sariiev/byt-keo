package com.group3.keo.conversationTests;

import com.group3.keo.conversation.Conversation;
import com.group3.keo.conversation.Message;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConversationTest {
    private PersonalUser user1;
    private PersonalUser user2;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    public ConversationTest() {
    }

    private void clearConversationExtent() {
        try {
            Field field = Conversation.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    @BeforeEach
    public void setup() {
        this.user1 = new PersonalUser("john", "John", "bio1", this.address, this.location);
        this.user2 = new PersonalUser("kate", "Kate", "bio2", this.address, this.location);
        this.clearConversationExtent();
    }

    @Test
    public void testConversationInitialization() {
        Conversation c = new Conversation(this.user1, this.user2);
        Assertions.assertNotNull(c.getUid());
        Assertions.assertEquals(Set.of(this.user1, this.user2), c.getParticipants());
    }

    @Test
    public void testNullUser() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Conversation((User)null, this.user2);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Conversation(this.user1, (User)null);
        });
    }

    @Test
    public void testSameUser() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Conversation(this.user1, this.user1);
        });
    }

    @Test
    public void testDuplicateConversation() {
        new Conversation(this.user1, this.user2);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            new Conversation(this.user1, this.user2);
        });
    }

    @Test
    public void testAddMessage() {
        Conversation c = new Conversation(this.user1, this.user2);
        Message m = new Message(this.user1, c, "Hello", new ArrayList());
        Assertions.assertEquals(1, c.getMessages().size());
        Assertions.assertTrue(c.getMessages().contains(m));
    }

    @Test
    void testAddNullMessage() {
        Conversation c = new Conversation(this.user1, this.user2);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            new Message(this.user1, c, (String)null, (List)null);
        });
    }
}
