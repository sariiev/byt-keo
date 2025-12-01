package com.group3.keo.communityTests;

import com.group3.keo.community.Community;
import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommunityTest {
    private Picture pic;

    public CommunityTest() {
    }

    private void clearCommunityExtent() {
        try {
            Field field = Community.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private void clearMediaAttachmentExtent() {
        try {
            Field field = MediaAttachment.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private void addToMediaAttachmentExtent(Picture pic) {
        try {
            Field field = MediaAttachment.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<Object, Object> extent = (Map)field.get((Object)null);
            extent.put(pic.getUid(), pic);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    @BeforeEach
    public void setup() {
        this.clearCommunityExtent();
        this.clearMediaAttachmentExtent();
        this.pic = new Picture("src", 100, 800, 600, false);
        this.addToMediaAttachmentExtent(this.pic);
    }

    @Test
    public void testAttributesInitialization() {
        Community c = new Community("Business", CommunityTopic.BUSINESS, this.pic);
        Assertions.assertEquals("Business", c.getName());
        Assertions.assertEquals(CommunityTopic.BUSINESS, c.getTopic());
        Assertions.assertEquals(this.pic, c.getAvatar());
        Assertions.assertNotNull(c.getUid());
    }

    @Test
    public void testSetNameEmptyException() {
        Community c = new Community("Tech", CommunityTopic.TECHNOLOGY, this.pic);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            c.setName("");
        });
    }

    @Test
    public void testSetNameTooLongException() {
        Community c = new Community("Tech", CommunityTopic.TECHNOLOGY, this.pic);
        String longName = "Toooooooooooooooooo long";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            c.setName(longName);
        });
    }

    @Test
    public void testSetTopicNullException() {
        Community c = new Community("Tech", CommunityTopic.TECHNOLOGY, this.pic);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            c.setTopic((CommunityTopic)null);
        });
    }

    @Test
    public void testExtentContainsCreatedObject() {
        Community c = new Community("Sports", CommunityTopic.SPORTS, this.pic);
        Map<UUID, Community> extent = Community.getExtent();
        Assertions.assertEquals(1, extent.size());
        Assertions.assertTrue(extent.containsKey(c.getUid()));
    }

    @Test
    public void testEncapsulation() {
        Community c = new Community("Art", CommunityTopic.ART, this.pic);
        String externalName = "Art";
        externalName = "Changed";
        Assertions.assertNotEquals(externalName, c.getName(), "Changing the external variable should not change the object inside Community");
    }

    @Test
    public void testExtentPersistence() throws Exception {
        new Community("Science", CommunityTopic.SCIENCE, this.pic);
        File tempFile = Files.createTempFile("community_test", ".json").toFile();
        Community.saveExtent(tempFile.getAbsolutePath());
        this.clearCommunityExtent();
        Assertions.assertEquals(0, Community.getExtent().size());
        Community.loadExtent(tempFile.getAbsolutePath());
        Assertions.assertEquals(1, Community.getExtent().size());
        Community loaded = (Community)Community.getExtent().values().iterator().next();
        Assertions.assertEquals("Science", loaded.getName());
        Assertions.assertEquals(CommunityTopic.SCIENCE, loaded.getTopic());
        Assertions.assertEquals(this.pic.getUid(), loaded.getAvatar().getUid());
    }
}
