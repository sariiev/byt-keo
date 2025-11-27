package com.group3.keo.publications;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.users.User;
import com.group3.keo.utils.Utils;

import java.time.LocalDateTime;
import java.util.*;

public abstract class PublicationBase {
    // region === CONSTANTS ===
    public static final int MAX_CAPTION_LENGTH = 1000;
    public static final int MAX_ATTACHMENTS_SIZE = 10;
    // endregion

    // region === FIELDS ===
    private UUID uid;
    private String caption;
    private final List<MediaAttachment> attachments = new ArrayList<>();
    private final LocalDateTime publicationDateTime;
    private int views = 0;
    private boolean wasEdited = false;

    private final Set<User> likedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    // endregion

    // region === CONSTRUCTORS ===
    protected PublicationBase(String caption, LocalDateTime publicationDateTime, List<MediaAttachment> attachments) {
        if (publicationDateTime == null) {
            throw new IllegalArgumentException("PublicationDateTime cannot be null");
        }
        this.publicationDateTime = publicationDateTime;
        setCaption(caption);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    private boolean hasCaption() {
        return caption != null && !caption.isBlank();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        String oldCaption = this.caption;

        if (caption == null || caption.trim().isEmpty()) {
            if (attachments.isEmpty()) {
                throw new IllegalStateException("Publication must have a non-empty caption or at least one attachment.");
            }

            this.caption = null;

            if (oldCaption != null) {
                wasEdited = true;
            }

            return;
        }

        Utils.validateMaxLength(caption, "caption", MAX_CAPTION_LENGTH);

        String trimmed = caption.trim();

        this.caption = trimmed;

        if (!trimmed.equals(oldCaption)) {
            this.wasEdited = true;
        }
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

    public boolean wasEdited() {
        return wasEdited;
    }

    public void setWasEdited(boolean wasEdited) {
        this.wasEdited = wasEdited;
    }

    public List<MediaAttachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }
    // endregion

    // region === MUTATORS ===
    private void addAttachments(List<MediaAttachment> attachments) {
        if (attachments == null) return;

        for (MediaAttachment attachment : attachments) {
            addAttachment(attachment);
        }
    }

    public void addAttachment(MediaAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("attachment cannot be null");
        }

        if (attachments.size() >= MAX_ATTACHMENTS_SIZE) {
            throw new IllegalStateException("Publication cannot have more than " + MAX_ATTACHMENTS_SIZE + " attachments");
        }

        attachments.add(attachment);
    }

    public void removeAttachment(MediaAttachment attachment) {
        if (attachment == null || !attachments.contains(attachment)) {
            return;
        }

        if (!hasCaption() && attachments.size() == 1) {
            throw new IllegalStateException("Publication must have a non-empty caption or at least one attachment.");
        }

        attachments.remove(attachment);
    }

    public void addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        if (comment.getCommentedPublication() != this) {
            throw new IllegalArgumentException("Comment does not belong to this publication.");
        }
        comments.add(comment);
    }
    // endregion

    // region === EQUALS & HASHCODE ===
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PublicationBase that)) return false;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
    // endregion
}
