package com.group3.keo.media;

import com.group3.keo.users.User;

import java.util.UUID;

public class Picture extends VisualAttachment {
    // region === FIELDS ===
    private boolean isAnimated;

    private User profilePictureOf;
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
    // endregion

    // region == MUTATORS ==
    public void removeAsProfilePicture() {
        setAsProfilePictureOf(null);
    }

    public void clearProfilePictureOfInternal() {
        this.profilePictureOf = null;
    }

    @Override
    public void delete() {
        // Clear profile picture association before deleting
        if (profilePictureOf != null) {
            profilePictureOf.clearProfilePictureInternal();
            profilePictureOf = null;
        }
        super.delete();
    }
    //endregion
}
