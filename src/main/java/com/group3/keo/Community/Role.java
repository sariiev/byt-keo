package com.group3.keo.Community;

import com.group3.keo.Enums.RoleType;
import com.group3.keo.Users.PersonalUser;

public class Role {

    private RoleType roleType;      // HEAD, EDITOR, MEMBER
    private Community community;
    private PersonalUser user;

    public Role(RoleType roleType, Community community, PersonalUser user) {
        setRoleType(roleType);
        setCommunity(community);
        setUser(user);
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

}
