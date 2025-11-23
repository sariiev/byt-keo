package com.group3.keo.MediaAttachments;

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

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }
}
