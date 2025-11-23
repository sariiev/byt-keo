package com.group3.keo.Users;
import com.group3.keo.utils.Utils;

import java.util.*;

public abstract class User {

    private static final int MAX_BIO_LENGTH = 300;

    private String username;
    private String name;
    private String bio;

    private Address address;
    private Location location;

    private final Set<User> followers = new HashSet<>();

    protected User(String username, String name, String bio, Address address, Location location){
        setUsername(username);
        setName(name);
        setBio(bio);          // optional, but validated if not null
        setAddress(address);
        setLocation(location);
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        Utils.validateNonEmpty(username, "username");
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Utils.validateNonEmpty(name, "name");
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        if (bio == null) {
            this.bio = null;
            return;
        }

        String trimmed = bio.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("bio cannot be empty if provided");
        }
        if (trimmed.length() > MAX_BIO_LENGTH) {
            throw new IllegalArgumentException(
                    "bio length must be <= " + MAX_BIO_LENGTH);
        }
        this.bio = trimmed;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("address cannot be null");
        }
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        }
        this.location = location;
    }


    public Set<User> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }

    public void addFollower(User follower) {
        if (follower == null) {
            throw new IllegalArgumentException("follower cannot be null");
        }
        if (follower == this) {
            throw new IllegalArgumentException("user cannot follow themselves");
        }
        followers.add(follower);
    }

    public void removeFollower(User follower) {
        followers.remove(follower);
    }

    public int getFollowersCount() {
        return followers.size();
    }

    public static class Address {

        private String country;
        private String city;
        private String street;

        public Address(String country, String city, String street) {
            setCountry(country);
            setCity(city);
            setStreet(street);
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            Utils.validateNonEmpty(country, "country");
            this.country = country.trim();
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            Utils.validateNonEmpty(city, "city");
            this.city = city.trim();
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            Utils.validateNonEmpty(street, "street");
            this.street = street.trim();
        }

    }

    public static class Location {

        private double latitude;
        private double longitude;

        public Location(double latitude, double longitude) {
            setLatitude(latitude);
            setLongitude(longitude);
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            if (latitude < -90.0 || latitude > 90.0) {
                throw new IllegalArgumentException(
                        "latitude must be between -90 and 90");
            }
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            if (longitude < -180.0 || longitude > 180.0) {
                throw new IllegalArgumentException(
                        "longitude must be between -180 and 180");
            }
            this.longitude = longitude;
        }
    }
}

