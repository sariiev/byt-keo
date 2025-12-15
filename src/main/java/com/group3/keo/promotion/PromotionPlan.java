package com.group3.keo.promotion;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class PromotionPlan {
    // region === CONSTANTS ===
    public static final int MIN_CUSTOM_VIEWS = 1000;
    public static final int MAX_CUSTOM_VIEWS = 1_000_000;
    public static final int MAX_NAME_LENGTH = 16;
    // endregion

    // region === EXTENT ===
    private static final Map<UUID, PromotionPlan> extent = new HashMap<>();
    // endregion

    // region === FIELDS ===
    private final UUID uid;
    private final String name;
    private final int views;
    private final double pricePerView;
    private final boolean isCustom;

    private boolean isActive = true;

    private final Set<PromotionOrder> orders = new HashSet<>();
    // endregion

    // region === CONSTRUCTORS ===
    public PromotionPlan(String name, int views, double pricePerView) {
        Utils.validateNonEmpty(name, "name");
        Utils.validateMaxLength(name, "name", MAX_NAME_LENGTH);

        if (views <= 0) {
            throw new IllegalArgumentException("views must be positive");
        }

        if (pricePerView < 0) {
            throw new IllegalArgumentException("pricePerView cannot be negative");
        }

        this.uid = UUID.randomUUID();
        this.isCustom = false;
        this.name = name.trim();
        this.views = views;
        this.pricePerView = pricePerView;
        extent.put(uid, this);
    }

    public PromotionPlan(int customViews, double pricePerView) {
        if (customViews < MIN_CUSTOM_VIEWS || customViews > MAX_CUSTOM_VIEWS) {
            throw new IllegalArgumentException("customViews must be between " + MIN_CUSTOM_VIEWS + " and " + MAX_CUSTOM_VIEWS);
        }

        if (pricePerView < 0) {
            throw new IllegalArgumentException("pricePerView cannot be negative");
        }

        this.uid = null;
        this.isCustom = true;
        this.name = null;
        this.views = customViews;
        this.pricePerView = pricePerView;
    }

    private PromotionPlan(UUID uid, String name, int views, double pricePerView, boolean isActive) {
        Utils.validateNonEmpty(name, "name");
        Utils.validateMaxLength(name, "name", MAX_NAME_LENGTH);

        if (views <= 0) {
            throw new IllegalArgumentException("views must be positive");
        }

        if (pricePerView < 0) {
            throw new IllegalArgumentException("pricePerView cannot be negative");
        }

        this.uid = uid;
        this.isCustom = false;
        this.name = name.trim();
        this.views = views;
        this.pricePerView = pricePerView;
        this.isActive = isActive;
        extent.put(uid, this);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getViews() {
        return views;
    }

    public double getPricePerView() {
        return pricePerView;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public double getTotalPrice() {
        return views * pricePerView;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Set<PromotionOrder> getOrders() {
        return Collections.unmodifiableSet(orders);
    }
    // endregion

    //region == MUTATORS ==
    public void addOrderInternal(PromotionOrder order) {
        if (order != null) {
            orders.add(order);
        }
    }

    public void removeOrderInternal(PromotionOrder order) {
        if (order != null) {
            orders.remove(order);
        }
    }

    public void delete() {
        for (PromotionOrder order : new HashSet<>(orders)) {
            order.clearPlanInternal();
        }
        orders.clear();

        extent.remove(this.uid);
    }
    //endregion

    // region === EQUALS & HASHCODE ===

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PromotionPlan that)) return false;

        if (this.isCustom || that.isCustom) {
            return this == that;
        }

        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }

    // endregion

    // region === EXTENT ACCESS ===
    public static Map<UUID, PromotionPlan> getExtent() {
        return Collections.unmodifiableMap(extent);
    }
    // endregion

    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<PromotionPlanDTO> dtos = new ArrayList<>();
            for (PromotionPlan promotionPlan : extent.values()) {
                dtos.add(promotionPlan.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<PromotionPlanDTO>>(){}.getType();
            List<PromotionPlanDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (PromotionPlanDTO dto : loaded) {
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
    private PromotionPlanDTO toDto() {
        PromotionPlanDTO dto = new PromotionPlanDTO();
        dto.uid = uid;
        dto.name = name;
        dto.views = views;
        dto.pricePerView = pricePerView;
        dto.isActive = isActive;

        return dto;
    }

    private static PromotionPlan fromDto(PromotionPlanDTO dto) {
        return new PromotionPlan(dto.uid, dto.name, dto.views, dto.pricePerView, dto.isActive);
    }
    // endregion
}