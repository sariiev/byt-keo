package com.group3.keo.associationsTests.userAssociationTests;

import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserLikeAssociationTest {

    private User user;
    private PublicationBase publication;

    @BeforeEach
    void setup() {
        user = new PersonalUser("user", "User", "Bio",
                new User.Address("A", "B", "C"),
                new User.Location(1, 1));
        publication = new PublicPost(user, "Post", null);
    }

    @Test
    void likingPublicationRegistersBidirectionally() {
        user.likePublication(publication);

        Assertions.assertTrue(user.getLikedPublications().contains(publication));
        Assertions.assertTrue(publication.getLikedBy().contains(user));
    }

    @Test
    void unlikingRemovesAssociation() {
        user.likePublication(publication);
        user.unlikePublication(publication);

        Assertions.assertFalse(user.getLikedPublications().contains(publication));
        Assertions.assertFalse(publication.getLikedBy().contains(user));
    }
}

