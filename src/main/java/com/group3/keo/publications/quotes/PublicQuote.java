package com.group3.keo.publications.quotes;

import com.group3.keo.community.Community;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicPublication;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.base.PublicationBase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PublicQuote extends Quote implements PublicPublication {
    private Community publishedByCommunity;

    // region === CONSTRUCTORS ===
    public PublicQuote(PublicationAuthor author, String caption, PublicationBase referencedPublication, List<MediaAttachment> attachments) {
        super(author, caption, attachments, referencedPublication);
        this.publishedByCommunity = null;
    }

    public PublicQuote(PublicationAuthor author, String caption, PublicationBase referencedPublication, List<MediaAttachment> attachments, Community community) {
        super(author, caption, attachments, referencedPublication);
        if (community != null) {
            setPublishedByCommunity(community);
        }
    }

    public PublicQuote(UUID uid, PublicationAuthor author, String caption, List<MediaAttachment> attachments, PublicationBase referencedPublication, LocalDateTime publicationDateTime,  int views, boolean wasEdited, boolean wasPromoted) {
        super(uid, author, caption, attachments, referencedPublication, publicationDateTime, views, wasEdited, wasPromoted);
        this.publishedByCommunity = null;
    }
    // endregion

    // region === MUTATORS ===
    @Override
    public Community getPublishedByCommunity() {
        return publishedByCommunity;
    }

    @Override
    public boolean isPublishedByCommunity() {
        return publishedByCommunity != null;
    }

    @Override
    public void setPublishedByCommunity(Community community) {
        if (community == this.publishedByCommunity) {
            return;
        }

        if (this.publishedByCommunity != null) {
            Community oldCommunity = this.publishedByCommunity;
            this.publishedByCommunity = null;
            oldCommunity.removePublicationInternal(this);
        }

        this.publishedByCommunity = community;

        if (community != null) {
            community.addPublicationInternal(this);
        }
    }

    @Override
    public void setPublishedByCommunityInternal(Community community) {
        this.publishedByCommunity = community;
    }

    @Override
    public void clearPublishedByCommunityInternal() {
        this.publishedByCommunity = null;
    }

    @Override
    public void delete() {
        // Clear community association before deleting
        if (publishedByCommunity != null) {
            publishedByCommunity.removePublicationInternal(this);
            publishedByCommunity = null;
        }
        super.delete();
    }
    //endregion
}
