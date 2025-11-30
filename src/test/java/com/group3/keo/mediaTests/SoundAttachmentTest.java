package com.group3.keo.mediaTests;

import com.group3.keo.media.SoundAttachment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SoundAttachmentTest {
    @Test
    public void testSoundParameters() {
        SoundAttachment sound = new SoundAttachment("audio.mp3", 50, 200, 2);
        Assertions.assertEquals("audio.mp3", sound.getSource());
        Assertions.assertEquals(50, sound.getFileSize());
        Assertions.assertEquals(200, sound.getDuration());
        Assertions.assertEquals(2, sound.getChannels());
    }

    @Test
    public void testSetDurationException() {
        SoundAttachment sound = new SoundAttachment("s.mp3", 20, 100, 1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sound.setDuration(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sound.setDuration(601);
        });
    }

    @Test
    public void testSetChannelsException() {
        SoundAttachment sound = new SoundAttachment("s.mp3", 20, 100, 1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sound.setChannels(0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sound.setChannels(3);
        });
    }

    @Test
    public void testInvalidSourceAndFileSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new SoundAttachment((String)null, 10, 50, 1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new SoundAttachment("   ", 10, 50, 1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new SoundAttachment("s.mp3", -1, 50, 1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new SoundAttachment("s.mp3", 501, 50, 1);
        });
    }
}
