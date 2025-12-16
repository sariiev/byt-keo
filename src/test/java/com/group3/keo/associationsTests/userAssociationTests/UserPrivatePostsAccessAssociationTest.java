package com.group3.keo.associationsTests.userAssociationTests;

import com.group3.keo.publications.posts.PrivatePost;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UserPrivatePostsAccessAssociationTest {
    private PersonalUser author;
    private PersonalUser user;
    private PrivatePost publication;

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

        publication = new PrivatePost(author, "caption", new ArrayList<>(), null);
    }

    @Test
    void addingAccessiblePrivatePublicationRegistersInUser() {
        user.addAccessiblePrivatePublicationInternal(publication);
        Assertions.assertTrue(
                user.getAccessiblePrivatePublications().contains(publication));
    }

    @Test
    void deletingUserRemovesUserFromPrivatePublication() {
        publication.addAllowedUser(user);
        user.delete();
        Assertions.assertFalse(publication.getAllowedUsers().contains(user));
    }
}

