package com.group3.keo.MediaAttachments;

import java.util.UUID;

public class Video extends VisualAttachment {

    public static final int MaxDuration = 600;

    private int duration;       // in seconds
    private boolean hasAudio;
    private Integer channels;   // [0..1], value in {1,2} if present

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

    public Video(UUID uid,
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration < 0 || duration > MaxDuration) {
            throw new IllegalArgumentException(
                    "duration must be between 0 and " + MaxDuration + " seconds"
            );
        }
        this.duration = duration;
    }

    public boolean isHasAudio() {
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
}
