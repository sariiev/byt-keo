package com.group3.keo.publications;

import java.time.LocalDateTime;

public class Comment extends PublicationBase{

    private final PublicationBase publication;

    public Comment(String caption,
                   LocalDateTime publicationDateTime,
                   PublicationBase publication) {
        super(caption, publicationDateTime);
        if (publication == null) {
            throw new IllegalArgumentException("publication cannot be null");
        }
        this.publication = publication;
    }

    public PublicationBase getPublication() {
        return publication;
    }

}
