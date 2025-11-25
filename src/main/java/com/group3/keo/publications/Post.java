package com.group3.keo.Publications;

import java.time.LocalDateTime;

public class Post extends PublicationBase{

    private boolean wasPromoted;

    public Post(String caption, LocalDateTime publicationDateTime) {
        super(caption, publicationDateTime);
    }

    public boolean isWasPromoted() {
        return wasPromoted;
    }

    public void setWasPromoted(boolean wasPromoted) {
        this.wasPromoted = wasPromoted;
    }

}
