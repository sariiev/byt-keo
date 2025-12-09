package com.group3.keo.associationsTests;

import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.publications.comments.Comment;
import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.publications.quotes.Quote;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class PublicationAssociationTest {
    private PersonalUser user;
    private PublicPost post1;
    private PublicPost post2;

    private static void clearClassExtent(Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField("extent");
            field.setAccessible(true);

            Object value = field.get(null);

            if (value instanceof Map<?,?> map) {
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
        clearClassExtent(PublicationBase.class);
        clearClassExtent(User.class);

        user = new PersonalUser("user", "User", "Bio", new User.Address("A", "B", "C"), new User.Location(1, 1));

        post1 = new PublicPost(user, "Post", null);
        post2 = new PublicPost(user, "Post 2", null);
    }

    // region === COMMENTS ===
    @Test
    void commentConstructorShouldRegisterAssociation() {
        Comment comment = new Comment(user, "Comment", post1, null);

        Assertions.assertEquals(1, post1.getComments().size());
        Assertions.assertEquals(post1, comment.getCommentedPublication());
    }

    @Test
    void commentDeleteShouldRemoveFromPublication() {
        Comment comment = new Comment(user, "Comment", post1, null);

        comment.delete();

        Assertions.assertEquals(0, post1.getComments().size());
        Assertions.assertEquals(2, PublicationBase.getExtent().size());
        Assertions.assertNull(comment.getCommentedPublication());
    }

    @Test
    void postDeleteShouldRemoveAllComments() {
        Comment comment1 = new Comment(user, "Comment1", post1, null);
        Comment comment2 = new Comment(user, "Comment2", post1, null);

        post1.delete();

        Assertions.assertEquals(1, PublicationBase.getExtent().size());
    }
    // endregion

    // region === QUOTES ===
    @Test
    void quoteConstructorShouldRegisterAssociation() {
        Quote quote = new Quote(user, "Quote", null, post1);

        Assertions.assertEquals(1, post1.getQuotes().size());
        Assertions.assertEquals(post1, quote.getReferencedPublication());
    }

    @Test
    void quoteCanChangeReferencedPublication() {
        Quote quote = new Quote(user, "Quote", null, post1);

        quote.setReferencedPublication(post2);

        Assertions.assertFalse(post1.getQuotes().contains(quote));
        Assertions.assertTrue(post2.getQuotes().contains(quote));
        Assertions.assertEquals(post2, quote.getReferencedPublication());
    }

    @Test
    void quoteDetachShouldRemoveFromPublication() {
        Quote quote = new Quote(user, "Quote", null, post1);

        quote.detachFromReferencedPublication();

        Assertions.assertEquals(0, post1.getQuotes().size());
        Assertions.assertEquals(3, PublicationBase.getExtent().size());
        Assertions.assertNull(quote.getReferencedPublication());
    }

    @Test
    void quoteDeleteShouldRemoveFromPublication() {
        Quote quote = new Quote(user, "Quote", null, post1);

        quote.delete();

        Assertions.assertEquals(0, post1.getQuotes().size());
        Assertions.assertEquals(2, PublicationBase.getExtent().size());
        Assertions.assertNull(quote.getReferencedPublication());
    }

    @Test
    void deletingPostShouldNullifyQuotesReference() {
        Quote quote = new Quote(user, "Quote", null, post1);

        post1.delete();

        Assertions.assertNull(quote.getReferencedPublication());
        Assertions.assertTrue(PublicationBase.getExtent().containsKey(quote.getUid()));
    }
    // endregion
}
