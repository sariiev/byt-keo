package com.group3.keo.publications.quotes;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PrivatePublication;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.users.User;

import java.time.LocalDateTime;
import java.util.*;

public class PrivateQuote extends Quote implements PrivatePublication {
    // region === FIELDS ===
    private final Set<User> allowedUsers = new HashSet<>();
    // endregion

    // region === CONSTRUCTORS ===
    public PrivateQuote(User author, String caption, PublicationBase referencedPublication, List<MediaAttachment> attachments, Set<User> allowedUsers) {
        super(author, caption, attachments, referencedPublication);

        if (allowedUsers != null) {
            this.allowedUsers.addAll(allowedUsers);
        }
        this.addAllowedUser(author);
    }

    public PrivateQuote(UUID uid, User author, String caption, List<MediaAttachment> attachments, PublicationBase referencedPublication, Set<User> allowedUsers, LocalDateTime publicationDateTime, int views, boolean wasEdited, boolean wasPromoted) {
        super(uid, author, caption, attachments, referencedPublication, publicationDateTime, views, wasEdited, wasPromoted);

        if (allowedUsers != null) {
            this.allowedUsers.addAll(allowedUsers);
        }
        this.addAllowedUser(author);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    @Override
    public Set<User> getAllowedUsers() {
        return Collections.unmodifiableSet(allowedUsers);
    }
    // endregion

    // region === MUTATORS ===
    @Override
    public void addAllowedUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        allowedUsers.add(user);
    }

    @Override
    public void removeAllowedUser(User user) {
        if (user == null || !allowedUsers.contains(user)) {
            return;
        }

        if (getAuthor() instanceof User a && a.equals(user)) {
            throw new IllegalStateException("author cannot be removed from allowedUsers");
        }

        allowedUsers.remove(user);
    }
    // endregion

    // region === HELPERS ===
    @Override
    public boolean canView(User user) {
        return allowedUsers.contains(user);
    }
    // endregion
}
