package com.group3.keo.enumsTests;

import com.group3.keo.enums.MediaFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MediaFormatTest {
    @Test
    public void testNumberOfMediaFormats() {
        Assertions.assertEquals(13, MediaFormat.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(MediaFormat.valueOf("JPEG"));
        Assertions.assertNotNull(MediaFormat.valueOf("MP4"));
        Assertions.assertNotNull(MediaFormat.valueOf("WAV"));
    }
}
