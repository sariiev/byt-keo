package com.group3.keo.Users;

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
