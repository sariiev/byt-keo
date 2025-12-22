package com.group3.keo.associationsTests;

import com.group3.keo.community.Community;
import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.publications.posts.PublicPost;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class PublicPostAssociationTest {
    private PersonalUser author;
    private Community community;
    private PublicPost post;

    @BeforeEach
    void setup() {
        author = new PersonalUser("user1", "User1", "Bio1",
                new User.Address("Poland", "Warsaw", "Koszykowa"),
                new User.Location(14.13, 21.1));

        community = new Community("Community", CommunityTopic.ART, null, author);
        post = new PublicPost(author, "caption", new ArrayList<>());
    }

    @Test
    void settingCommunityCreatesBidirectionalLink() {
        post.setPublishedByCommunity(community);
        Assertions.assertEquals(community, post.getPublishedByCommunity());
        Assertions.assertTrue(community.getPublications().contains(post));
    }

    @Test
    void changingCommunityRemovesOldAssociation() {
        Community other = new Community("Other", CommunityTopic.ANIMALS, null, author);

        post.setPublishedByCommunity(community);
        post.setPublishedByCommunity(other);

        Assertions.assertFalse(community.getPublications().contains(post));
        Assertions.assertTrue(other.getPublications().contains(post));
    }

    @Test
    void deletingPostRemovesItFromCommunity() {
        post.setPublishedByCommunity(community);
        post.delete();
        Assertions.assertFalse(community.getPublications().contains(post));
    }
}
