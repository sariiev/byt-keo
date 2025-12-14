package com.group3.keo.publications.posts;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.promotion.PromotionOrder;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.base.PublicationBase;

import java.time.LocalDateTime;
import java.util.*;

public abstract class Post extends PublicationBase {
    // region === FIELDS ===
    protected boolean wasPromoted = false;

    private final Set<PromotionOrder> promotionOrders = new HashSet<>();
    // endregion

    // region === CONSTRUCTORS ===
    protected Post(PublicationAuthor author, String caption, List<MediaAttachment> attachments) {
        super(author, caption, attachments);
    }

    protected Post(UUID uid, PublicationAuthor author, String caption, List<MediaAttachment> attachments, LocalDateTime publicationDateTime, int views, boolean wasEdited, boolean wasPromoted) {
        super(uid, author, caption, attachments, publicationDateTime, views, wasEdited);
        this.wasPromoted = wasPromoted;
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public boolean wasPromoted() {
        return wasPromoted;
    }

    public void setWasPromoted(boolean wasPromoted) {
        if (this.wasPromoted && !wasPromoted) {
            throw new IllegalStateException("wasPromoted can't be changed from true to false");
        }
        this.wasPromoted = wasPromoted;
    }

    public Set<PromotionOrder> getPromotionOrders() {
        return Collections.unmodifiableSet(promotionOrders);
    }

    public int getPromotionOrdersCount() {
        return promotionOrders.size();
    }
    // endregion

    //region = MUTATORS ==
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
    // endregion
}
