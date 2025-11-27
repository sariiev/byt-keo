package com.group3.keo.media;

import java.util.UUID;

public class MediaAttachmentDTO {
    public UUID uid;
    public String type;

    public String source;
    public int fileSize;

    public Integer width;
    public Integer height;

    public Integer duration;
    public Integer channels;

    public Boolean hasAudio;

    public Boolean isAnimated;
}
