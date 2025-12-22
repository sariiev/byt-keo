package com.group3.keo.associationsTests;

import com.group3.keo.enums.PromotionStatus;
import com.group3.keo.promotion.PromotionOrder;
import com.group3.keo.promotion.PromotionPlan;
import com.group3.keo.publications.posts.Post;
import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.users.BusinessUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class PromotionOrderAssociationTest {
    private PromotionPlan plan;
    private BusinessUser businessUser;
    private PublicPost post;

    private static void clearClassExtent(Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField("extent");
            field.setAccessible(true);

            Object value = field.get(null);

            if (value instanceof Map<?, ?> map) {
                map.clear();
            } else {
                throw new IllegalStateException("Unsupported type of extent in class: " + clazz.getName());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @BeforeEach
    void setup() {
        clearClassExtent(User.class);
        clearClassExtent(PromotionPlan.class);
        clearClassExtent(PromotionOrder.class);

        businessUser = new BusinessUser("user1", "User1", "Bio1",
                new User.Address("Poland", "Warsaw", "Koszykowa"),
                new User.Location(14.13, 21.1), "https://website.com", "email@gmail.com", "+4800000000");
        plan = new PromotionPlan("Basic Plan", 1000, 0.05);
        post = new PublicPost(businessUser, "caption", new ArrayList<>());
    }

    @Test
    void creatingPromotionOrderRegistersInExtentAndAssociations() {
        PromotionOrder order = new PromotionOrder(plan, businessUser, post);

        Assertions.assertTrue(PromotionOrder.getExtent().containsValue(order));
        Assertions.assertTrue(businessUser.getPromotionOrders().contains(order));
        Assertions.assertTrue(post.getPromotionOrders().contains(order));
    }

    @Test
    void deletingPromotionOrderRemovesAssociations() {
        PromotionOrder order = new PromotionOrder(plan, businessUser, post);

        order.delete();

        Assertions.assertFalse(PromotionOrder.getExtent().containsValue(order));
        Assertions.assertFalse(plan.getOrders().contains(order));
        Assertions.assertFalse(businessUser.getPromotionOrders().contains(order));
        Assertions.assertFalse(post.getPromotionOrders().contains(order));
    }

    @Test
    void changingStatusWorksCorrectly() {
        PromotionOrder order = new PromotionOrder(plan, businessUser, post);

        order.setStatus(PromotionStatus.COMPLETED);

        Assertions.assertEquals(PromotionStatus.COMPLETED, order.getStatus());
    }

    @Test
    void cannotChangeStatusAfterCompletionOrCancellation() {
        PromotionOrder order = new PromotionOrder(plan, businessUser, post);

        order.setStatus(PromotionStatus.COMPLETED);

        Assertions.assertThrows(IllegalStateException.class,
                () -> order.setStatus(PromotionStatus.IN_PROGRESS));
    }

    @Test
    void customOrderDoesNotAttachToPlan() {
        PromotionPlan customPlan = new PromotionPlan(10000, 0.1);

        PromotionOrder order = new PromotionOrder(customPlan, businessUser, post);

        Assertions.assertNull(order.getPlan());
        Assertions.assertFalse(customPlan.getOrders().contains(order));
    }
}
