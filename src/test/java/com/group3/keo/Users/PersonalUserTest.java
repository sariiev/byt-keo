package com.group3.keo.Users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonalUserTest {
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    private PersonalUser createUser() {
        return new PersonalUser(
                "user13",
                "Michael Jackson",
                "bio",
                address,
                location
        );
    }

    @Test
    public void testUserParameters() {
        PersonalUser user = createUser();

        assertEquals("user13", user.getUsername());
        assertEquals("Michael Jackson", user.getName());
        assertEquals("bio", user.getBio());
        assertEquals(address, user.getAddress());
        assertEquals(location, user.getLocation());
    }


    @Test
    public void testNameValid() {
        PersonalUser user = createUser();
        user.setName("New Name");
        assertEquals("New Name", user.getName());
    }

    @Test
    public void testNameInvalidEmpty() {
        PersonalUser user = createUser();
        assertThrows(IllegalArgumentException.class, () -> user.setName("   "));
    }

    @Test
    public void testBioValid() {
        PersonalUser user = createUser();
        user.setBio("New bio");
        assertEquals("New bio", user.getBio());
    }

    @Test
    public void testBioNullAllowed() {
        PersonalUser user = createUser();
        user.setBio(null);
        assertNull(user.getBio());
    }

    @Test
    public void testBioTooLong() {
        PersonalUser user = createUser();
        String longBio = "a".repeat(301);
        assertThrows(IllegalArgumentException.class, () -> user.setBio(longBio));
    }

    @Test
    public void testInvalidEmptyBio() {
        PersonalUser user = createUser();
        assertThrows(IllegalArgumentException.class, () -> user.setBio("     "));
    }

    @Test
    public void testAddressNotNull() {
        PersonalUser user = createUser();
        assertThrows(IllegalArgumentException.class, () -> user.setAddress(null));
    }

    @Test
    public void testLocationNotNull() {
        PersonalUser user = createUser();
        assertThrows(IllegalArgumentException.class, () -> user.setLocation(null));
    }

    @Test
    public void testSelfFollow() {
        PersonalUser user = createUser();
        assertThrows(IllegalArgumentException.class, () -> user.addFollower(user));
    }

    @Test
    public void testAddAndRemoveFollowers() {
        PersonalUser u1 = createUser();
        PersonalUser u2 = new PersonalUser("user27", "Mike", null, address, location);

        u1.addFollower(u2);
        assertEquals(1, u1.getFollowersCount());
        assertTrue(u1.getFollowers().contains(u2));

        u1.removeFollower(u2);
        assertEquals(0, u1.getFollowersCount());
    }
}
