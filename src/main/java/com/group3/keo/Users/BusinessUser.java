package com.group3.keo.Users;

public class BusinessUser extends User {

    private String websiteLink;
    private String email;
    private String phoneNumber;

    public BusinessUser(String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location,
                        String websiteLink,
                        String email,
                        String phoneNumber) {

        super(username, name, bio, address, location);
        setWebsiteLink(websiteLink);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        if (websiteLink == null || websiteLink.trim().isEmpty()) {
            throw new IllegalArgumentException("websiteLink cannot be null or empty");
        }
        this.websiteLink = websiteLink.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }
        this.email = email.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("phoneNumber cannot be null or empty");
        }
        this.phoneNumber = phoneNumber.trim();
    }
}
