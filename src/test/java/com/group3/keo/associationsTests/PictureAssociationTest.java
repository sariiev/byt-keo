package com.group3.keo.associationsTests;

import com.group3.keo.community.Community;
import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

class PictureAssociationTest {
    private PersonalUser user1;
    private Community community1;
    private Picture picture1;
    private Picture picture2;

    private static Field getExtentField(Class<?> clazz) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                Field field = current.getDeclaredField("extent");
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(clazz.getName());
    }

    private static void clearClassExtent(Class<?> clazz) {
        try {
            Field field = getExtentField(clazz);
            Object value = field.get(null);

            if (value instanceof java.util.Map<?, ?> map) {
                map.clear();
            } else if (value instanceof java.util.Set<?> set) {
                set.clear();
            } else {
                throw new IllegalStateException(clazz.getName());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @BeforeEach
    void setup() {
        clearClassExtent(User.class);
        clearClassExtent(Community.class);
        clearClassExtent(MediaAttachment.class);

        user1 = new PersonalUser("user1", "User1", "Bio1",
                new User.Address("C1", "C1", "A1"),
                new User.Location(1, 1)
        );

        community1 = new Community("Community1",
                CommunityTopic.ART,
                null,
                user1
        );

        picture1 = new Picture("pic1", 100, 200, 200, false);
        picture2 = new Picture("pic2", 120, 300, 300, true);
    }

    @Test
    void settingProfilePictureCreatesBidirectionalLink() {
        picture1.setAsProfilePictureOf(user1);

        Assertions.assertEquals(user1, picture1.getProfilePictureOf());
        Assertions.assertEquals(picture1, user1.getProfilePicture());
    }

    @Test
    void removingProfilePictureClearsBothSides() {
        picture1.setAsProfilePictureOf(user1);
        picture1.removeAsProfilePicture();

        Assertions.assertNull(picture1.getProfilePictureOf());
        Assertions.assertNull(user1.getProfilePicture());
    }

    @Test
    void settingCommunityPictureCreatesBidirectionalLink() {
        picture1.setAsCommunityPictureOf(community1);

        Assertions.assertEquals(community1, picture1.getCommunityPictureOf());
        Assertions.assertEquals(picture1, community1.getCommunityPicture());
    }

    @Test
    void removingCommunityPictureClearsBothSides() {
        picture1.setAsCommunityPictureOf(community1);
        picture1.removeAsCommunityPicture();

        Assertions.assertNull(picture1.getCommunityPictureOf());
        Assertions.assertNull(community1.getCommunityPicture());
    }

    @Test
    void deletingPictureClearsAllAssociations() {
        picture1.setAsProfilePictureOf(user1);
        picture1.setAsCommunityPictureOf(community1);

        picture1.delete();

        Assertions.assertNull(user1.getProfilePicture());
        Assertions.assertNull(community1.getCommunityPicture());
    }
}
