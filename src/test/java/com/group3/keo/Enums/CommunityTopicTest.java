package com.group3.keo.Enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CommunityTopicTest {
    @Test
    public void testNumberOfPromotionStatuses(){
        assertEquals(9, CommunityTopic.values().length);
    }
    @Test
    public void testEnumValues(){
        assertNotNull(CommunityTopic.valueOf("SPORTS"));
        assertNotNull(CommunityTopic.valueOf("ART"));
        assertNotNull(CommunityTopic.valueOf("FOOD"));
    }
}
