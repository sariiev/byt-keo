package com.group3.keo.MediaAttachments;

import java.util.UUID;

public class Picture extends VisualAttachment {

    private boolean isAnimated;

    public Picture(String source,
                   int fileSize,
                   int width,
                   int height,
                   boolean isAnimated) {

        super(source, fileSize, width, height);
        this.isAnimated = isAnimated;
    }

    public Picture(UUID uid,
                   String source,
                   int fileSize,
                   int width,
                   int height,
                   boolean isAnimated) {

        super(uid, source, fileSize, width, height);
        this.isAnimated = isAnimated;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }
}
