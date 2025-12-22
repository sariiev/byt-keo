package com.group3.keo.associationsTests.userAssociationTests;

import com.group3.keo.users.PersonalUser;
import com.group3.keo.media.Picture;
import com.group3.keo.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserProfilePictureAssociationTest {
    private User user;
    private Picture picture1;
    private Picture picture2;

    @BeforeEach
    void setup() {
        user = new PersonalUser("user1", "User1", "Bio1",
                new User.Address("C1", "C1", "A1"),
                new User.Location(1, 1)
        );

        picture1 = new Picture("pic1", 100, 200, 200, false);
        picture2 = new Picture("pic2", 120, 300, 300, true);
    }

    @Test
    void settingProfilePictureCreatesBidirectionalAssociation() {
        user.setProfilePicture(picture1);

        Assertions.assertEquals(picture1, user.getProfilePicture());
        Assertions.assertEquals(user, picture1.getProfilePictureOf());
    }

    @Test
    void replacingProfilePictureRemovesOldAssociation() {
        user.setProfilePicture(picture1);
        user.setProfilePicture(picture2);

        Assertions.assertNull(picture1.getProfilePictureOf());
        Assertions.assertEquals(picture2, user.getProfilePicture());
    }

    @Test
    void removingProfilePictureClearsAssociation() {
        user.setProfilePicture(picture1);
        user.removeProfilePicture();

        Assertions.assertNull(user.getProfilePicture());
        Assertions.assertNull(picture1.getProfilePictureOf());
    }
}
