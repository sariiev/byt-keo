package com.group3.keo.publications;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.posts.Post;

import java.time.LocalDateTime;
import java.util.List;

public class Quote extends Post {

    private final PublicationBase referencedPublication;

    public Quote(String caption,
                 LocalDateTime publicationDateTime,
                 PublicationBase referencedPublication,
                 List<MediaAttachment> attachments) {
        super(caption, publicationDateTime, attachments);
        if (referencedPublication == null) {
            throw new IllegalArgumentException("Referenced publication cannot be null");
        }
        this.referencedPublication = referencedPublication;
    }

    public PublicationBase getReferencedPublication() {
        return referencedPublication;
    }

}
