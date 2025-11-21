package com.group3.keo;

import com.group3.keo.MediaAttachments.SoundAttachment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SoundAttachmentTest {
    @Test
    public void testInvalidChannelException(){
        assertThrows(IllegalArgumentException.class, () ->
                new SoundAttachment("sound.mp3", 10, 15, 100));
    }
    @Test
    public void testSoundAttachmentParameters(){
        SoundAttachment attachment = new SoundAttachment("attachment.wav", 200, 15, 2);
        assertEquals("attachment.wav", attachment.getSource());
        assertEquals(200, attachment.getFileSize());
        assertEquals(15, attachment.getDuration());
        assertEquals(2, attachment.getChannels());
    }
}
