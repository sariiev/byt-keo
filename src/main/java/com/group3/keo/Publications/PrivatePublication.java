package com.group3.keo.Publications;

import com.group3.keo.Users.User;

import java.util.Set;

public interface PrivatePublication {
    Set<User> getAllowedUsers();
    boolean canView(User user);
}
