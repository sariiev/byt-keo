package com.group3.keo.Users;

import com.group3.keo.utils.Utils;

import java.util.UUID;

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

    public BusinessUser(UUID uid,
                        String username,
                        String name,
                        String bio,
                        User.Address address,
                        User.Location location,
                        String websiteLink,
                        String email,
                        String phoneNumber) {
        super(uid, username, name, bio, address, location);
        setWebsiteLink(websiteLink);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        Utils.validateWebsiteLink(websiteLink);
        this.websiteLink = websiteLink.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Utils.validateEmail(email);
        this.email = email.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        Utils.validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber.trim();
    }
}
