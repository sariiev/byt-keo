package com.group3.keo.media;

import java.util.UUID;

public class Picture extends VisualAttachment {
    // region === FIELDS ===
    private boolean isAnimated;
    // endregion

    // region === CONSTRUCTORS ===
    public Picture(String source,
                   int fileSize,
                   int width,
                   int height,
                   boolean isAnimated) {

        super(source, fileSize, width, height);
        this.isAnimated = isAnimated;
    }

    protected Picture(UUID uid,
                   String source,
                   int fileSize,
                   int width,
                   int height,
                   boolean isAnimated) {

        super(uid, source, fileSize, width, height);
        this.isAnimated = isAnimated;
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }
    // endregion
}
