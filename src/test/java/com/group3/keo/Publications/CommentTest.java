package com.group3.keo.Publications;
import static org.junit.jupiter.api.Assertions.*;

import com.group3.keo.MediaAttachments.Picture;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CommentTest {
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    public void testCommentParameters() {
        Post publication = new Post("Hello", now);
        Comment comment = new Comment("Nice post", now, publication);
        assertEquals("Nice post", comment.getCaption());
        assertEquals(publication, comment.getPublication());
        assertEquals(now, comment.getPublicationDateTime());
    }

    @Test
    public void testPublicationNullException() {
        assertThrows(IllegalArgumentException.class, () -> new Comment("test", now, null));
    }

    @Test
    public void testCommentCaptionTooLong() {
        Post publication = new Post("Hello", now);
        String longCaption = "a".repeat(PublicationBase.MaximumCaptionLength + 1);
        assertThrows(IllegalArgumentException.class, () -> new Comment(longCaption, now, publication));
    }

    @Test
    public void testCommentEmptyCaptionIfAttachmentExists() {
        Post publication = new Post("Hello", now);
        Comment comment = new Comment("", now, publication);
        Picture pic = new Picture("img.png", 100, 100, 10, false);
        comment.addAttachment(pic);
        assertTrue(comment.getAttachments().contains(pic));
    }

    @Test
    public void testPublicationBaseCaptionEdited() {
        Post publication = new Post("Hello", now);
        Comment c = new Comment("Nice", now, publication);
        c.setCaption("Updated");
        assertTrue(c.isWasEdited());
        assertEquals("Updated", c.getCaption());
    }

    @Test
    public void testCommentToPublication() {
        Post p = new Post("Hello", now);
        Comment c = new Comment("Nice", now, p);
        p.addComment(c);
        assertEquals(1, p.getComments().size());
        assertSame(c, p.getComments().get(0));
    }

    @Test
    public void testCommentToOtherPublication() {
        Post p1 = new Post("Hello", now);
        Post p2 = new Post("Other", now);
        Comment c = new Comment("Nice", now, p1);
        assertThrows(IllegalArgumentException.class, () -> p2.addComment(c));
    }
}