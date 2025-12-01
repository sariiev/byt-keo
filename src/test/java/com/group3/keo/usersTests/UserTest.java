package com.group3.keo.usersTests;

import com.group3.keo.users.BusinessUser;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @Test
    public void testPersonalUserCreation() {
        PersonalUser user = new PersonalUser("username", "Name", "Bio", this.address, this.location);
        Assertions.assertEquals("username", user.getUsername());
        Assertions.assertEquals("Name", user.getName());
        Assertions.assertEquals("Bio", user.getBio());
        Assertions.assertEquals(this.address, user.getAddress());
        Assertions.assertEquals(this.location, user.getLocation());
        Assertions.assertNotNull(user.getUid());
    }

    @Test
    public void testPersonalUserBioOptional() {
        PersonalUser user = new PersonalUser("user", "Name", (String)null, this.address, this.location);
        Assertions.assertNull(user.getBio());
    }

    @Test
    public void testBusinessUserCreation() {
        BusinessUser user = new BusinessUser("bizUser", "Biz Name", "Bio", this.address, this.location, "https://example.com", "test@example.com", "+1234567890");
        Assertions.assertEquals("https://example.com", user.getWebsiteLink());
        Assertions.assertEquals("test@example.com", user.getEmail());
        Assertions.assertEquals("+1234567890", user.getPhoneNumber());
    }

    @Test
    public void testBusinessUserInvalidEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BusinessUser("u", "N", "Bio", this.address, this.location, "https://site.com", "invalidemail", "+1234567890");
        });
    }

    @Test
    public void testBusinessUserInvalidWebsite() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BusinessUser("u", "N", "Bio", this.address, this.location, "htp:/invalid", "test@example.com", "+1234567890");
        });
    }

    @Test
    public void testBusinessUserInvalidPhone() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BusinessUser("u", "N", "Bio", this.address, this.location, "https://site.com", "test@example.com", "notphonenumber");
        });
    }

    @Test
    public void testAddFollower() {
        PersonalUser user = new PersonalUser("u1", "N", "Bio", this.address, this.location);
        PersonalUser follower = new PersonalUser("u2", "N2", "Bio2", this.address, this.location);
        user.addFollower(follower);
        Assertions.assertTrue(user.getFollowers().contains(follower));
        Assertions.assertEquals(1, user.getFollowersCount());
    }

    @Test
    public void testSelfFollowException() {
        PersonalUser user = new PersonalUser("u1", "N", "Bio", this.address, this.location);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            user.addFollower(user);
        });
    }

    @Test
    public void testRemoveFollower() {
        PersonalUser user = new PersonalUser("u1", "N", "Bio", this.address, this.location);
        PersonalUser follower = new PersonalUser("u2", "N2", "Bio2", this.address, this.location);
        user.addFollower(follower);
        user.removeFollower(follower);
        Assertions.assertFalse(user.getFollowers().contains(follower));
        Assertions.assertEquals(0, user.getFollowersCount());
    }

    @Test
    public void testRemoveNullFollowerDoesNothing() {
        PersonalUser user = new PersonalUser("user", "N", "Bio", this.address, this.location);
        user.removeFollower((User)null);
        Assertions.assertEquals(0, user.getFollowersCount());
    }

    @Test
    public void testExtentContainsUser() {
        PersonalUser user = new PersonalUser("user", "N", "Bio", this.address, this.location);
        Assertions.assertTrue(User.getExtent().containsKey(user.getUid()));
    }

    @Test
    public void testInvalidLatitude() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User.Location(100.0, 0.0);
        });
    }

    @Test
    public void testInvalidLongitude() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User.Location(0.0, 200.0);
        });
    }

    @Test
    public void testInvalidAddressFields() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User.Address("", "City", "Street");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User.Address("Country", "", "Street");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User.Address("Country", "City", "");
        });
    }
}
