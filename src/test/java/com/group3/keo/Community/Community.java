package com.group3.keo.Community;

import static org.junit.jupiter.api.Assertions.*;
import com.group3.keo.Enums.CommunityTopic;
import com.group3.keo.MediaAttachments.Picture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommunityTest {
    private Picture picture;

    @BeforeEach
    public void setUp() {
        picture = new Picture("pic.png", 20, 100, 100, false);
    }

    @Test
    public void testCommunityCreation() {
        Community c = new Community("Name", CommunityTopic.TECHNOLOGY, picture);
        assertEquals("Name", c.getName());
        assertEquals(CommunityTopic.TECHNOLOGY, c.getTopic());
        assertEquals(picture, c.getAvatar());
    }

    @Test
    public void testSetAllValidTopics() {
        Community c = new Community("Name", CommunityTopic.SPORTS, picture);
        for (CommunityTopic topic : CommunityTopic.values()) {
            c.setTopic(topic);
            assertEquals(topic, c.getTopic());
        }
    }

    @Test
    public void testSetNameNullOrEmpty() {
        Community c = new Community("Name", CommunityTopic.SPORTS, picture);

        assertThrows(IllegalArgumentException.class, () -> c.setName(null));
        assertThrows(IllegalArgumentException.class, () -> c.setName(""));
        assertThrows(IllegalArgumentException.class, () -> c.setName("   "));
    }

    @Test
    public void testSetNameTooLong() {
        Community c = new Community("Name", CommunityTopic.SPORTS, picture);
        String longName = "ThisNameIsWayTooLong";
        assertThrows(IllegalArgumentException.class, () -> c.setName(longName));
    }

    @Test
    public void testSetTopicNull() {
        Community c = new Community("Name", CommunityTopic.SPORTS, picture);
        assertThrows(IllegalArgumentException.class, () -> c.setTopic(null));
    }

    @Test
    public void testSetAvatar() {
        Community c = new Community("Name", CommunityTopic.SPORTS, picture);
        Picture newAvatar = new Picture("pic.png", 20, 200, 200, true);
        c.setAvatar(newAvatar);
        assertEquals(newAvatar, c.getAvatar());
    }
}
