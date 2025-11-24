package com.group3.keo.Community;

import com.group3.keo.Users.PersonalUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import com.group3.keo.MediaAttachments.Picture;
import com.group3.keo.Enums.CommunityTopic;
import com.group3.keo.Enums.RoleType;

import com.group3.keo.Users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class RoleTest {
    private Community community;
    private PersonalUser user;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    public void setUp() {
        community = new Community("TestCommunity", CommunityTopic.ART, new Picture("dummy.png", 10, 100, 100, false));
        user = new PersonalUser("user13", "Michael Jackson", "bio", address, location);
    }

    @Test
    public void testRoleCreation() {
        Role role = new Role(RoleType.HEAD, community, user);
        assertEquals(RoleType.HEAD, role.getRoleType());
        assertEquals(community, role.getCommunity());
        assertEquals(user, role.getUser());
        assertTrue(Role.getExtent().contains(role));
    }

    @Test
    public void testDuplicateRole() {
        new Role(RoleType.HEAD, community, user);
        assertThrows(IllegalStateException.class, () -> new Role(RoleType.MEMBER, community, user));
    }

    @Test
    public void testRoleTypeNull() {
        Role role = new Role(RoleType.MEMBER, community, user);
        assertThrows(IllegalArgumentException.class, () -> role.setRoleType(null));
    }

    @Test
    public void testCommunityNull() {
        Role role = new Role(RoleType.MEMBER, community, user);
        assertThrows(IllegalArgumentException.class, () -> role.setCommunity(null));
    }

    @Test
    public void testUserNull() {
        Role role = new Role(RoleType.MEMBER, community, user);
        assertThrows(IllegalArgumentException.class, () -> role.setUser(null));
    }

    @Test
    public void testExtentUnmodifiable() {
        Role role = new Role(RoleType.MEMBER, community, user);
        Set<Role> extent = Role.getExtent();
        assertThrows(UnsupportedOperationException.class, () -> extent.add(role));
    }
}