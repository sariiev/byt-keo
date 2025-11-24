package com.group3.keo.MediaAttachments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {
    @Test
    public void testVideoParameters() {
        Video video = new Video("video.mp4", 200, 1920, 1080, 300, true, 2);
        assertEquals("video.mp4", video.getSource());
        assertEquals(200, video.getFileSize());
        assertEquals(1920, video.getWidth());
        assertEquals(1080, video.getHeight());
        assertEquals(300, video.getDuration());
        assertTrue(video.isHasAudio());
        assertEquals(2, video.getChannels());
    }

    @Test
    public void testSetDurationInvalid() {
        Video video = new Video("v.mp4", 50, 640, 480, 10, false, null);
        assertThrows(IllegalArgumentException.class, () -> video.setDuration(-1));
        assertThrows(IllegalArgumentException.class, () -> video.setDuration(Video.MaxDuration + 1));
    }

    @Test
    public void testSetHasAudioResetsChannels() {
        Video video = new Video("v.mp4", 50, 640, 480, 100, true, 1);
        assertEquals(1, video.getChannels());
        video.setHasAudio(false);
        assertNull(video.getChannels());
    }

    @Test
    public void testSetChannelsInvalid() {
        Video video = new Video("v.mp4", 50, 640, 480, 100, true, 1);
        assertThrows(IllegalArgumentException.class, () -> video.setChannels(0));
        assertThrows(IllegalArgumentException.class, () -> video.setChannels(3));
        assertThrows(IllegalArgumentException.class, () -> video.setChannels(null));
    }

    @Test
    public void testSetChannelsIgnoredWhenNoAudio() {
        Video video = new Video("v.mp4", 50, 640, 480, 100, false, null);
        video.setChannels(2);
        assertNull(video.getChannels());
    }
}
