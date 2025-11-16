package com.group3.keo;


import java.time.LocalDateTime;

public abstract class PublicationBase {

    public static final int MaximumCaptionLength = 1000;

    private String caption;

    private LocalDateTime publicationDateTime;

    private int likes = 0;
    private int views = 0;

    private boolean wasEdited = false;

    protected PublicationBase(String caption, LocalDateTime publicationDateTime) {
        setCaption(caption);
        setPublicationDateTime(publicationDateTime);
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        if (caption != null && caption.length() > MaximumCaptionLength) {
            throw new IllegalArgumentException(
                    "Caption length must be ≤ " + MaximumCaptionLength
            );
        }

        if (this.caption != null && caption != null && !caption.equals(this.caption)) {
            this.wasEdited = true;
        }

        this.caption = caption;
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
}
