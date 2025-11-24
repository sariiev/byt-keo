package com.group3.keo.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionStatusTest {
    @Test
    public void testNumberOfPromotionStatuses(){
        assertEquals(2, PromotionStatus.values().length);
    }
    @Test
    public void testEnumValues(){
        assertNotNull(PromotionStatus.valueOf("IN_PROGRESS"));
        assertNotNull(PromotionStatus.valueOf("COMPLETED"));
    }
}
