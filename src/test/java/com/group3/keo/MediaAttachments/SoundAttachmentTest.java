package com.group3.keo.MediaAttachments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SoundAttachmentTest {
    @Test
    public void testSoundParameters() {
        SoundAttachment sound = new SoundAttachment("audio.mp3", 50, 200, 2);
        assertEquals("audio.mp3", sound.getSource());
        assertEquals(50, sound.getFileSize());
        assertEquals(200, sound.getDuration());
        assertEquals(2, sound.getChannels());
    }

    @Test
    public void testSetDurationException() {
        SoundAttachment sound = new SoundAttachment("s.mp3", 20, 100, 1);
        assertThrows(IllegalArgumentException.class, () -> sound.setDuration(-1));
        assertThrows(IllegalArgumentException.class, () -> sound.setDuration(SoundAttachment.MaxDuration + 1));
    }

    @Test
    public void testSetChannelsException() {
        SoundAttachment sound = new SoundAttachment("s.mp3", 20, 100, 1);
        assertThrows(IllegalArgumentException.class, () -> sound.setChannels(0));
        assertThrows(IllegalArgumentException.class, () -> sound.setChannels(3));
    }

    @Test
    public void testInvalidSourceAndFileSize() {
        assertThrows(IllegalArgumentException.class, () -> new SoundAttachment(null, 10, 50, 1));
        assertThrows(IllegalArgumentException.class, () -> new SoundAttachment("   ", 10, 50, 1));
        assertThrows(IllegalArgumentException.class, () -> new SoundAttachment("s.mp3", -1, 50, 1));
        assertThrows(IllegalArgumentException.class, () -> new SoundAttachment("s.mp3", MediaAttachment.MaxFileSize + 1, 50, 1));
    }
}
