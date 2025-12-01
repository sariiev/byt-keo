package com.group3.keo.publicationsTests.postsTests;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PublicPostTest {
    private PublicationAuthor author;
    private List<MediaAttachment> attachments;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    public void setUp() {
        this.author = new PersonalUser("user13", "Michael Jackson", "bio", this.address, this.location);
        this.attachments = List.of(new Picture("file.png", 100, 200, 200, false));
    }

    @Test
    public void testInitialWasPromotedFalse() {
        PublicPost post = new PublicPost(this.author, "Caption", this.attachments);
        Assertions.assertFalse(post.wasPromoted());
    }

    @Test
    public void testSetWasPromotedTrue() {
        PublicPost post = new PublicPost(this.author, "Caption", this.attachments);
        post.setWasPromoted(true);
        Assertions.assertTrue(post.wasPromoted());
    }

    @Test
    public void testSetWasPromotedToFalseException() {
        PublicPost post = new PublicPost(this.author, "Caption", this.attachments);
        post.setWasPromoted(true);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            post.setWasPromoted(false);
        });
    }

    @Test
    public void testEncapsulationAttachments() {
        PublicPost post = new PublicPost(this.author, "Caption", this.attachments);
        List<MediaAttachment> postAttachments = post.getAttachments();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            postAttachments.add(new Picture("file1.png", 100, 200, 200, false));
        });
    }
}
