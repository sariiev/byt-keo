package com.group3.keo.promotion;

import com.group3.keo.enums.PromotionStatus;
import com.group3.keo.publications.posts.Post;
import com.group3.keo.users.BusinessUser;

import java.util.UUID;

public class PromotionOrderDTO {
    public UUID uid;
    public UUID plan;
    public boolean isCustomOrder;
    public int views;
    public double pricePerView;
    public String creationDateTime;
    public PromotionStatus status;
    public BusinessUser businessUser;
    public Post post;
}
