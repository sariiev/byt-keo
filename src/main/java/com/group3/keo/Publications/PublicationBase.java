package com.group3.keo.Publications;

import com.group3.keo.MediaAttachments.MediaAttachment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PublicationBase {

    public static final int MaximumCaptionLength = 1000;

    private String caption;
    private final List<MediaAttachment> attachments = new ArrayList<>();

    private LocalDateTime publicationDateTime;

    private int likes = 0;
    private int views = 0;

    private boolean wasEdited = false;

    protected PublicationBase(String caption, LocalDateTime publicationDateTime) {
        setPublicationDateTime(publicationDateTime);
        setCaption(caption);
    }

    private boolean hasEmptyCaption() {
        return caption == null || caption.isBlank();
    }

    private void ensureContentNotEmpty() {
        if (hasEmptyCaption() && attachments.isEmpty()) {
            throw new IllegalStateException(
                    "Publication must have a non-empty caption or at least one attachment."
            );
        }
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {

        if (caption == null) {
            this.caption = null;
            ensureContentNotEmpty();
            return;
        }

        String trimmed = caption.trim();

        if (trimmed.isEmpty()) {
            this.caption = null;
            ensureContentNotEmpty();
            return;
        }

        if (trimmed.length() > MaximumCaptionLength) {
            throw new IllegalArgumentException(
                    "Caption length must be ≤ " + MaximumCaptionLength
            );
        }

        if (this.caption != null && !trimmed.equals(this.caption)) {
            this.wasEdited = true;
        }

        this.caption = trimmed;
    }

    public LocalDateTime getPublicationDateTime() {
        return publicationDateTime;
    }

    public void setPublicationDateTime(LocalDateTime publicationDateTime) {
        if (publicationDateTime == null) {
            throw new IllegalArgumentException("PublicationDateTime cannot be null");
        }
        this.publicationDateTime = publicationDateTime;
    }

    public int getLikes() {
        return likes;
    }

    public void addLike() {
        likes++;
    }

    public int getViews() {
        return views;
    }

    public void addView() {
        views++;
    }

    public boolean isWasEdited() {
        return wasEdited;
    }

    public List<MediaAttachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public void addAttachment(MediaAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("Attachment cannot be null");
        }
        attachments.add(attachment);
    }

    public void removeAttachment(MediaAttachment attachment) {
        if (attachment == null || !attachments.contains(attachment)) {
            return;
        }

        if (attachments.size() == 1 && hasEmptyCaption()) {
            throw new IllegalStateException(
                    "Cannot remove the last attachment when the caption is empty."
            );
        }

        attachments.remove(attachment);
    }
}
