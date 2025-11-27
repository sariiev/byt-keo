package com.group3.keo.publications;

import com.group3.keo.media.MediaAttachment;

import java.time.LocalDateTime;
import java.util.List;

public class Post extends PublicationBase{

    private boolean wasPromoted;

    public Post(String caption, LocalDateTime publicationDateTime, List<MediaAttachment> attachments) {
        super(caption, publicationDateTime, attachments);
    }

    public boolean isWasPromoted() {
        return wasPromoted;
    }

    public void setWasPromoted(boolean wasPromoted) {
        this.wasPromoted = wasPromoted;
    }

}
