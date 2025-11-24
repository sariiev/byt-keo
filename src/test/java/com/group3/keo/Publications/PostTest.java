package com.group3.keo.Publications;

import com.group3.keo.MediaAttachments.Picture;
import com.group3.keo.Users.PersonalUser;
import com.group3.keo.Users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class PostTest {
    private Post post;
    private final LocalDateTime now = LocalDateTime.now();
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    public void setUp() {
        post = new Post("Hello world", now);
    }

    @Test
    public void testPostCreation() {
        assertEquals("Hello world", post.getCaption());
        assertEquals(now, post.getPublicationDateTime());
        assertEquals(0, post.getLikes());
        assertEquals(0, post.getViews());
    }

    @Test
    public void testNullDateException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Post("caption", null)
        );
    }

    @Test
    public void testCaptionNullNoAttachmentException() {
        Post p = new Post("test", now);
        assertThrows(IllegalStateException.class, () -> p.setCaption(null));
    }

    @Test
    public void testCaptionTooLongThrows() {
        String longCaption = "a".repeat(PublicationBase.MaximumCaptionLength + 1);
        assertThrows(IllegalArgumentException.class, () -> new Post(longCaption, now));
    }

    @Test
    public void testCaptionTrim() {
        Post p = new Post("   abc   ", now);
        assertEquals("abc", p.getCaption());
    }

    @Test
    public void testCaptionEditedFlag() {
        Post p = new Post("old", now);
        assertFalse(p.isWasEdited());
        p.setCaption("new");
        assertTrue(p.isWasEdited());
    }

    @Test
    public void testAddAttachment() {
        Picture picture = new Picture("img.png", 200, 100, 100, false);
        post.addAttachment(picture);
        assertEquals(1, post.getAttachments().size());
    }

    @Test
    public void testAddNullAttachmentException() {
        assertThrows(IllegalArgumentException.class, () -> post.addAttachment(null));
    }

    @Test
    public void testRemoveAttachment() {
        Picture pic = new Picture("img.png", 100, 100, 100, false);
        post.addAttachment(pic);
        assertDoesNotThrow(() -> post.removeAttachment(pic));
        assertTrue(post.getAttachments().isEmpty());
    }

    @Test
    public void testAddLike() {
        PersonalUser u = new PersonalUser("u", "name", "bio", address, location);
        post.addLike(u);
        assertEquals(1, post.getLikes());
    }

    @Test
    public void testAddView() {
        post.addView();
        post.addView();
        assertEquals(2, post.getViews());
    }

    @Test
    public void testWasPromotedFlag() {
        assertFalse(post.isWasPromoted());
        post.setWasPromoted(true);
        assertTrue(post.isWasPromoted());
    }
}

