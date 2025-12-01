package com.group3.keo.mediaTests;

import com.group3.keo.media.Picture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PictureTest {
    @Test
    public void testPictureParameters() {
        Picture pic = new Picture("image.png", 100, 800, 600, true);
        Assertions.assertEquals("image.png", pic.getSource());
        Assertions.assertEquals(100, pic.getFileSize());
        Assertions.assertEquals(800, pic.getWidth());
        Assertions.assertEquals(600, pic.getHeight());
        Assertions.assertTrue(pic.isAnimated());
    }

    @Test
    public void testSetAnimated() {
        Picture pic = new Picture("img.png", 50, 640, 480, false);
        Assertions.assertFalse(pic.isAnimated());
        pic.setAnimated(true);
        Assertions.assertTrue(pic.isAnimated());
    }

    @Test
    public void testInvalidSource() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture((String)null, 50, 100, 100, false);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture("   ", 50, 100, 100, false);
        });
    }

    @Test
    public void testInvalidFileSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture("img.png", -1, 100, 100, false);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture("img.png", 501, 100, 100, false);
        });
    }

    @Test
    public void testInvalidWidthHeight() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture("img.png", 50, 0, 100, false);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture("img.png", 50, 100, 0, false);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture("img.png", 50, 3841, 100, false);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Picture("img.png", 50, 100, 2161, false);
        });
    }
}
