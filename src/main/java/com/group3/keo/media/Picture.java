package com.group3.keo.media;

import com.group3.keo.community.Community;
import com.group3.keo.users.User;

import java.util.UUID;

public class Picture extends VisualAttachment {
    // region === FIELDS ===
    private boolean isAnimated;

    private User profilePictureOf;
    private Community communityPictureOf;
    // endregion

    // region === CONSTRUCTORS ===
    public Picture(String source,
                   int fileSize,
                   int width,
                   int height,
                   boolean isAnimated) {

        super(source, fileSize, width, height);
        this.isAnimated = isAnimated;
        this.profilePictureOf = null;
    }

    protected Picture(UUID uid,
                   String source,
                   int fileSize,
                   int width,
                   int height,
                   boolean isAnimated) {

        super(uid, source, fileSize, width, height);
        this.isAnimated = isAnimated;
        this.profilePictureOf = null;
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public User getProfilePictureOf() {
        return profilePictureOf;
    }

    public void setAsProfilePictureOf(User user) {
        if (user == this.profilePictureOf) {
            return;
        }

        if (this.profilePictureOf != null) {
            User oldUser = this.profilePictureOf;
            this.profilePictureOf = null;
            oldUser.clearProfilePictureInternal();
        }

        this.profilePictureOf = user;

        if (user != null) {
            user.setProfilePictureInternal(this);
        }
    }

    public void setProfilePictureOfInternal(User user) {
        this.profilePictureOf = user;
    }

    public Community getCommunityPictureOf() {
        return communityPictureOf;
    }

    public void setAsCommunityPictureOf(Community community) {
        if (community == this.communityPictureOf) {
            return;
        }

        if (this.communityPictureOf != null) {
            Community oldCommunity = this.communityPictureOf;
            this.communityPictureOf = null;
            oldCommunity.clearCommunityPictureInternal();
        }

        this.communityPictureOf = community;

        if (community != null) {
            community.setCommunityPictureInternal(this);
        }
    }

    public void setCommunityPictureOfInternal(Community community) {
        this.communityPictureOf = community;
    }
    // endregion

    // region == MUTATORS ==
    public void removeAsProfilePicture() {
        setAsProfilePictureOf(null);
    }

    public void clearProfilePictureOfInternal() {
        this.profilePictureOf = null;
    }

    public void removeAsCommunityPicture() {
        setAsCommunityPictureOf(null);
    }

    public void clearCommunityPictureOfInternal() {
        this.communityPictureOf = null;
    }

    @Override
    public void delete() {
        // Clear profile picture association before deleting
        if (profilePictureOf != null) {
            profilePictureOf.clearProfilePictureInternal();
            profilePictureOf = null;
        }

        if (communityPictureOf != null) {
            communityPictureOf.clearCommunityPictureInternal();
            communityPictureOf = null;
        }

        super.delete();
    }
    //endregion
}
