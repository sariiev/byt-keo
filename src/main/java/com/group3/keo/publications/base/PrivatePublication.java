package com.group3.keo.publications.base;

import com.group3.keo.users.User;

import java.util.Set;

public interface PrivatePublication {
    boolean canView(User user);

    void addAllowedUser(User user);
    void removeAllowedUser(User user);

    Set<User> getAllowedUsers();
}
