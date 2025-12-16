package com.group3.keo.associationsTests;

import com.group3.keo.publications.posts.PrivatePost;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.*;

import java.util.*;

public class PrivatePostAssociationTest {

    private PersonalUser author;
    private PersonalUser user;
    private PrivatePost post;

    @BeforeEach
    void setup() {
        author = new PersonalUser("author", "Author", "Bio1",
                new User.Address("C1", "C1", "A1"),
                new User.Location(1, 1)
        );

        user = new PersonalUser("user", "User", "Bio2",
                new User.Address("C2", "C2", "A2"),
                new User.Location(2, 2)
        );

        post = new PrivatePost(author, "caption", new ArrayList<>(), null);
    }

    @Test
    void authorIsAlwaysAllowed() {
        Assertions.assertTrue(post.getAllowedUsers().contains(author));
    }

    @Test
    void addingAllowedUserUpdatesBothSides() {
        post.addAllowedUser(user);

        Assertions.assertTrue(post.getAllowedUsers().contains(user));
        Assertions.assertTrue(user.getAccessiblePrivatePublications().contains(post));
    }

    @Test
    void removingAllowedUserUpdatesBothSides() {
        post.addAllowedUser(user);
        post.removeAllowedUser(user);

        Assertions.assertFalse(post.getAllowedUsers().contains(user));
        Assertions.assertFalse(user.getAccessiblePrivatePublications().contains(post));
    }

    @Test
    void authorCannotBeRemoved() {
        Assertions.assertThrows(IllegalStateException.class,
                () -> post.removeAllowedUser(author));
    }

    @Test
    void deletingPostRemovesAllUserAssociations() {
        post.addAllowedUser(user);

        post.delete();

        Assertions.assertFalse(user.getAccessiblePrivatePublications().contains(post));
    }
}
