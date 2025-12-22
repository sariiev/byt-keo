package com.group3.keo.associationsTests;

import com.group3.keo.community.Community;
import com.group3.keo.community.Role;
import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.enums.RoleType;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class RoleAssociationTest {
    private PersonalUser user1;
    private PersonalUser user2;
    private Community community1;
    private Community community2;

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
        clearClassExtent(User.class);
        clearClassExtent(Community.class);
        clearClassExtent(Role.class);

        user1 = new PersonalUser("user1", "User1", "Bio1", new User.Address("C1", "C1", "A1"), new User.Location(1, 1));
        user2 = new PersonalUser("user2", "User2", "Bio2", new User.Address("C2", "C2", "A2"), new User.Location(2, 2));

        community1 = new Community("Community1", CommunityTopic.ANIMALS, null, user1); ////
        community2 = new Community("Community2", CommunityTopic.ART, null, user2); /////
    }

    @Test
    void userJoinCommunityShouldCreateRoleInBothSides() {
        Role role = user2.joinCommunity(community1, RoleType.MEMBER);

        Assertions.assertTrue(Role.getExtent().contains(role));
        Assertions.assertEquals(role, community1.getMembers().get(user2.getUsername()));
        Assertions.assertTrue(user2.getRoles().contains(role));
    }

    @Test
    void communityAddMemberShouldCreateRoleInBothSides() {
        Role role = community1.addMember(user2, RoleType.MEMBER);

        Assertions.assertTrue(Role.getExtent().contains(role));
        Assertions.assertEquals(role, community1.getMembers().get(user2.getUsername()));
        Assertions.assertTrue(user2.getRoles().contains(role));
    }

    @Test
    void cannotAddTheSameUserTwice() {
        Role role = community1.addMember(user2, RoleType.MEMBER);

        Assertions.assertThrows(IllegalStateException.class,
                () -> {
                    community1.addMember(user2, RoleType.MEMBER);
                }
        );
    }

    @Test
    void userCanJoinMultipleCommunities() {
        user1.joinCommunity(community2, RoleType.MEMBER);

        Assertions.assertEquals(2, user1.getRoles().size());
        Assertions.assertEquals(1, community1.getMembers().size());
        Assertions.assertEquals(2, community2.getMembers().size());
    }

    @Test
    void removingMemberFromCommunityShouldRemoveAllReferences() {
        Role role = community1.addMember(user2, RoleType.MEMBER);

        community1.removeMember(user2);

        Assertions.assertFalse(Role.getExtent().contains(role));
        Assertions.assertFalse(community1.getMembers().containsKey(user2.getUsername()));
        Assertions.assertFalse(user2.getRoles().contains(role));
    }

    @Test
    void userLeaveCommunityShouldRemoveAllReferences() {
        Role role = user2.joinCommunity(community1, RoleType.MEMBER);

        user2.leaveCommunity(community1);

        Assertions.assertFalse(Role.getExtent().contains(role));
        Assertions.assertFalse(community1.getMembers().containsKey(user2.getUsername()));
        Assertions.assertFalse(user2.getRoles().contains(role));
    }

    @Test
    void deletingUserShouldRemoveTheirRoles() {
        user1.joinCommunity(community2, RoleType.MEMBER);

        user1.delete();

        Assertions.assertEquals(1, Role.getExtent().size());
        Assertions.assertEquals(0, community1.getMembers().size());
        Assertions.assertEquals(1, community2.getMembers().size());
    }

    @Test
    void deletingCommunityShouldRemoveCommunityRoles() {
        community1.addMember(user2, RoleType.MEMBER);
        community1.delete();

        Assertions.assertEquals(1, Role.getExtent().size());
        Assertions.assertEquals(0, community1.getMembers().size());
    }

    @Test
    void usersUsernameChangeShouldUpdateCommunityKey() {
        community1.addMember(user2, RoleType.MEMBER);
        String oldUsername = user2.getUsername();
        user2.setUsername("newuser2");

        Assertions.assertNull(community1.getMembers().get(oldUsername));
        Assertions.assertNotNull(community1.getMembers().get(user2.getUsername()));
    }
}
