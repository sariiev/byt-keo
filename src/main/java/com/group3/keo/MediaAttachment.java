package com.group3.keo;
import java.util.Set;

public abstract class MediaAttachment {

    public static final int MaxFileSize = 500;

    private String source;
    private int fileSize;
    private Set<MediaFormat> allowedFormats;

    protected MediaAttachment(String source, int fileSize, Set<MediaFormat> allowedFormats) {
        setSource(source);
        setFileSize(fileSize);
        setAllowedFormats(allowedFormats);
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

    public Set<MediaFormat> getAllowedFormats() {
        return allowedFormats;
    }

    public void setAllowedFormats(Set<MediaFormat> allowedFormats) {
        if (allowedFormats == null || allowedFormats.isEmpty()) {
            throw new IllegalArgumentException(
                    "AllowedFormats must contain at least one format"
            );
        }
        this.allowedFormats = allowedFormats;
    }
}