package com.group3.keo.users;

import java.util.List;
import java.util.UUID;

public class UserDTO {
    public UUID uid;
    public String type;

    public String username;
    public String name;
    public String bio;

    public AddressDTO address;
    public LocationDTO location;

    public String websiteLink;
    public String email;
    public String phoneNumber;

    public List<UUID> followers;

    public static class AddressDTO {
        public String country;
        public String city;
        public String street;
    }

    public static class LocationDTO {
        public double latitude;
        public double longitude;
    }
}
