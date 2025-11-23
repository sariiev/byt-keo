package com.group3.keo.Promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromotionPlan {

    public static final int MinimumCustomViews = 1000;
    public static final int MaximumCustomViews = 1_000_000;

    private String name;
    private int views;
    private double pricePerView;
    private boolean isCustom;

    private final List<PromotionOrder> orders = new ArrayList<>();

    public PromotionPlan(String name, int views, double pricePerView, boolean isCustom) {
        setName(name);
        setCustom(isCustom);
        setViews(views);
        setPricePerView(pricePerView);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.name = name.trim();
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        if (views <= 0) {
            throw new IllegalArgumentException("views must be positive");
        }

        if (isCustom) {
            validateCustomViews(views);
        }

        this.views = views;
    }

    public double getPricePerView() {
        return pricePerView;
    }

    public void setPricePerView(double pricePerView) {
        if (pricePerView < 0) {
            throw new IllegalArgumentException("pricePerView cannot be negative");
        }
        this.pricePerView = pricePerView;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public double getTotalPrice() {
        return views * pricePerView;
    }

    public List<PromotionOrder> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public PromotionOrder createOrder() {
        PromotionOrder order = new PromotionOrder(this);
        orders.add(order);
        return order;
    }

    void validateCustomViews(Integer customViews) {
        if (!isCustom) {
            if (customViews != null) {
                throw new IllegalArgumentException(
                        "Custom views are not allowed for non-custom promotion plans");
            }
            return;
        }

        if (customViews == null) {
            throw new IllegalArgumentException(
                    "Custom views must be provided for custom promotion plans");
        }
        if (customViews < MinimumCustomViews || customViews > MaximumCustomViews) {
            throw new IllegalArgumentException(
                    "Custom views must be between " + MinimumCustomViews +
                            " and " + MaximumCustomViews);
        }
    }
}