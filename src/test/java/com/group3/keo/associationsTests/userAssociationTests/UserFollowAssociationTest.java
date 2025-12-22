package com.group3.keo.associationsTests.userAssociationTests;

import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserFollowAssociationTest {
    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
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
    }

    @Test
    void followingCreatesBidirectionalAssociation() {
        user1.follow(user2);
        Assertions.assertTrue(user1.getFollowing().contains(user2));
        Assertions.assertTrue(user2.getFollowers().contains(user1));
    }

    @Test
    void unfollowRemovesBidirectionalAssociation() {
        user1.follow(user2);
        user1.unfollow(user2);
        Assertions.assertFalse(user1.getFollowing().contains(user2));
        Assertions.assertFalse(user2.getFollowers().contains(user1));
    }

    @Test
    void userCannotFollowThemselves() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> user1.follow(user1));
    }
}
