package com.group3.keo.community;

import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.media.Picture;
import com.group3.keo.utils.Utils;

public class Community {

    public static final int MAX_NAME_LENGTH = 16;

    private String name;
    private Picture avatar;
    private CommunityTopic topic;

    public Community(String name, CommunityTopic topic, Picture avatar) {
        setName(name);
        setTopic(topic);
        setAvatar(avatar);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Utils.validateNonEmpty(name, "name");
        Utils.validateMaxLength(name, "name", MAX_NAME_LENGTH);
        this.name = name.trim();
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
