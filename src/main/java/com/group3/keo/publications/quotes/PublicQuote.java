package com.group3.keo.publications.quotes;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicPublication;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.base.PublicationBase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PublicQuote extends Quote implements PublicPublication {
    public PublicQuote(PublicationAuthor author, String caption, PublicationBase referencedPublication, List<MediaAttachment> attachments) {
        super(author, caption, attachments, referencedPublication);
    }

    public PublicQuote(UUID uid, PublicationAuthor author, String caption, List<MediaAttachment> attachments, PublicationBase referencedPublication, LocalDateTime publicationDateTime,  int views, boolean wasEdited, boolean wasPromoted) {
        super(uid, author, caption, attachments, referencedPublication, publicationDateTime, views, wasEdited, wasPromoted);
    }
}
