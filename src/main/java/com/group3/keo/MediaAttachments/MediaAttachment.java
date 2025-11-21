package com.group3.keo;
import com.group3.keo.Enums.MediaFormat;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class MediaAttachment {

    public static final int MaxFileSize = 500;

    private String source;
    private int fileSize;

    private static final Set<MediaFormat> ALLOWED_FORMATS =
            Collections.unmodifiableSet(EnumSet.of(
                    MediaFormat.JPG,
                    MediaFormat.JPEG,
                    MediaFormat.PNG,
                    MediaFormat.GIF,
                    MediaFormat.WEBP,
                    MediaFormat.SVG,
                    MediaFormat.MP4,
                    MediaFormat.MOV,
                    MediaFormat.MKV,
                    MediaFormat.WEBM,
                    MediaFormat.MP3,
                    MediaFormat.WAV,
                    MediaFormat.OGG
            ));

    protected MediaAttachment(String source, int fileSize) {
        setSource(source);
        setFileSize(fileSize);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        this.source = source;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        if (fileSize < 0 || fileSize > MaxFileSize) {
            throw new IllegalArgumentException(
                    "FileSize must be between 0 and " + MaxFileSize + " MB"
            );
        }
        this.fileSize = fileSize;
    }

}