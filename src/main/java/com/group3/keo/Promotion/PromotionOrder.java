package com.group3.keo.Promotion;

import com.group3.keo.Enums.PromotionStatus;

import java.time.LocalDateTime;

public class PromotionOrder {

    private final PromotionPlan plan;
    private final Integer customViews;

    private final double pricePerView;

    private final LocalDateTime creationDateTime;
    private PromotionStatus status;

    public PromotionOrder(PromotionPlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("plan cannot be null");
        }
        this.plan = plan;
        this.customViews = plan.getViews();
        this.pricePerView = plan.getPricePerView();
        this.creationDateTime = LocalDateTime.now();
        this.status = PromotionStatus.IN_PROGRESS;
    }

    // ---------- getters ----------

    public PromotionPlan getPlan() {
        return plan;
    }

    public Integer getCustomViews() {
        return customViews;
    }

    public double getPricePerView() {
        return pricePerView;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public PromotionStatus getStatus() {
        return status;
    }

    public void setStatus(PromotionStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        this.status = status;
    }

    public int getViews() {
        return (customViews != null) ? customViews : plan.getViews();
    }

    public double getTotalPrice() {
        return getViews() * pricePerView;
    }
}