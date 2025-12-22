package com.group3.keo.publications.quotes;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.publications.base.visibility.PublicationVisibility;
import com.group3.keo.publications.posts.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Quote extends Post {
    // region === FIELDS ===
    private PublicationBase referencedPublication;
    // endregion

    // region === CONSTRUCTORS ===
    public Quote(PublicationAuthor author,
                 String caption,
                 List<MediaAttachment> attachments,
                 PublicationBase referencedPublication, PublicationVisibility visibility) {
        super(author, caption, attachments, visibility);
        if (referencedPublication != null) {
            quotePublication(referencedPublication);
        }
    }

    public Quote(UUID uid,
                 PublicationAuthor author,
                 String caption,
                 List<MediaAttachment> attachments,
                 PublicationBase referencedPublication,
                 LocalDateTime publicationDateTime,
                 int views,
                 boolean wasEdited,
                 boolean wasPromoted,
                 PublicationVisibility visibility) {
        super(uid, author, caption, attachments, publicationDateTime, views, wasEdited, wasPromoted, visibility);

        if (referencedPublication != null) {
            quotePublication(referencedPublication);
        }
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public PublicationBase getReferencedPublication() {
        return referencedPublication;
    }

    public void setReferencedPublication(PublicationBase newReferencedPublication) {
        quotePublication(newReferencedPublication);
    }

    void setReferencedPublicationInternal(PublicationBase publication) {
        this.referencedPublication = publication;
    }
    // endregion

    // region === MUTATORS ===
    public void quotePublication(PublicationBase publication) {
        if (publication == this.referencedPublication) {
            return;
        }

        if (publication == this) {
            throw new IllegalArgumentException("A Quote cannot reference itself");
        }

        detachFromReferencedPublication();

        this.referencedPublication = publication;

        if (publication != null) {
            publication.addQuoteInternal(this);
        }
    }

    public void detachFromReferencedPublication() {
        if (referencedPublication == null) {
            return;
        }

        PublicationBase referencedPublication = this.referencedPublication;
        this.referencedPublication = null;

        referencedPublication.removeQuoteInternal(this);
    }

    public void clearReferencedPublicationInternal() {
        this.referencedPublication = null;
    }

    @Override
    public void delete() {
        detachFromReferencedPublication();
        super.delete();
    }

    // endregion
}
