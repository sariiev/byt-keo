package com.group3.keo.Promotion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PromotionPlanTest {

    @Test
    public void testPlanParameters() {
        PromotionPlan plan = new PromotionPlan("Basic", 1000, 0.5, false);
        assertEquals("Basic", plan.getName());
        assertEquals(1000, plan.getViews());
        assertEquals(0.5, plan.getPricePerView());
        assertFalse(plan.isCustom());
        assertTrue(plan.getOrders().isEmpty());
        assertEquals(500, plan.getTotalPrice());
    }

    @Test
    public void testNameValidation() {
        assertThrows(IllegalArgumentException.class, () -> new PromotionPlan(null, 1000, 1, false));
        assertThrows(IllegalArgumentException.class, () -> new PromotionPlan("   ", 1000, 1, false));
        PromotionPlan plan = new PromotionPlan("Plan", 1000, 1, false);
        plan.setName("  New Plan  ");
        assertEquals("New Plan", plan.getName());
    }

    @Test
    public void testViewsValidation() {
        assertThrows(IllegalArgumentException.class, () -> new PromotionPlan("P", 0, 1, false));
        assertThrows(IllegalArgumentException.class, () -> new PromotionPlan("P", -10, 1, false));
        PromotionPlan customPlan = new PromotionPlan("Custom", 2000, 1, true);
        assertThrows(IllegalArgumentException.class, () -> customPlan.validateCustomViews(500)); // < min
        assertThrows(IllegalArgumentException.class, () -> customPlan.validateCustomViews(2_000_000)); // > max
    }

    @Test
    public void testPriceValidation() {
        assertThrows(IllegalArgumentException.class, () -> new PromotionPlan("P", 1000, -0.1, false));
        PromotionPlan plan = new PromotionPlan("P", 1000, 0.1, false);
        plan.setPricePerView(0.5);
        assertEquals(0.5, plan.getPricePerView());
    }

    @Test
    public void testCreateOrder() {
        PromotionPlan plan = new PromotionPlan("Basic", 1000, 0.5, false);
        PromotionOrder order = plan.createOrder();
        assertEquals(1, plan.getOrders().size());
        assertSame(order, plan.getOrders().get(0));
        assertEquals(plan, order.getPlan());
    }

    @Test
    public void testTotalPrice() {
        PromotionPlan plan = new PromotionPlan("Basic", 2000, 0.25, false);
        assertEquals(500, plan.getTotalPrice());
    }
}