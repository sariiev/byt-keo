package com.group3.keo.Publications;

import com.group3.keo.MediaAttachments.Picture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class QuoteTest {
    private Post originalPost;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        originalPost = new Post("Original", now);
    }

    @Test
    public void testQuoteCreation() {
        Quote q = new Quote("Quoted caption", now, originalPost);
        assertEquals("Quoted caption", q.getCaption());
        assertEquals(originalPost, q.getReferencedPublication());
        assertEquals(now, q.getPublicationDateTime());
    }

    @Test
    public void testReferencedPublicationNotNullException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quote("Some caption", now, null));
    }

    @Test
    public void testCaptionTrimAndEditFlag() {
        Quote q = new Quote("Hello", now, originalPost);
        assertFalse(q.isWasEdited());
        q.setCaption("Hello world");
        assertEquals("Hello world", q.getCaption());
        assertTrue(q.isWasEdited());
    }

    @Test
    public void testEmptyCaptionOnlyWithAttachment() {
        Quote q = new Quote("Nonempty", now, originalPost);
        Picture p = new Picture("img.png", 10, 100, 100, false);
        q.addAttachment(p);
        q.setCaption(null);
        assertDoesNotThrow(() -> q.setCaption("   "));
    }

    @Test
    public void testRemoveLastAttachmentWhenCaptionEmptyException() {
        Quote q = new Quote("test", now, originalPost);
        Picture pic = new Picture("img.png", 10, 100, 100, false);
        q.addAttachment(pic);
        q.setCaption(null);
        assertThrows(IllegalStateException.class, () -> q.removeAttachment(pic));
    }

    @Test
    public void testQuoteAsPostForPromotionFlag() {
        Quote q = new Quote("Test", now, originalPost);
        assertFalse(q.isWasPromoted());
        q.setWasPromoted(true);
        assertTrue(q.isWasPromoted());
    }
}
