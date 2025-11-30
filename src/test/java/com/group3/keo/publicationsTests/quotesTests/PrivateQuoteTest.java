package com.group3.keo.publicationsTests.quotesTests;

import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.publications.posts.PrivatePost;
import com.group3.keo.publications.quotes.PrivateQuote;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrivateQuoteTest {
    private PersonalUser author;
    private PersonalUser user1;
    private PersonalUser user2;
    private PublicationBase referencedPublication;
    private List<MediaAttachment> attachments;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    @BeforeEach
    void setUp() {
        this.user1 = new PersonalUser("john", "John", "bio1", this.address, this.location);
        this.user2 = new PersonalUser("kate", "Kate", "bio2", this.address, this.location);
        this.author = new PersonalUser("mike", "Mike", "bio3", this.address, this.location);
        this.attachments = List.of(new Picture("file.png", 100, 200, 200, false));
        this.referencedPublication = new PrivatePost(this.author, "Original Post", List.of(new Picture("image.png", 100, 200, 200, false)), Set.of(this.user1, this.user2));
    }

    @Test
    public void testAuthorAlwaysAllowed() {
        PrivateQuote quote = new PrivateQuote(this.author, "Caption", this.referencedPublication, this.attachments, Set.of(this.user1));
        Assertions.assertTrue(quote.getAllowedUsers().contains(this.author));
    }

    @Test
    public void testAddAndRemoveAllowedUser() {
        PrivateQuote quote = new PrivateQuote(this.author, "Caption", this.referencedPublication, this.attachments, Set.of(this.user1));
        quote.addAllowedUser(this.user2);
        Assertions.assertTrue(quote.getAllowedUsers().contains(this.user2));
        quote.removeAllowedUser(this.user1);
        Assertions.assertFalse(quote.getAllowedUsers().contains(this.user1));
    }

    @Test
    public void testCannotRemoveAuthor() {
        PrivateQuote quote = new PrivateQuote(this.author, "Caption", this.referencedPublication, this.attachments, (Set)null);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            quote.removeAllowedUser(this.author);
        });
    }

    @Test
    public void testCanView() {
        PrivateQuote quote = new PrivateQuote(this.author, "Caption", this.referencedPublication, this.attachments, Set.of(this.user1));
        Assertions.assertTrue(quote.canView(this.author));
        Assertions.assertTrue(quote.canView(this.user1));
        Assertions.assertFalse(quote.canView(this.user2));
    }

    @Test
    public void testAllowedUsersImmutable() {
        PrivateQuote quote = new PrivateQuote(this.author, "Caption", this.referencedPublication, this.attachments, Set.of(this.user1));
        Set<User> allowedUsers = quote.getAllowedUsers();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            allowedUsers.add(this.user2);
        });
    }

    @Test
    public void testReferencedPublication() {
        PrivateQuote quote = new PrivateQuote(this.author, "Caption", this.referencedPublication, this.attachments, (Set)null);
        Assertions.assertEquals(this.referencedPublication, quote.getReferencedPublication());
    }

    @Test
    public void testWasPromoted() {
        PrivateQuote quote = new PrivateQuote(this.author, "Caption", this.referencedPublication, this.attachments, (Set)null);
        Assertions.assertFalse(quote.wasPromoted());
        quote.setWasPromoted(true);
        Assertions.assertTrue(quote.wasPromoted());
    }
}
