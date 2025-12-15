package com.group3.keo.publications.base;

import com.group3.keo.community.Community;

public interface PublicPublication {
    Community getPublishedByCommunity();
    boolean isPublishedByCommunity();

    void setPublishedByCommunity(Community community);
    void setPublishedByCommunityInternal(Community community);
    void clearPublishedByCommunityInternal();
}
