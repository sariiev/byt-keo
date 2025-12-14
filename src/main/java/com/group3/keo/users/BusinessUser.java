package com.group3.keo.users;

import com.group3.keo.promotion.PromotionOrder;
import com.group3.keo.promotion.PromotionPlan;
import com.group3.keo.publications.posts.Post;
import com.group3.keo.utils.Utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BusinessUser extends User {
    // region === FIELDS ===
    private String websiteLink;
    private String email;
    private String phoneNumber;

    private final Set<PromotionOrder> promotionOrders = new HashSet<>();
    // endregion

    // region === CONSTRUCTORS ===
    public BusinessUser(String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location,
                        String websiteLink,
                        String email,
                        String phoneNumber) {

        super(username, name, bio, address, location);
        setWebsiteLink(websiteLink);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    protected BusinessUser(UUID uid,
                           String username,
                           String name,
                           String bio,
                           User.Address address,
                           User.Location location,
                           String websiteLink,
                           String email,
                           String phoneNumber) {
        super(uid, username, name, bio, address, location);
        setWebsiteLink(websiteLink);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        Utils.validateWebsiteLink(websiteLink);
        this.websiteLink = websiteLink.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Utils.validateEmail(email);
        this.email = email.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        Utils.validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber.trim();
    }

    public Set<PromotionOrder> getPromotionOrders() {
        return Collections.unmodifiableSet(promotionOrders);
    }

    public int getPromotionOrdersCount() {
        return promotionOrders.size();
    }
    // endregion

    // region === MUTATORS ===
    public PromotionOrder promotePost(PromotionPlan plan, Post post) {
        if (plan == null) {
            throw new IllegalArgumentException("plan cannot be null");
        }
        if (post == null) {
            throw new IllegalArgumentException("post cannot be null");
        }
        if (post.getAuthor() != this) {
            throw new IllegalArgumentException("Only the author can promote their own post");
        }

        return new PromotionOrder(plan, this, post);
    }

    public void addPromotionOrderInternal(PromotionOrder order) {
        if (order != null) {
            promotionOrders.add(order);
        }
    }

    public void removePromotionOrderInternal(PromotionOrder order) {
        if (order != null) {
            promotionOrders.remove(order);
        }
    }

    public boolean hasPromotionOrder(PromotionOrder order) {
        return order != null && promotionOrders.contains(order);
    }

    @Override
    public void delete() {
        for (PromotionOrder order : new HashSet<>(promotionOrders)) {
            order.delete();
        }

        super.delete();
    }
    //endregion
}
