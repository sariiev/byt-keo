package com.group3.keo.users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.enums.UserType;
import com.group3.keo.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public abstract class User {
    private static final Map<UUID, User> extent = new HashMap<>();

    private final UUID uid;

    private static final int MAX_BIO_LENGTH = 300;

    private String username;
    private String name;
    private String bio;

    private Address address;
    private Location location;

    private final Set<User> followers = new HashSet<>();

    protected User(String username, String name, String bio, Address address, Location location){
        uid = UUID.randomUUID();
        setUsername(username);
        setName(name);
        setBio(bio);          // optional, but validated if not null
        setAddress(address);
        setLocation(location);
        extent.put(uid, this);
    }

    protected User(UUID uid, String username, String name, String bio, Address address, Location location){
        this.uid = uid;
        setUsername(username);
        setName(name);
        setBio(bio);
        setAddress(address);
        setLocation(location);
        extent.put(uid, this);
    }

    public UUID getUid() {
        return uid;
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

    public static Map<UUID, User> getExtent() {
        return Collections.unmodifiableMap(extent);
    }

    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<UserDTO> dtos = new ArrayList<>();
            for (User user : extent.values()) {
                dtos.add(user.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<UserDTO>>(){}.getType();
            List<UserDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (UserDTO dto : loaded) {
                    fromDto(dto);
                }

                for (UserDTO dto : loaded) {
                    User user = extent.get(dto.uid);
                    if (dto.followers != null) {
                        for (UUID followerUid : dto.followers) {
                            User follower = extent.get(followerUid);
                            if (follower != null) {
                                user.addFollower(follower);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            extent.clear();
            ex.printStackTrace();
        }
    }

    private UserDTO toDto() {
        UserDTO dto = new UserDTO();
        dto.uid = uid;

        dto.type = (this instanceof PersonalUser) ? UserType.PERSONAL : UserType.BUSINESS;
        dto.username = username;
        dto.name = name;
        dto.bio = bio;

        dto.address = new UserDTO.AddressDTO();
        dto.address.country = address.getCountry();
        dto.address.city = address.getCity();
        dto.address.street = address.getStreet();

        dto.location = new UserDTO.LocationDTO();
        dto.location.latitude = location.getLatitude();
        dto.location.longitude = location.getLongitude();

        if (this instanceof BusinessUser businessUser) {
            dto.websiteLink = businessUser.getWebsiteLink();
            dto.email = businessUser.getEmail();
            dto.phoneNumber = businessUser.getPhoneNumber();
        }

        dto.followers = new ArrayList<>();
        for (User follower : followers) {
            dto.followers.add(follower.uid);
        }

        return dto;
    }

    private static User fromDto(UserDTO dto) {
        User user;

        if (dto.type == UserType.PERSONAL) {
            user = new PersonalUser(dto.uid, dto.username, dto.name, dto.bio, new Address(dto.address.country, dto.address.city, dto.address.street), new Location(dto.location.latitude, dto.location.longitude));
        } else {
            user = new BusinessUser(dto.uid, dto.username, dto.name, dto.bio, new Address(dto.address.country, dto.address.city, dto.address.street), new Location(dto.location.latitude, dto.location.longitude), dto.websiteLink, dto.email, dto.phoneNumber);
        }

        return user;
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

