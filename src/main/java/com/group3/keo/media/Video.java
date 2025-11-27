package com.group3.keo.media;

import java.util.UUID;

public class Video extends VisualAttachment {
    // region === CONSTANTS ===
    public static final int MAX_DURATION = 600;
    // endregion

    // region === FIELDS ===
    private int duration; // in seconds
    private boolean hasAudio;
    private Integer channels;
    // endregion

    // region === CONSTRUCTORS ===
    public Video(String source,
                 int fileSize,
                 int width,
                 int height,
                 int duration,
                 boolean hasAudio,
                 Integer channels) {

        super(source, fileSize, width, height);
        setDuration(duration);
        setHasAudio(hasAudio);
        setChannels(channels);
    }

    protected Video(UUID uid,
                 String source,
                 int fileSize,
                 int width,
                 int height,
                 int duration,
                 boolean hasAudio,
                 Integer channels) {

        super(uid, source, fileSize, width, height);
        setDuration(duration);
        setHasAudio(hasAudio);
        setChannels(channels);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration < 0 || duration > MAX_DURATION) {
            throw new IllegalArgumentException(
                    "duration must be between 0 and " + MAX_DURATION + " seconds"
            );
        }
        this.duration = duration;
    }

    public boolean hasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
        if (!hasAudio) {
            this.channels = null;
        }
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        if (!hasAudio) {
            this.channels = null;   // ignore when no audio
            return;
        }
        if (channels == null) {
            throw new IllegalArgumentException("channels cannot be null when hasAudio is true");
        }
        if (channels != 1 && channels != 2) {
            throw new IllegalArgumentException("channels must be either 1 or 2");
        }
        this.channels = channels;
    }
    // endregion
}
