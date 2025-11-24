package com.group3.keo.Conversation;

import com.group3.keo.Users.PersonalUser;
import com.group3.keo.Users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ConversationTest {
    private Conversation conversation;
    private PersonalUser user1;
    private PersonalUser user2;
    private PersonalUser user3;

    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    public void setup() {
        conversation = new Conversation();
        user1 = new PersonalUser("john", "John", "bio1", address, location);
        user2 = new PersonalUser("kate", "Kate", "bio2", address, location);
        user3 = new PersonalUser("mike", "Mike", "bio3", address, location);
    }

    @Test
    void testAddParticipant() {
        conversation.addParticipant(user1);
        conversation.addParticipant(user2);

        assertEquals(2, conversation.getParticipants().size());
        assertTrue(conversation.getParticipants().contains(user1));
        assertTrue(conversation.getParticipants().contains(user2));
    }

    @Test
    public void testAddNullParticipantException() {
        assertThrows(IllegalArgumentException.class, () -> conversation.addParticipant(null));
    }

    @Test
    public void testDuplicateParticipants() {
        conversation.addParticipant(user1);
        conversation.addParticipant(user1);
        assertEquals(1, conversation.getParticipants().size());
    }

    @Test
    public void testNotMoreThanTwoParticipants() {
        conversation.addParticipant(user1);
        conversation.addParticipant(user2);
        assertThrows(IllegalStateException.class, () -> conversation.addParticipant(user3));
    }

    @Test
    public void testParticipantsListUnmodifiable() {
        conversation.addParticipant(user1);
        List<User> list = conversation.getParticipants();
        assertThrows(UnsupportedOperationException.class, list::clear);
    }

    @Test
    public void testAddMessage() {
        conversation.addParticipant(user1);
        conversation.addParticipant(user2);
        Message m = new Message(user1, conversation, "Hello", LocalDateTime.now());
        assertTrue(conversation.getMessages().contains(m));
        assertEquals(1, conversation.getMessages().size());
    }

    @Test
    public void testAddMessageNullException() {
        assertThrows(IllegalArgumentException.class, () -> conversation.addMessage(null));
    }

    @Test
    public void testAddMessageFromDifferentConversationThrows() {
        conversation.addParticipant(user1);
        conversation.addParticipant(user2);
        Conversation otherConvo = new Conversation();
        otherConvo.addParticipant(user1);
        otherConvo.addParticipant(user2);
        Message m = new Message(user1, otherConvo, "Hi", LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> conversation.addMessage(m));
    }

    @Test
    public void testMessageListUnmodifiable() {
        conversation.addParticipant(user1);
        conversation.addParticipant(user2);
        //Message m = new Message(user1, conversation, "Hello", LocalDateTime.now());
        List<Message> messages = conversation.getMessages();
        assertThrows(UnsupportedOperationException.class, messages::clear);
    }

    @Test
    public void testMessageNotAddedTwice() {
        conversation.addParticipant(user1);
        conversation.addParticipant(user2);
        Message m = new Message(user1, conversation, "Hello", LocalDateTime.now());
        conversation.addMessage(m);
        conversation.addMessage(m);
        assertEquals(1, conversation.getMessages().size());
    }
}
