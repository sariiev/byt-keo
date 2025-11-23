package com.group3.keo.MediaAttachments;

import java.util.UUID;

public class MediaAttachmentDTO {
    public UUID uid;
    public String type;

    public String source;
    public int fileSize;

    Integer width;
    Integer height;

    Integer duration;
    Integer channels;

    Boolean hasAudio;

    Boolean isAnimated;
}
