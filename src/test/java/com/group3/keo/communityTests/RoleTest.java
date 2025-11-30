package com.group3.keo.communityTests;

import com.group3.keo.community.Community;
import com.group3.keo.community.Role;
import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.enums.RoleType;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleTest {
    private Community community;
    private PersonalUser user;
    private Picture avatar;
    private final User.Address address = new User.Address("Poland", "Warsaw", "Koszykowa");
    private final User.Location location = new User.Location(14.13, 21.1);

    public RoleTest() {
    }

    private void clearRoleExtent() {
        try {
            Field field = Role.class.getDeclaredField("extent");
            field.setAccessible(true);
            Set<?> extent = (Set)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private void clearCommunityExtent() {
        try {
            Field field = Community.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private void clearUserExtent() {
        try {
            Field field = User.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private void clearMediaAttachmentExtent() {
        try {
            Field field = MediaAttachment.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<?, ?> extent = (Map)field.get((Object)null);
            extent.clear();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private void addToUserExtent(User u) {
        try {
            Field field = User.class.getDeclaredField("extent");
            field.setAccessible(true);
            Map<Object, Object> extent = (Map)field.get((Object)null);
            extent.put(u.getUid(), u);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    private void addToMediaExtent(Picture p) {
        try {
            Field f = MediaAttachment.class.getDeclaredField("extent");
            f.setAccessible(true);
            Map<Object, Object> extent = (Map)f.get((Object)null);
            extent.put(p.getUid(), p);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    @BeforeEach
    void setup() {
        this.clearRoleExtent();
        this.clearCommunityExtent();
        this.clearUserExtent();
        this.clearMediaAttachmentExtent();
        this.avatar = new Picture("src", 100, 800, 600, false);
        this.addToMediaExtent(this.avatar);
        this.community = new Community("Tech", CommunityTopic.TECHNOLOGY, this.avatar);
        this.user = new PersonalUser("user13", "Michael Jackson", "bio", this.address, this.location);
        this.addToUserExtent(this.user);
    }

    @Test
    public void testRoleInitialization() {
        Role r = new Role(RoleType.MEMBER, this.community, this.user);
        Assertions.assertEquals(RoleType.MEMBER, r.getRoleType());
        Assertions.assertEquals(this.community, r.getCommunity());
        Assertions.assertEquals(this.user, r.getUser());
        Assertions.assertEquals(1, Role.getExtent().size());
    }

    @Test
    public void testNullRoleTypeException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Role((RoleType)null, this.community, this.user);
        });
    }

    @Test
    public void testNullCommunityException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Role(RoleType.HEAD, (Community)null, this.user);
        });
    }

    @Test
    public void testNullUserException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Role(RoleType.HEAD, this.community, (PersonalUser)null);
        });
    }

    @Test
    public void testDuplicateRoleException() {
        new Role(RoleType.HEAD, this.community, this.user);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            new Role(RoleType.MEMBER, this.community, this.user);
        });
    }

    @Test
    public void testExtentStoresRole() {
        Role r = new Role(RoleType.HEAD, this.community, this.user);
        Assertions.assertEquals(1, Role.getExtent().size());
        Assertions.assertTrue(Role.getExtent().contains(r));
    }

    @Test
    public void testEncapsulation() {
        Role r = new Role(RoleType.HEAD, this.community, this.user);
        RoleType external = RoleType.HEAD;
        external = RoleType.MEMBER;
        Assertions.assertNotEquals(external, r.getRoleType(), "Changing local variable must not affect Role object");
    }

    @Test
    public void testExtentPersistence() throws Exception {
        new Role(RoleType.HEAD, this.community, this.user);
        File file = Files.createTempFile("role_test", ".json").toFile();
        Role.saveExtent(file.getAbsolutePath());
        this.clearRoleExtent();
        Assertions.assertEquals(0, Role.getExtent().size());
        Role.loadExtent(file.getAbsolutePath());
        Assertions.assertEquals(1, Role.getExtent().size());
        Role loaded = (Role)Role.getExtent().iterator().next();
        Assertions.assertEquals(RoleType.HEAD, loaded.getRoleType());
        Assertions.assertEquals(this.community.getUid(), loaded.getCommunity().getUid());
        Assertions.assertEquals(this.user.getUid(), loaded.getUser().getUid());
    }
}
