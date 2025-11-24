package com.group3.keo.MediaAttachments;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PictureTest {
    @Test
    public void testPictureParameters() {
        Picture pic = new Picture("image.png", 100, 800, 600, true);
        assertEquals("image.png", pic.getSource());
        assertEquals(100, pic.getFileSize());
        assertEquals(800, pic.getWidth());
        assertEquals(600, pic.getHeight());
        assertTrue(pic.isAnimated());
    }

    @Test
    public void testSetAnimated() {
        Picture pic = new Picture("img.png", 50, 640, 480, false);
        assertFalse(pic.isAnimated());
        pic.setAnimated(true);
        assertTrue(pic.isAnimated());
    }

    @Test
    public void testInvalidSource() {
        assertThrows(IllegalArgumentException.class, () -> new Picture(null, 50, 100, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new Picture("   ", 50, 100, 100, false));
    }

    @Test
    public void testInvalidFileSize() {
        assertThrows(IllegalArgumentException.class, () -> new Picture("img.png", -1, 100, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new Picture("img.png", MediaAttachment.MaxFileSize + 1, 100, 100, false));
    }

    @Test
    public void testInvalidWidthHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Picture("img.png", 50, 0, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new Picture("img.png", 50, 100, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new Picture("img.png", 50, VisualAttachment.MaxWidth + 1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new Picture("img.png", 50, 100, VisualAttachment.MaxHeight + 1, false));
    }
}
