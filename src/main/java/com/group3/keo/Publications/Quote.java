package com.group3.keo.Publications;

import java.time.LocalDateTime;

public class Quote extends Post{

    private final PublicationBase referencedPublication;

    public Quote(String caption,
                 LocalDateTime publicationDateTime,
                 PublicationBase referencedPublication) {
        super(caption, publicationDateTime);
        if (referencedPublication == null) {
            throw new IllegalArgumentException("Referenced publication cannot be null");
        }
        this.referencedPublication = referencedPublication;
    }

    public PublicationBase getReferencedPublication() {
        return referencedPublication;
    }

}
