package com.group3.keo.Publications;

import java.time.LocalDateTime;

public class Comment extends PublicationBase{

    private PublicationBase publication;

    public Comment(String caption,
                   LocalDateTime publicationDateTime,
                   PublicationBase publication) {
        super(caption, publicationDateTime);
        setPublication(publication);
    }

    public PublicationBase getPublication() {
        return publication;
    }

    public void setPublication(PublicationBase publication) {
        if (publication == null) {
            throw new IllegalArgumentException("publication cannot be null");
        }
        this.publication = publication;
    }

}
