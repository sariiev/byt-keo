package com.group3.keo.publications.comments;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.publications.base.visibility.PublicationVisibility;
import com.group3.keo.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Comment extends PublicationBase {
    // region === FIELDS ===
    private PublicationBase commentedPublication;
    // endregion

    // region === CONSTRUCTORS ===
    public Comment(PublicationAuthor author,
                   String caption,
                   PublicationBase commentedPublication,
                   List<MediaAttachment> attachments) {
        super(author, caption, attachments, commentedPublication.getVisibility());
        if (commentedPublication == null) {
            throw new IllegalArgumentException("publication cannot be null");
        }
        this.commentedPublication = commentedPublication;
        commentedPublication.addComment(this);
    }

    public Comment(UUID uid,
                   PublicationAuthor author,
                   String caption,
                   PublicationBase commentedPublication,
                   List<MediaAttachment> attachments,
                   LocalDateTime publicationDateTime,
                   int views,
                   boolean wasEdited) {
        super(uid, author, caption, attachments, publicationDateTime, views, wasEdited, commentedPublication.getVisibility());
        if (commentedPublication == null) {
            throw new IllegalArgumentException("publication cannot be null");
        }
        this.commentedPublication = commentedPublication;
        commentedPublication.addComment(this);
    }
    // endregion

    // region === MUTATORS ===
    public void detachFromPublication() {
        if (commentedPublication == null) return;

        PublicationBase commentedPublication = this.commentedPublication;
        this.commentedPublication = null;

        commentedPublication.internalRemoveComment(this);
    }

    @Override
    public void delete() {
        detachFromPublication();
        super.delete();
    }

    @Override
    public void addAllowedUser(User user) {
        throw new UnsupportedOperationException("Comment visibility cannot be modified");
    }

    @Override
    public void removeAllowedUser(User user) {
        throw new UnsupportedOperationException("Comment visibility cannot be modified");
    }

    @Override
    public Set<User> getAllowedUsers() {
        return commentedPublication.getAllowedUsers();
    }

    // endregion

    // region === GETTERS & SETTERS ===
    public PublicationBase getCommentedPublication() {
        return commentedPublication;
    }

    @Override
    public PublicationVisibility getVisibility() {
        return commentedPublication.getVisibility();
    }

    @Override
    public boolean canView(User user) {
        return commentedPublication.canView(user);
    }

    // endregion
}
