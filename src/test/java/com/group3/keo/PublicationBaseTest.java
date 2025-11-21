package com.group3.keo;

import com.group3.keo.MediaAttachments.MediaAttachment;
import com.group3.keo.MediaAttachments.SoundAttachment;
import com.group3.keo.Publications.PublicationBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class PublicationBaseTest {

    public static class PublicPublication extends PublicationBase {
        public PublicPublication(String caption, LocalDateTime dateTime){
            super(caption, dateTime);
        }
    }
    @Test
    public void testCaptionAndDate(){
        LocalDateTime dateTime = LocalDateTime.now();
        PublicationBase publication = new PublicPublication("Some caption", dateTime);
        assertEquals("Some caption", publication.getCaption());
        assertEquals(dateTime, publication.getPublicationDateTime());
    }
    @Test
    public void testAddingAttachment(){
        LocalDateTime dateTime = LocalDateTime.now();
        PublicationBase publication = new PublicPublication("Some caption", dateTime);
        MediaAttachment attachment = new SoundAttachment("sound.mp3", 10, 15, 1);
        publication.addAttachment(attachment);
        assertEquals(1, publication.getAttachments().size());
        assertTrue(publication.getAttachments().contains(attachment));
    }

}
