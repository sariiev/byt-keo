package com.group3.keo.Users;


import com.group3.keo.Community.Role;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PersonalUser extends User {

    public PersonalUser(String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location) {
        super(username, name, bio, address, location);
    }

}
