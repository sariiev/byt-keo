package com.group3.keo.publications;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicationBase;

import java.time.LocalDateTime;
import java.util.List;

public class Comment extends PublicationBase {

    private final PublicationBase commentedPublication;

    public Comment(String caption,
                   LocalDateTime publicationDateTime,
                   PublicationBase commentedPublication,
                   List<MediaAttachment> attachments) {
        super(caption, publicationDateTime, attachments);
        if (commentedPublication == null) {
            throw new IllegalArgumentException("publication cannot be null");
        }
        this.commentedPublication = commentedPublication;
    }

    public PublicationBase getCommentedPublication() {
        return commentedPublication;
    }

}
