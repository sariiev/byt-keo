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

    private String name;
    private Picture avatar;
    private CommunityTopic topic;

    public Community(String id, String name, CommunityTopic topic, Picture avatar) {
        setName(name);
        setTopic(topic);
        setAvatar(avatar);
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

    public Picture getAvatar() {
        return avatar;
    }

    public void setAvatar(Picture avatar) {
        this.avatar = avatar;
    }
}
