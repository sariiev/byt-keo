package com.group3.keo.mediaTests;

import com.group3.keo.media.Video;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoTest {
    @Test
    public void testVideoParameters() {
        Video video = new Video("video.mp4", 200, 1920, 1080, 300, true, 2);
        Assertions.assertEquals("video.mp4", video.getSource());
        Assertions.assertEquals(200, video.getFileSize());
        Assertions.assertEquals(1920, video.getWidth());
        Assertions.assertEquals(1080, video.getHeight());
        Assertions.assertEquals(300, video.getDuration());
        Assertions.assertTrue(video.hasAudio());
        Assertions.assertEquals(2, video.getChannels());
    }

    @Test
    public void testSetDurationInvalid() {
        Video video = new Video("v.mp4", 50, 640, 480, 10, false, (Integer)null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            video.setDuration(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            video.setDuration(601);
        });
    }

    @Test
    public void testSetHasAudioResetsChannels() {
        Video video = new Video("v.mp4", 50, 640, 480, 100, true, 1);
        Assertions.assertEquals(1, video.getChannels());
        video.setHasAudio(false);
        Assertions.assertNull(video.getChannels());
    }

    @Test
    public void testSetChannelsInvalid() {
        Video video = new Video("v.mp4", 50, 640, 480, 100, true, 1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            video.setChannels(0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            video.setChannels(3);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            video.setChannels((Integer)null);
        });
    }

    @Test
    public void testSetChannelsIgnoredWhenNoAudio() {
        Video video = new Video("v.mp4", 50, 640, 480, 100, false, (Integer)null);
        video.setChannels(2);
        Assertions.assertNull(video.getChannels());
    }
}
