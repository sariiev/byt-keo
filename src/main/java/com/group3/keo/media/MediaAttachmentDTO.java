package com.group3.keo.media;

import com.group3.keo.enums.MediaAttachmentType;

import java.util.UUID;

public class MediaAttachmentDTO {
    public UUID uid;
    public MediaAttachmentType type;

    public String source;
    public int fileSize;

    public Integer width;
    public Integer height;

    public Integer duration;
    public Integer channels;

    public Boolean hasAudio;

    public Boolean isAnimated;
}
