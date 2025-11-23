package com.group3.keo;

import com.group3.keo.Users.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    public static class PersonalUser extends User {
        public PersonalUser(String username, String name, String bio, Address address, Location location){
            super(username, name, bio, address, location);
        }
    }
    private User createUser(){
        return new PersonalUser("user13",
                "Michael Jackson",
                "I write songs",
                new User.Address("Poland", "Warsaw", "Koszykowa"),
                new User.Location(14.13, 21.1));
    }
    @Test
    public void testFollowingFunctionality(){
        User user1 = createUser();
        User user2 = new PersonalUser("ladygaga",
                "Lady Gaga",
                "I write songs too",
                new User.Address("Poland", "Warsaw", "Nowogorodzka"),
                new User.Location(13.13, 28.1));

        user1.addFollower(user2);
        assertEquals(1, user1.getFollowersCount());
        assertTrue(user1.getFollowers().contains(user2));

        user1.removeFollower(user2);
        assertEquals(0, user1.getFollowersCount());
    }
    @Test
    public void testSelfFollow(){
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> user.addFollower(user));
    }
    @Test
    public void testTrimmedBio(){
        User user = createUser();
        user.setBio("   I write songs       ");
        assertEquals("I write songs", user.getBio());
    }
    @Test
    public void testBioLength(){
        String bio = "I".repeat(400);
        User user = createUser();
        assertThrows(IllegalArgumentException.class, () -> user.setBio(bio));
    }
}
