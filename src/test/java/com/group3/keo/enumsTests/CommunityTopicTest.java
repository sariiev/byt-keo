package com.group3.keo.enumsTests;

import com.group3.keo.enums.CommunityTopic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommunityTopicTest {
    @Test
    public void testNumberOfCommunityTopics() {
        Assertions.assertEquals(9, CommunityTopic.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(CommunityTopic.valueOf("SPORTS"));
        Assertions.assertNotNull(CommunityTopic.valueOf("ART"));
        Assertions.assertNotNull(CommunityTopic.valueOf("FOOD"));
    }
}
