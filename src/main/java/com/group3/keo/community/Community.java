package com.group3.keo.community;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.enums.CommunityTopic;
import com.group3.keo.enums.RoleType;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.media.Picture;
import com.group3.keo.publications.base.PublicationAuthor;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class Community implements PublicationAuthor {
    // region === CONSTANTS ===
    public static final int MAX_NAME_LENGTH = 16;
    // endregion
    
    // region === EXTENT ===
    private static final Map<UUID, Community> extent = new HashMap<>();
    // endregion

    // region === FIELDS ===
    private final Map<String, Role> members = new HashMap<>();
    private final UUID uid;
    private String name;
    private Picture avatar;
    private CommunityTopic topic;
    // endregion

    // region === CONSTRUCTORS ===
    public Community(String name, CommunityTopic topic, Picture avatar, PersonalUser creator) {
        uid = UUID.randomUUID();
        setName(name);
        setTopic(topic);
        setAvatar(avatar);
        addMember(creator, RoleType.HEAD);
        extent.put(uid, this);
    }

    private Community(UUID uid, String name, CommunityTopic topic, Picture avatar) {
        this.uid = uid;
        setName(name);
        setTopic(topic);
        setAvatar(avatar);
        extent.put(uid, this);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Utils.validateNonEmpty(name, "name");
        Utils.validateMaxLength(name, "name", MAX_NAME_LENGTH);
        this.name = name.trim();
    }

    public CommunityTopic getTopic() {
        return topic;
    }

    public void setTopic(CommunityTopic topic) {
        if (topic == null) {
            throw new IllegalArgumentException("topic cannot be null");
        }
        this.topic = topic;
    }

    public Picture getAvatar() {
        return avatar;
    }

    public void setAvatar(Picture avatar) {
        this.avatar = avatar;
    }

    public Map<String, Role> getMembers() {
        return Collections.unmodifiableMap(members);
    }

    // endregion

    // region === MUTATORS ===
    public Role addMember(PersonalUser user, RoleType type) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }

        if (members.containsKey(user.getUsername())) {
            throw new IllegalStateException("User is already in this community");
        }

        if (type == RoleType.HEAD) {
            for (Role role : members.values()) {
                if (role.getRoleType() == RoleType.HEAD) {
                    role.setRoleType(RoleType.MEMBER);
                    break;
                }
            }
        }

        Role role = new Role(type, this, user);

        internalAddRole(role);
        user.internalAddRole(role);

        return role;
    }

    public void removeMember(PersonalUser user) {
        Role role = members.get(user.getUsername());
        if (role == null) {
            return;
        }

        role.delete();
    }

    public void delete() {
        for (Role role : new HashSet<>(members.values())) {
            role.delete();
        }
        extent.remove(this.uid);
    }
    // endregion

    // region === HELPERS ===
    public void internalAddRole(Role role) {
        members.put(role.getUser().getUsername(), role);
    }

    public void internalRemoveRole(Role role) {
        members.remove(role.getUser().getUsername());
    }

    public void updateUsername(PersonalUser user, String oldUsername) {
        Role role = members.remove(oldUsername);

        if (role != null) {
            members.put(user.getUsername(), role);
        }
    }

    public void reassignHead(PersonalUser removedUser) {
        Role removedRole = members.get(removedUser.getUsername());
        if (removedRole == null) {
            return;
        }
        if (removedRole.getRoleType() != RoleType.HEAD) {
            return;
        }

        for (Role role : members.values()) {
            if (!role.getUser().equals(removedUser)) {
                role.setRoleType(RoleType.HEAD);
                return;
            }
        }
    }
    // endregion
    
    // region === EQUALS & HASHCODE ===
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Community community)) return false;
        return Objects.equals(uid, community.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
    // endregion
    
    // region === EXTENT ACCESS ===
    public static Map<UUID, Community> getExtent() {
        return Collections.unmodifiableMap(extent);
    }    
    // endregion
    
    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<CommunityDTO> dtos = new ArrayList<>();
            for (Community community : extent.values()) {
                dtos.add(community.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<CommunityDTO>>(){}.getType();
            List<CommunityDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (CommunityDTO dto : loaded) {
                    fromDto(dto);
                }
            }
        } catch (Exception ex) {
            extent.clear();
            ex.printStackTrace();
        }
    }
    // endregion
    
    // region === DTO CONVERSION ===
    private CommunityDTO toDto() {
        CommunityDTO dto = new CommunityDTO();
        dto.uid = uid;
        dto.name = name;
        dto.topic = topic;
        dto.avatarUid = avatar != null ? avatar.getUid() : null;

        return dto;
    }

    private static Community fromDto(CommunityDTO dto) {
        Picture avatar = dto.avatarUid != null ? (Picture) MediaAttachment.getExtent().get(dto.avatarUid) : null;

        return new Community(dto.uid, dto.name, dto.topic, avatar);
    }
    // endregion
}
