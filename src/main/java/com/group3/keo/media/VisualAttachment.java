package com.group3.keo.MediaAttachments;

import java.util.UUID;

public abstract class VisualAttachment extends MediaAttachment {

    public static final int MaxWidth = 3840;
    public static final int MaxHeight = 2160;

    private int width;
    private int height;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width <= 0 || width > MaxWidth) {
            throw new IllegalArgumentException(
                    "width must be between 1 and " + MaxWidth
            );
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height <= 0 || height > MaxHeight) {
            throw new IllegalArgumentException(
                    "height must be between 1 and " + MaxHeight
            );
        }
        this.height = height;
    }
}
