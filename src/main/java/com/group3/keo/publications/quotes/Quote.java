package com.group3.keo.publications.quotes;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.publications.posts.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Quote extends Post {
    // region === FIELDS ===
    private final PublicationBase referencedPublication;
    // endregion

    // region === CONSTRUCTORS ===
    public Quote(PublicationAuthor author,
                 String caption,
                 List<MediaAttachment> attachments,
                 PublicationBase referencedPublication) {
        super(author, caption, attachments);
        if (referencedPublication == null) {
            throw new IllegalArgumentException("Referenced publication cannot be null");
        }
        this.referencedPublication = referencedPublication;
    }

    public Quote(UUID uid,
                 PublicationAuthor author,
                 String caption,
                 List<MediaAttachment> attachments,
                 PublicationBase referencedPublication,
                 LocalDateTime publicationDateTime,
                 int views,
                 boolean wasEdited,
                 boolean wasPromoted) {
        super(uid, author, caption, attachments, publicationDateTime, views, wasEdited, wasPromoted);
        if (referencedPublication == null) {
            throw new IllegalArgumentException("Referenced publication cannot be null");
        }
        this.referencedPublication = referencedPublication;
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public PublicationBase getReferencedPublication() {
        return referencedPublication;
    }
    // endregion
}
