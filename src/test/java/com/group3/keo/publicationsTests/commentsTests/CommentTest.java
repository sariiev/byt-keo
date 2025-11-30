package com.group3.keo.publicationsTests.commentsTests;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.publications.comments.Comment;
import com.group3.keo.publications.posts.PrivatePost;
import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommentTest {
    private PersonalUser author;
    private PersonalUser privateAuthor;
    private List<MediaAttachment> attachments;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    void setUp() {
        this.author = new PersonalUser("mike", "Mike", "bio3", this.address, this.location);
        this.privateAuthor = new PersonalUser("kate", "Kate", "bio2", this.address, this.location);
        this.attachments = List.of(new Picture("file.png", 100, 200, 200, false));
    }

    @Test
    public void testConstructorRejectsNullPublication() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Comment(this.author, "Caption", (PublicationBase)null, this.attachments);
        });
    }

    @Test
    public void testGetterCommentedPublication() {
        PublicPost post = new PublicPost(this.author, "Original Post", this.attachments);
        Comment comment = new Comment(this.author, "Nice post!", post, this.attachments);
        Assertions.assertEquals(post, comment.getCommentedPublication());
    }

    @Test
    public void testCommentAutomaticallyAddedToPublication() {
        PublicPost post = new PublicPost(this.author, "Original Post", this.attachments);
        Comment comment = new Comment(this.author, "Nice post!", post, this.attachments);
        Assertions.assertTrue(post.getComments().contains(comment));
    }

    @Test
    public void testCanViewPublicPublicationAlwaysTrue() {
        PublicPost post = new PublicPost(this.author, "Original Post", this.attachments);
        Comment comment = new Comment(this.author, "Nice post!", post, this.attachments);
        Assertions.assertTrue(comment.canView(new PersonalUser("john", "John", "bio1", this.address, this.location)));
    }

    @Test
    public void testCanViewPrivatePublicationRespectsAllowedUsers() {
        PrivatePost privatePost = new PrivatePost(this.privateAuthor, "Secret Post", this.attachments, Set.of(this.privateAuthor));
        Comment comment = new Comment(this.privateAuthor, "Private comment", privatePost, this.attachments);
        User allowedUser = this.privateAuthor;
        User notAllowedUser = new PersonalUser("john", "John", "bio1", this.address, this.location);
        Assertions.assertTrue(comment.canView(allowedUser));
        Assertions.assertFalse(comment.canView(notAllowedUser));
    }

    @Test
    public void testConstructorWithCustomFields() {
        PublicPost post = new PublicPost(this.author, "Original Post", this.attachments);
        Comment comment = new Comment(UUID.randomUUID(), this.author, "Custom comment", post, this.attachments, (LocalDateTime)null, 10, false);
        Assertions.assertEquals(post, comment.getCommentedPublication());
        Assertions.assertEquals("Custom comment", comment.getCaption());
    }

    @Test
    public void testAttachmentsEncapsulation() {
        PublicPost post = new PublicPost(this.author, "Original Post", this.attachments);
        Comment comment = new Comment(this.author, "Nice post!", post, this.attachments);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            comment.getAttachments().add(new Picture("file.png", 100, 200, 200, false));
        });
    }
}
