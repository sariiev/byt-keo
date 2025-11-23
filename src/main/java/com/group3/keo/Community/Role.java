package com.group3.keo.Community;

import java.util.HashSet;
import java.util.Set;

import com.group3.keo.Enums.RoleType;
import com.group3.keo.Users.PersonalUser;

public class Role {

    private static final Set<Role> extent = new HashSet<>();

    private RoleType roleType;      // HEAD, EDITOR, MEMBER
    private Community community;
    private PersonalUser user;

    public Role(RoleType roleType, Community community, PersonalUser user) {
        setRoleType(roleType);
        setCommunity(community);
        setUser(user);

        for (Role r : extent) {
            if (r.getCommunity().equals(community) && r.getUser().equals(user)) {
                throw new IllegalStateException("A role for this (Community, User) pair already exists");
            }
        }

        extent.add(this);
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        if (roleType == null) {
            throw new IllegalArgumentException("roleType cannot be null");
        }
        this.roleType = roleType;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        if (community == null) {
            throw new IllegalArgumentException("community cannot be null");
        }
        this.community = community;
    }

    public PersonalUser getUser() {
        return user;
    }

    public void setUser(PersonalUser user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        this.user = user;
    }

    public static Set<Role> getExtent() {
        return extent;
    }
}
