package com.group3.keo.Users;


import com.group3.keo.Community.Role;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PersonalUser extends User {

    private final Set<Role> roles = new HashSet<>();

    public PersonalUser(String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location) {
        super(username, name, bio, address, location);
    }

    // --- roles ---

    Set<Role> getRolesInternal() {
        return roles;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

}
