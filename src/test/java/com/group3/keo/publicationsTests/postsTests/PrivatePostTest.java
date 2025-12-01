package com.group3.keo.publicationsTests.postsTests;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.publications.posts.PrivatePost;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrivatePostTest {
    private PersonalUser author;
    private PersonalUser user1;
    private PersonalUser user2;
    private List<MediaAttachment> attachments;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);
  
    @BeforeEach
    void setUp() {
        this.user1 = new PersonalUser("john", "John", "bio1", this.address, this.location);
        this.user2 = new PersonalUser("kate", "Kate", "bio2", this.address, this.location);
        this.author = new PersonalUser("mike", "Mike", "bio3", this.address, this.location);
        this.attachments = List.of(new Picture("file.png", 100, 200, 200, false));
    }

    @Test
    public void testAuthorAlwaysAllowed() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, Set.of(this.user1));
        Assertions.assertTrue(post.getAllowedUsers().contains(this.author));
    }

    @Test
    public void testAddAllowedUser() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, (Set)null);
        post.addAllowedUser(this.user1);
        Assertions.assertTrue(post.getAllowedUsers().contains(this.user1));
    }

    @Test
    public void testRemoveAllowedUser() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, Set.of(this.user1, this.user2));
        post.removeAllowedUser(this.user1);
        Assertions.assertFalse(post.getAllowedUsers().contains(this.user1));
    }

    @Test
    public void testCannotRemoveAuthor() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, (Set)null);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            post.removeAllowedUser(this.author);
        });
    }

    @Test
    public void testCanView() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, Set.of(this.user1));
        Assertions.assertTrue(post.canView(this.author));
        Assertions.assertTrue(post.canView(this.user1));
        Assertions.assertFalse(post.canView(this.user2));
    }

    @Test
    public void testAllowedUsersImmutable() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, Set.of(this.user1));
        Set<User> allowedUsers = post.getAllowedUsers();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            allowedUsers.add(this.user2);
        });
    }

    @Test
    public void testEncapsulationAttachments() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, (Set)null);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            post.getAttachments().add(new Picture("file.png", 100, 200, 200, false));
        });
    }

    @Test
    public void testWasPromoted() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, (Set)null);
        Assertions.assertFalse(post.wasPromoted());
        post.setWasPromoted(true);
        Assertions.assertTrue(post.wasPromoted());
    }

    @Test
    public void testRemoveNonExistingUser() {
        PrivatePost post = new PrivatePost(this.author, "Caption", this.attachments, Set.of(this.user1));
        post.removeAllowedUser(this.user2);
        Assertions.assertTrue(post.getAllowedUsers().contains(this.user1));
        Assertions.assertTrue(post.getAllowedUsers().contains(this.author));
    }
}
