package com.group3.keo.media;

import java.util.UUID;

public abstract class VisualAttachment extends MediaAttachment {
    // region === CONSTANTS ===
    public static final int MAX_WIDTH = 3840;
    public static final int MAX_HEIGHT = 2160;
    // endregion

    // region === FIELDS ===
    private int width;
    private int height;
    // endregion

    // region === CONSTRUCTORS ===
    protected VisualAttachment(String source,
                               int fileSize,
                               int width,
                               int height) {
        super(source, fileSize);
        setWidth(width);
        setHeight(height);
    }

    protected VisualAttachment(UUID uid,
                               String source,
                               int fileSize,
                               int width,
                               int height) {
        super(uid, source, fileSize);
        setWidth(width);
        setHeight(height);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width <= 0 || width > MAX_WIDTH) {
            throw new IllegalArgumentException(
                    "width must be between 1 and " + MAX_WIDTH
            );
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height <= 0 || height > MAX_HEIGHT) {
            throw new IllegalArgumentException(
                    "height must be between 1 and " + MAX_HEIGHT
            );
        }
        this.height = height;
    }
    // endregion
}
