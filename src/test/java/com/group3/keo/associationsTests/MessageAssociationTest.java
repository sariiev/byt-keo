package com.group3.keo.associationsTests;

import com.group3.keo.conversation.Conversation;
import com.group3.keo.conversation.Message;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageAssociationTest {
    private PersonalUser user1;
    private PersonalUser user2;
    private Conversation conversation;

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
                throw new IllegalStateException("Unsupported type of extent in class: " + clazz.getName());
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

        conversation = new Conversation(user1, user2);
    }

    @Test
    void creatingMessageShouldRegisterInConversationAndExtent() {
        Message message = conversation.addMessage(user1, "Hello", List.of());

        Assertions.assertTrue(Message.getExtent().containsValue(message));
        Assertions.assertTrue(conversation.getMessages().contains(message));
        Assertions.assertEquals(conversation, message.getConversation());
    }

    @Test
    void deletingMessageShouldRemoveItFromConversationAndExtent() {
        Message message = conversation.addMessage(user1, "Hello", List.of());

        message.delete();

        Assertions.assertFalse(Message.getExtent().containsValue(message));
        Assertions.assertFalse(conversation.getMessages().contains(message));
    }

    @Test
    void senderMustBeConversationParticipant() {
        PersonalUser outsider = new PersonalUser(
                "outsider", "Out", "Bio",
                new User.Address("C3", "C3", "A3"),
                new User.Location(3, 3)
        );

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> conversation.addMessage(outsider, "Hi", List.of()));
    }

    @Test
    void messageShouldKeepCorrectSenderReference() {
        Message message = conversation.addMessage(user2, "Hi", List.of());
        Assertions.assertEquals(user2, message.getSender());
    }

    @Test
    void messageCannotBeEmpty() {
        Assertions.assertThrows(IllegalStateException.class,
                () -> conversation.addMessage(user1, null, List.of()));
    }

    @Test
    void messageCanContainOnlyAttachments() {
        MediaAttachment attachment = new Picture("image.png", 100, 800, 600, true);
        Message message = conversation.addMessage(user1, null, List.of(attachment));
        Assertions.assertTrue(conversation.getMessages().contains(message));
    }

    @Test
    void deletingConversationShouldDeleteAllMessages() {
        conversation.addMessage(user1, "Hello", List.of());
        conversation.addMessage(user2, "Hi", List.of());

        conversation.delete();

        Assertions.assertEquals(0, Message.getExtent().size());
        Assertions.assertEquals(0, conversation.getMessages().size());
    }
}
