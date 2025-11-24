package com.group3.keo.Users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessUserTest {
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    private BusinessUser createBusinessUser() {
        return new BusinessUser(
                "company1",
                "Apple Inc",
                "We create products.",
                address,
                location,
                "https://apple.com",
                "support@apple.com",
                "+1 555 111 222"
        );
    }

    @Test
    public void testSetNameValid() {
        BusinessUser user = createBusinessUser();
        user.setName("Microsoft Corp");
        assertEquals("Microsoft Corp", user.getName());
    }

    @Test
    public void testSetNameInvalidEmpty() {
        BusinessUser user = createBusinessUser();
        assertThrows(IllegalArgumentException.class, () -> user.setName("   "));
    }

    @Test
    public void testSetBioValid() {
        BusinessUser user = createBusinessUser();
        user.setBio("New bio content");
        assertEquals("New bio content", user.getBio());
    }

    @Test
    public void testSetBioNull() {
        BusinessUser user = createBusinessUser();
        user.setBio(null);
        assertNull(user.getBio());
    }

    @Test
    public void testSetBioTooLong() {
        BusinessUser user = createBusinessUser();
        String longBio = "a".repeat(301);
        assertThrows(IllegalArgumentException.class, () -> user.setBio(longBio));
    }

    @Test
    public void testSetAddressNotNull() {
        BusinessUser user = createBusinessUser();
        assertThrows(IllegalArgumentException.class, () -> user.setAddress(null));
    }

    @Test
    public void testSetLocationNotNull() {
        BusinessUser user = createBusinessUser();
        assertThrows(IllegalArgumentException.class, () -> user.setLocation(null));
    }

    @Test
    public void testSetWebsiteLinkValid() {
        BusinessUser user = createBusinessUser();
        user.setWebsiteLink("https://microsoft.com");
        assertEquals("https://microsoft.com", user.getWebsiteLink());
    }

    @Test
    public void testSetWebsiteLinkInvalid() {
        BusinessUser user = createBusinessUser();
        assertThrows(IllegalArgumentException.class, () -> user.setWebsiteLink("invalid-url"));
    }

    @Test
    public void testSetEmailValid() {
        BusinessUser user = createBusinessUser();
        user.setEmail("contact@microsoft.com");
        assertEquals("contact@microsoft.com", user.getEmail());
    }

    @Test
    public void testSetEmailInvalid() {
        BusinessUser user = createBusinessUser();
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalid-email"));
    }

    @Test
    public void testSetPhoneNumberValid() {
        BusinessUser user = createBusinessUser();
        user.setPhoneNumber("+1 555 222 333");
        assertEquals("+1 555 222 333", user.getPhoneNumber());
    }

    @Test
    void testSetPhoneNumberInvalid() {
        BusinessUser user = createBusinessUser();
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("ABC123"));
    }

    @Test
    public void testAddFollower() {
        BusinessUser user = createBusinessUser();
        BusinessUser follower = new BusinessUser(
                "company2",
                "Microsoft",
                null,
                address,
                location,
                "https://microsoft.com",
                "contact@microsoft.com",
                "+1 555 222 333"
        );

        user.addFollower(follower);

        assertEquals(1, user.getFollowersCount());
        assertTrue(user.getFollowers().contains(follower));
    }

    @Test
    public void testRemoveFollower() {
        BusinessUser user = createBusinessUser();
        BusinessUser follower = new BusinessUser(
                "company2",
                "Microsoft",
                null,
                address,
                location,
                "https://microsoft.com",
                "contact@microsoft.com",
                "+1 555 222 333"
        );

        user.addFollower(follower);
        user.removeFollower(follower);

        assertEquals(0, user.getFollowersCount());
    }

    @Test
    public void testSelfFollow() {
        BusinessUser user = createBusinessUser();
        assertThrows(IllegalArgumentException.class, () -> user.addFollower(user));
    }
}
