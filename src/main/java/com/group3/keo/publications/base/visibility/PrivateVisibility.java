package com.group3.keo.publications.base.visibility;

import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.users.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PrivateVisibility implements PublicationVisibility {
    private final Set<User> allowedUsers = new HashSet<>();

    public PrivateVisibility(User author) {
        allowedUsers.add(author);
    }

    @Override
    public boolean canView(User user) {
        return allowedUsers.contains(user);
    }

    @Override
    public void onStart(PublicationBase publication) {
        for (User user : allowedUsers) {
            user.addAccessiblePrivatePublicationInternal(publication);
        }
    }

    @Override
    public void onEnd(PublicationBase publication) {
        for (User user : allowedUsers) {
            user.removeAccessiblePrivatePublicationInternal(publication);
        }
    }

    public void addAllowedUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        allowedUsers.add(user);
    }
    public void removeAllowedUser(User user) {
        if (user == null || !allowedUsers.contains(user)) {
            return;
        }

        allowedUsers.remove(user);
    }

    public Set<User> getAllowedUsers() {
        return Collections.unmodifiableSet(allowedUsers);
    }
}
