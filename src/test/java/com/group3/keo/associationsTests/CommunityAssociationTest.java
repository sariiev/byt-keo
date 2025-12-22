package com.group3.keo.associationsTests;

import com.group3.keo.community.Community;
import com.group3.keo.community.Role;
import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.enums.RoleType;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class CommunityAssociationTest {
    private PersonalUser user1;
    private PersonalUser user2;
    private Community community;

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
                throw new IllegalStateException("Unsupported extent type in " + clazz.getName());
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

        community = new Community(
                "Community",
                CommunityTopic.ANIMALS,
                null,
                user1
        );
    }

    @Test
    void creatorShouldBecomeHeadAutomatically() {
        Assertions.assertEquals(1, Role.getExtent().size());
        Assertions.assertTrue(user1.getRoles().stream()
                .anyMatch(r -> r.getRoleType() == RoleType.HEAD));
    }

    @Test
    void addingMemberCreatesRoleOnBothSides() {
        Role role = community.addMember(user2, RoleType.MEMBER);

        Assertions.assertTrue(Role.getExtent().contains(role));
        Assertions.assertEquals(role, community.getMembers().get(user2.getUsername()));
        Assertions.assertTrue(user2.getRoles().contains(role));
    }

    @Test
    void cannotAddSameUserTwice() {
        community.addMember(user2, RoleType.MEMBER);

        Assertions.assertThrows(IllegalStateException.class,
                () -> community.addMember(user2, RoleType.MEMBER));
    }

    @Test
    void removingMemberRemovesAllReferences() {
        Role role = community.addMember(user2, RoleType.MEMBER);

        community.removeMember(user2);

        Assertions.assertFalse(Role.getExtent().contains(role));
        Assertions.assertFalse(community.getMembers().containsKey(user2.getUsername()));
        Assertions.assertFalse(user2.getRoles().contains(role));
    }

    @Test
    void deletingCommunityRemovesAllRoles() {
        community.addMember(user2, RoleType.MEMBER);

        community.delete();

        Assertions.assertEquals(0, Role.getExtent().size());
        Assertions.assertEquals(0, community.getMembers().size());
    }

    @Test
    void usernameChangeUpdatesCommunityKey() {
        community.addMember(user2, RoleType.MEMBER);

        String oldUsername = user2.getUsername();
        user2.setUsername("newUser2");

        Assertions.assertNull(community.getMembers().get(oldUsername));
        Assertions.assertNotNull(community.getMembers().get("newUser2"));
    }
}
