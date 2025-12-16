package com.group3.keo.associationsTests;

import com.group3.keo.promotion.PromotionOrder;
import com.group3.keo.promotion.PromotionPlan;
import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.users.BusinessUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class PromotionPlanAssociationTest {
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
        plan = new PromotionPlan("Basic Plan", 100, 0.05);
        post = new PublicPost(businessUser, "caption", new ArrayList<>());
    }

    @Test
    void creatingPlanRegistersInExtent() {
        Assertions.assertTrue(PromotionPlan.getExtent().containsValue(plan));
    }


    @Test
    void deletingPlanClearsAllOrders() {
        PromotionOrder order1 = new PromotionOrder(plan, businessUser, post);
        PromotionOrder order2 = new PromotionOrder(plan, businessUser, post);

        plan.delete();

        Assertions.assertFalse(PromotionPlan.getExtent().containsValue(plan));
        Assertions.assertTrue(plan.getOrders().isEmpty());
        Assertions.assertNull(order1.getPlan());
        Assertions.assertNull(order2.getPlan());
    }

    @Test
    void customPlanDoesNotStoreOrders() {
        PromotionPlan customPlan = new PromotionPlan(5000, 0.1); // custom plan

        PromotionOrder order = new PromotionOrder(customPlan, businessUser, post);

        Assertions.assertNull(order.getPlan());
        Assertions.assertTrue(customPlan.getOrders().isEmpty());
    }
}
