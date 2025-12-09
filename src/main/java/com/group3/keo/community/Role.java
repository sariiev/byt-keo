package com.group3.keo.community;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.enums.RoleType;
import com.group3.keo.users.PersonalUser;
import com.group3.keo.users.User;

public class Role {
    // region === EXTENT ===
    private static final Set<Role> extent = new HashSet<>();
    // endregion

    // region === FIELDS ===
    private RoleType roleType;
    private Community community;
    private PersonalUser user;
    // endregion

    // region === CONSTRUCTORS ===
    public Role(RoleType roleType, Community community, PersonalUser user) {
        // this constructor is not used directly
        // roles are only instantiated by PersonalUser.joinCommunity() or Community.addMember()
        for (Role r : extent) {
            if (r.getCommunity().equals(community) && r.getUser().equals(user)) {
                throw new IllegalStateException("Role for this (Community, User) pair already exists");
            }
        }

        setRoleType(roleType);

        if (community == null) {
            throw new IllegalArgumentException("community cannot be null");
        }
        this.community = community;

        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        this.user = user;

        extent.add(this);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        if (roleType == null) {
            throw new IllegalArgumentException("roleType cannot be null");
        }
        this.roleType = roleType;
    }

    // region === MUTATORS ===
    public void delete() {
        user.internalRemoveRole(this);

        community.internalRemoveRole(this);

        extent.remove(this);
    }
    // endregion

    public Community getCommunity() {
        return community;
    }

    public PersonalUser getUser() {
        return user;
    }
    // endregion

    // region === EQUALS & HASHCODE ===

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;
        return Objects.equals(community, role.community) && Objects.equals(user, role.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(community, user);
    }
    // endregion

    // region === EXTENT ACCESS ===
    public static Set<Role> getExtent() {
        return Collections.unmodifiableSet(extent);
    }
    // endregion
    
    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<RoleDTO> dtos = new ArrayList<>();
            for (Role role : extent) {
                dtos.add(role.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<RoleDTO>>(){}.getType();
            List<RoleDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (RoleDTO dto : loaded) {
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
    private RoleDTO toDto() {
        RoleDTO dto = new RoleDTO();
        dto.communityUid = community.getUid();
        dto.userUid = user.getUid();
        dto.roleType = roleType;

        return dto;
    }

    private static Role fromDto(RoleDTO dto) {
        Community community = Community.getExtent().get(dto.communityUid);
        if (community == null) {
            throw new IllegalStateException("community not found for UID: " + dto.communityUid);
        }
        PersonalUser user = (PersonalUser) User.getExtent().get(dto.userUid);
        if (user == null) {
            throw new IllegalStateException("user not found for UID: " + dto.userUid);
        }

        return new Role(dto.roleType, community, user);
    }
    // endregion
}
