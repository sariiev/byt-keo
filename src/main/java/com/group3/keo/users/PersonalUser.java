package com.group3.keo.users;

import com.group3.keo.community.Community;
import com.group3.keo.community.Role;
import com.group3.keo.enums.RoleType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PersonalUser extends User {
    // region === FIELDS ===
    private final Set<Role> roles = new HashSet<>();
    // endregion

    // region === CONSTRUCTORS ===
    public PersonalUser(String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location) {
        super(username, name, bio, address, location);
    }

    protected PersonalUser(UUID uid,
                           String username,
                           String name,
                           String bio,
                           User.Address address,
                           User.Location location) {
        super(uid, username, name, bio, address, location);
    }
    // endregion

    // region === GETTERS & SETTERS ===

    @Override
    public void setUsername(String username) {
        String oldUsername = this.getUsername();

        if (roles != null && oldUsername != null) {
            super.setUsername(username);
            for (Role role : new HashSet<>(roles)) {
                role.getCommunity().updateUsername(this, oldUsername);
            }
        } else {
            super.setUsername(username);
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    // endregion

    // region === HELPERS ===
    public void internalAddRole(Role role) {
        roles.add(role);
    }

    public void internalRemoveRole(Role role) {
        roles.remove(role);
    }
    // endregion

    // region === MUTATORS ===
    public Role joinCommunity(Community community, RoleType type) {
        return community.addMember(this, type);
    }

    public void leaveCommunity(Community community) {
        community.removeMember(this);
    }

    public void delete() {
        for (Role role : new HashSet<>(roles)) {
            if (role.getRoleType() == RoleType.HEAD) {
                role.getCommunity().reassignHead(this);
            }
        }

        for (Role role : new HashSet<>(roles)) {
            role.delete();
        }
        super.delete();
    }
    // endregion
}
