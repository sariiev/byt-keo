package com.group3.keo.utils;


public final class Utils {

    private Utils() {
    }

    public static void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    public static void validateMaxLength(String value, String fieldName, int maxLength) {
        if (value == null) {
            return;
        }
        String trimmed = value.trim();

        if (trimmed.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " length cannot exceed " + maxLength + " characters");
        }
    }

    public static void validateEmail(String email) {
        validateNonEmpty(email, "email");

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static void validateWebsiteLink(String url) {
        validateNonEmpty(url, "websiteLink");

        if (!url.matches("^(https?://).+")) {
            throw new IllegalArgumentException("websiteLink must start with http:// or https://");
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        validateNonEmpty(phoneNumber, "phoneNumber");

        if (!phoneNumber.matches("^[0-9+()\\-\\s]+$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
