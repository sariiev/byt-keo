package com.group3.keo.publications.posts;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PrivatePublication;
import com.group3.keo.users.User;

import java.time.LocalDateTime;
import java.util.*;

public class PrivatePost extends Post implements PrivatePublication {
    private final Set<User> allowedUsers = new HashSet<>();

    public PrivatePost(User author, String caption, List<MediaAttachment> attachments, Set<User> allowedUsers) {
        super(author, caption, attachments);

        if (allowedUsers != null) {
            this.allowedUsers.addAll(allowedUsers);
        }
        this.addAllowedUser(author);
    }

    public PrivatePost(UUID uid, User author, String caption, List<MediaAttachment> attachments, Set<User> allowedUsers, LocalDateTime publicationDateTime, int views, boolean wasEdited, boolean wasPromoted) {
        super(uid, author, caption, attachments, publicationDateTime, views, wasEdited, wasPromoted);

        if (allowedUsers != null) {
            this.allowedUsers.addAll(allowedUsers);
        }
        this.addAllowedUser(author);
    }

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

    @Override
    public Set<User> getAllowedUsers() {
        return Collections.unmodifiableSet(allowedUsers);
    }

    @Override
    public boolean canView(User user) {
        return allowedUsers.contains(user);
    }
}
