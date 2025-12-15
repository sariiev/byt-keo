package com.group3.keo.promotion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.enums.PromotionStatus;
import com.group3.keo.publications.posts.Post;
import com.group3.keo.users.BusinessUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class PromotionOrder {
    // region === EXTENT ===
    private static final Map<UUID, PromotionOrder> extent = new HashMap<>();
    // endregion

    // region === FIELDS ===
    private final UUID uid;
    private PromotionPlan plan;
    private final boolean isCustomOrder;
    private final int views;

    private final double pricePerView;

    private final LocalDateTime creationDateTime;
    private PromotionStatus status = PromotionStatus.IN_PROGRESS;

    private final BusinessUser businessUser;
    private final Post post;
    // endregion

    // region === CONSTRUCTORS ===
    public PromotionOrder(PromotionPlan plan, BusinessUser businessUser, Post post) {
        if (plan == null) {
            throw new IllegalArgumentException("plan cannot be null");
        }

        if (businessUser == null) {
            throw new IllegalArgumentException("businessUser cannot be null");
        }

        if (post == null) {
            throw new IllegalArgumentException("post cannot be null");
        }

        if (!plan.isActive()) {
            throw new IllegalStateException("Cannot create order: plan is inactive");
        }

        if (post.getAuthor() != businessUser) {
            throw new IllegalArgumentException("Only the author (BusinessUser) can promote their own post");
        }

        if (plan.isCustom()) {
            this.plan = null;
            this.isCustomOrder = true;

        } else {
            this.plan = plan;
            this.isCustomOrder = false;

            plan.addOrderInternal(this);
        }

        this.uid = UUID.randomUUID();
        this.views = plan.getViews();
        this.pricePerView = plan.getPricePerView();
        this.creationDateTime = LocalDateTime.now();
        this.businessUser = businessUser;
        this.post = post;

        extent.put(uid, this);

        businessUser.addPromotionOrderInternal(this);
        post.addPromotionOrderInternal(this);
    }

    private PromotionOrder(UUID uid, PromotionPlan plan, boolean isCustomOrder, int views, double pricePerView, LocalDateTime creationDateTime, PromotionStatus status, BusinessUser businessUser, Post post) {
        if (isCustomOrder) {
            this.plan = null;
            this.isCustomOrder = true;

        } else {
            this.plan = plan;
            this.isCustomOrder = false;

            if (plan != null) {
                plan.addOrderInternal(this);
            }
        }

        this.uid = uid;
        this.views = views;
        this.pricePerView = pricePerView;
        this.creationDateTime = creationDateTime;
        this.status = status;
        this.businessUser = businessUser;
        this.post = post;

        extent.put(uid, this);

        if (businessUser != null) {
            businessUser.addPromotionOrderInternal(this);
        }
        if (post != null) {
            post.addPromotionOrderInternal(this);
        }
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    public boolean isCustomOrder() {
        return isCustomOrder;
    }

    public PromotionPlan getPlan() {
        return plan;
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

        if (this.status == PromotionStatus.COMPLETED || this.status == PromotionStatus.CANCELED) {
            throw new IllegalStateException("status cannot be changed after completion or cancellation");
        }
        this.status = status;
    }

    public int getViews() {
        return views;
    }

    public double getTotalPrice() {
        return views * pricePerView;
    }

    public BusinessUser getBusinessUser() {
        return businessUser;
    }

    public Post getPost() {
        return post;
    }
    // endregion

    // region === MUTATORS ===
    void clearPlanInternal() {
        this.plan = null;
    }

    public void delete() {
        if (plan != null) {
            plan.removeOrderInternal(this);
        }
        
        if (businessUser != null) {
            businessUser.removePromotionOrderInternal(this);
        }
        if (post != null) {
            post.removePromotionOrderInternal(this);
        }

        extent.remove(this.uid);
    }
    //endregion

    // region === EQUALS & HASHCODE ===
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PromotionOrder that)) return false;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
    // endregion

    // region === EXTENT ACCESS ===
    public static Map<UUID, PromotionOrder> getExtent() {
        return Collections.unmodifiableMap(extent);
    }
    // endregion

    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<PromotionOrderDTO> dtos = new ArrayList<>();
            for (PromotionOrder promotionOrder : extent.values()) {
                dtos.add(promotionOrder.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<PromotionOrderDTO>>(){}.getType();
            List<PromotionOrderDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (PromotionOrderDTO dto : loaded) {
                    fromDto(dto);
                }
            }
        } catch (Exception ex) {
            extent.clear();
            ex.printStackTrace();
        }
    }
    // endregion

    // region === DTO CONVERSION ===
    private PromotionOrderDTO toDto() {
        PromotionOrderDTO dto = new PromotionOrderDTO();
        dto.uid = uid;
        if (plan != null) {
            dto.plan = plan.getUid();
        }
        dto.isCustomOrder = isCustomOrder;
        dto.views = views;
        dto.pricePerView = pricePerView;
        dto.creationDateTime = creationDateTime.toString();
        dto.status = status;

        if (businessUser != null) {
            dto.businessUser = businessUser;
        }
        if (post != null) {
            dto.post = post;
        }

        return dto;
    }

    private static PromotionOrder fromDto(PromotionOrderDTO dto) {
        PromotionPlan promotionPlan = null;
        if (dto.plan != null) {
            promotionPlan = PromotionPlan.getExtent().get(dto.plan);
        }

        BusinessUser businessUser = null;
        if (dto.businessUser != null) {
            businessUser = (BusinessUser) com.group3.keo.users.User.getExtent().get(dto.businessUser);
        }

        Post post = null;
        if (dto.post != null) {
            post = (Post) com.group3.keo.publications.base.PublicationBase.getExtent().get(dto.post);
        }

        return new PromotionOrder(dto.uid, promotionPlan, dto.isCustomOrder, dto.views, dto.pricePerView, LocalDateTime.parse(dto.creationDateTime), dto.status, businessUser, post);
    }
    // endregion
}