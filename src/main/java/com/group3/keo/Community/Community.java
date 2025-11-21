package com.group3.keo.Community;

import com.group3.keo.Enums.CommunityTopic;
import com.group3.keo.Enums.RoleType;
import com.group3.keo.MediaAttachments.Picture;
import com.group3.keo.Users.PersonalUser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Community {

    public static final int MaximumNameLength = 16;

    private String id;
    private String name;
    private Picture avatar;
    private CommunityTopic topic;

    private final Set<Role> roles = new HashSet<>();

    public Community(String id, String name, CommunityTopic topic, Picture avatar) {
        setId(id);
        setName(name);
        setTopic(topic);
        setAvatar(avatar);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        String trimmed = name.trim();
        if (trimmed.length() > MaximumNameLength) {
            throw new IllegalArgumentException(
                    "name length must be ≤ " + MaximumNameLength
            );
        }
        this.name = trimmed;
    }

    public CommunityTopic getTopic() {
        return topic;
    }

    public void setTopic(CommunityTopic topic) {
        if (topic == null) {
            throw new IllegalArgumentException("topic cannot be null");
        }
        this.topic = topic;
    }

    // --- roles ---

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void addMemberWithRole(PersonalUser user, RoleType roleType) {
        if (user == null || roleType == null) {
            throw new IllegalArgumentException("user and roleType cannot be null");
        }

        if (roleType == RoleType.HEAD) {
            for (Role r : roles) {
                if (r.getRoleType() == RoleType.HEAD) {
                    throw new IllegalStateException("Community already has a Head");
                }
            }
        }

        Role role = new Role(roleType, this, user);
        roles.add(role);
        user.addRole(role);
    }

    public void removeMember(PersonalUser user) {
        if (user == null) return;

        Role toRemove = null;
        for (Role r : roles) {
            if (r.getUser().equals(user)) {
                toRemove = r;
                break;
            }
        }
        if (toRemove != null) {
            roles.remove(toRemove);
            user.removeRole(toRemove);
        }
    }

    public Picture getAvatar() {
        return avatar;
    }

    public void setAvatar(Picture avatar) {
        this.avatar = avatar;
    }
}
