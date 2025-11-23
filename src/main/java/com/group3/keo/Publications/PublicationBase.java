package com.group3.keo.Publications;

import com.group3.keo.MediaAttachments.MediaAttachment;
import com.group3.keo.Users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PublicationBase {

    public static final int MaximumCaptionLength = 1000;

    private String caption;
    private final List<MediaAttachment> attachments = new ArrayList<>();

    private final LocalDateTime publicationDateTime;

    private final Set<User> likedBy = new HashSet<>();

    private int views = 0;

    private boolean wasEdited = false;

    private final List<Comment> comments = new ArrayList<>();

    protected PublicationBase(String caption, LocalDateTime publicationDateTime) {
        if (publicationDateTime == null) {
            throw new IllegalArgumentException("PublicationDateTime cannot be null");
        }
        this.publicationDateTime = publicationDateTime;
        setCaption(caption);
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    private boolean hasEmptyCaption() {
        return isNullOrBlank(caption);
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

        if (isNullOrBlank(caption)) {
            this.caption = null;
            ensureContentNotEmpty();
            return;
        }

        String trimmed = caption.trim();

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

    public int getLikes() {
        return likedBy.size();
    }

    public Set<User> getLikedBy() {
        return Collections.unmodifiableSet(likedBy);
    }

    public void addLike(User user) {
        likedBy.add(user);
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

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        if (comment.getPublication() != this) {
            throw new IllegalArgumentException("Comment does not belong to this publication.");
        }
        comments.add(comment);
    }
}
