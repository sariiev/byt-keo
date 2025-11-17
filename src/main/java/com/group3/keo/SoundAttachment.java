package com.group3.keo;

import java.util.Set;

public class SoundAttachment extends MediaAttachment{

    public static final int MaxDuration = 600;  // seconds

    private int duration;
    private int channels;  // must be 1 or 2

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration < 0 || duration > MaxDuration) {
            throw new IllegalArgumentException(
                    "Duration must be between 0 and " + MaxDuration + " seconds"
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

}
