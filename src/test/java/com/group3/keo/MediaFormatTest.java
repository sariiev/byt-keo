package com.group3.keo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MediaFormatTest {
    @Test
    public void testNumberOfMediaFormats(){
        assertEquals(13, MediaFormat.values().length);
    }
    @Test
    public void testEnumValues(){
        assertNotNull(MediaFormat.valueOf("JPEG"));
        assertNotNull(MediaFormat.valueOf("MP4"));
        assertNotNull(MediaFormat.valueOf("WAV"));
    }
}
