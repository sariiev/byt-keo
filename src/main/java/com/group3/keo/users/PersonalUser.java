package com.group3.keo.users;

import java.util.UUID;

public class PersonalUser extends User {

    public PersonalUser(String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location) {
        super(username, name, bio, address, location);
    }

    protected PersonalUser(UUID uid,
                        String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location) {
        super(uid, username, name, bio, address, location);
    }

}
