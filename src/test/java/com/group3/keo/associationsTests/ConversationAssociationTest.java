package com.group3.keo.associationsTests;

import com.group3.keo.conversation.Message;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.conversation.Conversation;
import com.group3.keo.users.User;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConversationAssociationTest {
    private PersonalUser user1;
    private PersonalUser user2;
    private PersonalUser user3;

    private static void clearClassExtent(Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField("extent");
            field.setAccessible(true);

            Object value = field.get(null);

            if (value instanceof Map<?, ?> map) {
                map.clear();
            } else if (value instanceof Set<?> set) {
                set.clear();
            } else {
                throw new IllegalStateException("Unsupported extent type in class: " + clazz.getName());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @BeforeEach
    void setup() {
        clearClassExtent(User.class);
        clearClassExtent(Conversation.class);
        clearClassExtent(Message.class);

        user1 = new PersonalUser(
                "user1", "User1", "Bio1",
                new User.Address("C1", "C1", "A1"),
                new User.Location(1, 1)
        );

        user2 = new PersonalUser(
                "user2", "User2", "Bio2",
                new User.Address("C2", "C2", "A2"),
                new User.Location(2, 2)
        );

        user3 = new PersonalUser(
                "user3", "User3", "Bio3",
                new User.Address("C3", "C3", "A3"),
                new User.Location(3, 3)
        );
    }

    @Test
    void creatingConversationShouldRegisterOnBothUsers() {
        Conversation conversation = new Conversation(user1, user2);

        Assertions.assertTrue(Conversation.getExtent().containsValue(conversation));
        Assertions.assertTrue(conversation.getParticipants().contains(user1));
        Assertions.assertTrue(conversation.getParticipants().contains(user2));

        Assertions.assertTrue(user1.getConversations().contains(conversation));
        Assertions.assertTrue(user2.getConversations().contains(conversation));
    }

    @Test
    void cannotCreateConversationWithSameUser() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Conversation(user1, user1));
    }

    @Test
    void cannotCreateDuplicateConversationBetweenSameUsers() {
        new Conversation(user1, user2);

        Assertions.assertThrows(IllegalStateException.class,
                () -> new Conversation(user1, user2));
    }

    @Test
    void userCanHaveMultipleConversations() {
        Conversation c1 = new Conversation(user1, user2);
        Conversation c2 = new Conversation(user1, user3);

        Assertions.assertEquals(2, user1.getConversations().size());
        Assertions.assertTrue(user1.getConversations().contains(c1));
        Assertions.assertTrue(user1.getConversations().contains(c2));
    }

    @Test
    void deletingConversationRemovesItFromUsers() {
        Conversation conversation = new Conversation(user1, user2);

        conversation.delete();

        Assertions.assertFalse(Conversation.getExtent().containsValue(conversation));
        Assertions.assertFalse(user1.getConversations().contains(conversation));
        Assertions.assertFalse(user2.getConversations().contains(conversation));
    }

    @Test
    void addingMessageCreatesAssociationOnBothSides() {
        Conversation conversation = new Conversation(user1, user2);

        Message message = conversation.addMessage(user1, "Hello", List.of());

        Assertions.assertTrue(Message.getExtent().containsKey(message.getUid()));
        Assertions.assertTrue(conversation.getMessages().contains(message));
        Assertions.assertEquals(conversation, message.getConversation());
    }

    @Test
    void nonParticipantCannotSendMessage() {
        Conversation conversation = new Conversation(user1, user2);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> conversation.addMessage(user3, "Hi", List.of()));
    }

    @Test
    void removingMessageRemovesAllReferences() {
        Conversation conversation = new Conversation(user1, user2);
        Message message = conversation.addMessage(user1, "Hello", List.of());

        conversation.removeMessage(message);

        Assertions.assertFalse(conversation.getMessages().contains(message));
        Assertions.assertFalse(Message.getExtent().containsKey(message.getUid()));
    }

    @Test
    void deletingConversationDeletesAllMessages() {
        Conversation conversation = new Conversation(user1, user2);
        conversation.addMessage(user1, "Hello", List.of());
        conversation.addMessage(user2, "Hi", List.of());

        conversation.delete();

        Assertions.assertEquals(0, Message.getExtent().size());
        Assertions.assertEquals(0, conversation.getMessages().size());
    }
}
