package com.group3.keo.promotionTests;

import com.group3.keo.enums.PromotionStatus;
import com.group3.keo.promotion.PromotionOrder;
import com.group3.keo.promotion.PromotionPlan;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PromotionPlanTest {
    private PromotionPlan activePlan;
    private PromotionPlan inactivePlan;

    private void clearPromotionOrderExtent() {
        try {
            Field field = PromotionOrder.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    @BeforeEach
    public void setup() {
        this.activePlan = new PromotionPlan("Standard", 100, 0.5);
        this.inactivePlan = new PromotionPlan("Inactive", 50, 0.3);
        this.inactivePlan.setActive(false);
        this.clearPromotionOrderExtent();
    }

    @Test
    public void testCreateOrderActivePlan() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        Assertions.assertNotNull(order.getUid());
        Assertions.assertEquals(this.activePlan, order.getPlan());
        Assertions.assertFalse(order.isCustomOrder());
        Assertions.assertEquals(this.activePlan.getViews(), order.getViews());
        Assertions.assertEquals(this.activePlan.getPricePerView(), order.getPricePerView());
        Assertions.assertEquals(PromotionStatus.IN_PROGRESS, order.getStatus());
    }

    @Test
    public void testCreateOrderInactivePlanException() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            new PromotionOrder(this.inactivePlan);
        });
    }

    @Test
    public void testCreateCustomOrder() {
        PromotionPlan customPlan = new PromotionPlan(2000, 1.0);
        PromotionOrder order = new PromotionOrder(customPlan);
        Assertions.assertTrue(order.isCustomOrder());
        Assertions.assertNull(order.getPlan());
        Assertions.assertEquals(2000, order.getViews());
        Assertions.assertEquals(1.0, order.getPricePerView());
    }

    @Test
    public void testSetStatus() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        order.setStatus(PromotionStatus.CANCELED);
        Assertions.assertEquals(PromotionStatus.CANCELED, order.getStatus());
    }

    @Test
    public void testSetStatusNullException() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            order.setStatus((PromotionStatus)null);
        });
    }

    @Test
    public void testSetStatusAfterCompletedException() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        order.setStatus(PromotionStatus.COMPLETED);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            order.setStatus(PromotionStatus.CANCELED);
        });
    }

    @Test
    public void testSetStatusAfterCanceledException() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        order.setStatus(PromotionStatus.CANCELED);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            order.setStatus(PromotionStatus.IN_PROGRESS);
        });
    }

    @Test
    public void testTotalPriceCalculation() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        Assertions.assertEquals((double)this.activePlan.getViews() * this.activePlan.getPricePerView(), order.getTotalPrice());
    }

    @Test
    public void testExtentContainsOrder() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        Assertions.assertTrue(PromotionOrder.getExtent().containsKey(order.getUid()));
    }

    @Test
    public void testEncapsulation() {
        PromotionOrder order = new PromotionOrder(this.activePlan);
        new PromotionPlan("External", 10, 0.1);
        PromotionPlan externalPlan = new PromotionPlan("Another", 20, 0.2);
        Assertions.assertNotEquals(externalPlan, order.getPlan(), "Changing local variable should not affect PromotionOrder object");
    }
}
