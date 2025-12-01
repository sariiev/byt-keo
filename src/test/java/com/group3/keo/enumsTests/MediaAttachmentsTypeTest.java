package com.group3.keo.enumsTests;

import com.group3.keo.enums.MediaAttachmentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MediaAttachmentsTypeTest {
    @Test
    public void testNumberOfMediaAttachments() {
        Assertions.assertEquals(3, MediaAttachmentType.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(MediaAttachmentType.valueOf("PICTURE"));
        Assertions.assertNotNull(MediaAttachmentType.valueOf("VIDEO"));
        Assertions.assertNotNull(MediaAttachmentType.valueOf("SOUND"));
    }
}
