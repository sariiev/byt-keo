package com.group3.keo.Promotion;

import com.group3.keo.Enums.PromotionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionOrderTest {
    @Test
    public void testNonCustomPlan() {
        PromotionPlan plan = new PromotionPlan("Basic", 1000, 0.5, false);
        PromotionOrder order = new PromotionOrder(plan);
        assertEquals(plan, order.getPlan());
        assertEquals(0.5, order.getPricePerView());
        assertEquals(PromotionStatus.IN_PROGRESS, order.getStatus());
        assertNotNull(order.getCreationDateTime());
        assertEquals(1000, order.getViews());
        assertEquals(500, order.getTotalPrice());
    }

    @Test
    public void testCustomPlan() {
        PromotionPlan plan = new PromotionPlan("Custom", 5000, 0.1, true);
        PromotionOrder order = new PromotionOrder(plan);
        assertEquals(5000, order.getViews());
        assertEquals(500, order.getTotalPrice());
    }

    @Test
    public void testNullPlanException() {
        assertThrows(IllegalArgumentException.class, () -> new PromotionOrder(null));
    }

    @Test
    public void testSetStatus() {
        PromotionPlan plan = new PromotionPlan("Basic", 1000, 0.5, false);
        PromotionOrder order = new PromotionOrder(plan);
        order.setStatus(PromotionStatus.COMPLETED);
        assertEquals(PromotionStatus.COMPLETED, order.getStatus());
        assertThrows(IllegalArgumentException.class, () -> order.setStatus(null));
    }

    @Test
    public void testTotalPrice() {
        PromotionPlan plan = new PromotionPlan("Basic", 2000, 0.25, false);
        PromotionOrder order = new PromotionOrder(plan);
        assertEquals(500, order.getTotalPrice());
    }
}