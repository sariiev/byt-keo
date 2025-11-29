package com.group3.keo.publications.posts;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicPublication;
import com.group3.keo.publications.base.PublicationAuthor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PublicPost extends Post implements PublicPublication {
    public PublicPost(PublicationAuthor author, String caption, List<MediaAttachment> attachments) {
        super(author, caption, attachments);
    }

    public PublicPost(UUID uid, PublicationAuthor author, String caption, List<MediaAttachment> attachments, LocalDateTime publicationDateTime, int views, boolean wasEdited, boolean wasPromoted) {
        super(uid, author, caption, attachments, publicationDateTime, views, wasEdited, wasPromoted);
    }
}
