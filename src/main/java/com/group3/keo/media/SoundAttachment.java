package com.group3.keo.media;

import java.util.UUID;

public class SoundAttachment extends MediaAttachment {
    // region === CONSTANTS ===
    public static final int MAX_DURATION = 600;  // seconds
    // endregion

    // region === FIELDS ===
    private int duration;
    private int channels;
    // endregion

    // region === CONSTRUCTORS ===
    public SoundAttachment(
            String source,
            int fileSize,
            int duration,
            int channels
    ) {
        super(source, fileSize);
        setDuration(duration);
        setChannels(channels);
    }

    protected SoundAttachment(
            UUID uid,
            String source,
            int fileSize,
            int duration,
            int channels
    ) {
        super(uid, source, fileSize);
        setDuration(duration);
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
                    "Duration must be between 0 and " + MAX_DURATION + " seconds"
            );
        }
        this.duration = duration;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        if (channels != 1 && channels != 2) {
            throw new IllegalArgumentException(
                    "Channels must be either 1 (mono) or 2 (stereo)"
            );
        }
        this.channels = channels;
    }
    // endregion
}
