package com.group3.keo.publicationsTests.quotesTests;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.publications.quotes.PublicQuote;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PublicQuoteTest {
    private PersonalUser author;
    private PublicationBase referencedPublication;
    private List<MediaAttachment> attachments;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    void setUp() {
        this.author = new PersonalUser("user13", "Michael Jackson", "bio", this.address, this.location);
        this.referencedPublication = new PublicPost(this.author, "Original Post", List.of(new Picture("image.png", 100, 200, 200, false)));
        this.attachments = List.of(new Picture("file.png", 100, 200, 200, false));
    }

    @Test
    public void testNullReferenceException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new PublicQuote(this.author, "Caption", (PublicationBase)null, this.attachments);
        });
    }

    @Test
    public void testReferencedPublication() {
        PublicQuote quote = new PublicQuote(this.author, "Caption", this.referencedPublication, this.attachments);
        Assertions.assertEquals(this.referencedPublication, quote.getReferencedPublication());
    }

    @Test
    public void testWasPromoted() {
        PublicQuote quote = new PublicQuote(this.author, "Caption", this.referencedPublication, this.attachments);
        Assertions.assertFalse(quote.wasPromoted());
        quote.setWasPromoted(true);
        Assertions.assertTrue(quote.wasPromoted());
        Assertions.assertThrows(IllegalStateException.class, () -> {
            quote.setWasPromoted(false);
        });
    }

    @Test
    public void testAttachmentsEncapsulation() {
        PublicQuote quote = new PublicQuote(this.author, "Caption", this.referencedPublication, this.attachments);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            quote.getAttachments().add(new Picture("file.png", 100, 200, 200, false));
        });
    }
}
