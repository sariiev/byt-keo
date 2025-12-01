package com.group3.keo.enumsTests;

import com.group3.keo.enums.PromotionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PromotionStatusTest {
    @Test
    public void testNumberOfPromotionStatuses() {
        Assertions.assertEquals(3, PromotionStatus.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(PromotionStatus.valueOf("IN_PROGRESS"));
        Assertions.assertNotNull(PromotionStatus.valueOf("COMPLETED"));
        Assertions.assertNotNull(PromotionStatus.valueOf("CANCELED"));
    }
}
