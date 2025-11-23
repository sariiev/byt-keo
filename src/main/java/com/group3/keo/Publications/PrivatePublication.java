package com.group3.keo.Publications;

import java.util.Set;

public interface PrivatePublication {
    Set<String> getAllowedUsers();
    boolean canView(String userId);
}
