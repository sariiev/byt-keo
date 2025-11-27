package com.group3.keo.publications;

import com.group3.keo.users.User;

import java.util.Set;

public interface PrivatePublication {
    Set<User> getAllowedUsers();
    boolean canView(User user);
}
