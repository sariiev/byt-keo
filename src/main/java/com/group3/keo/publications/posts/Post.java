package com.group3.keo.publications.posts;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.base.PublicationBase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class Post extends PublicationBase {

    protected boolean wasPromoted = false;

    protected Post(PublicationAuthor author, String caption, List<MediaAttachment> attachments) {
        super(author, caption, attachments);
    }

    protected Post(UUID uid, PublicationAuthor author, String caption, List<MediaAttachment> attachments, LocalDateTime publicationDateTime, int views, boolean wasEdited, boolean wasPromoted) {
        super(uid, author, caption, attachments, publicationDateTime, views, wasEdited);
        this.wasPromoted = wasPromoted;
    }

    public boolean wasPromoted() {
        return wasPromoted;
    }

    public void setWasPromoted(boolean wasPromoted) {
        if (this.wasPromoted && !wasPromoted) {
            throw new IllegalStateException("wasPromoted can't be changed from true to false");
        }
        this.wasPromoted = wasPromoted;
    }
}
