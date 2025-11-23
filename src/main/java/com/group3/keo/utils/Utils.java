package com.group3.keo.utils;


public final class Utils {

    private Utils() {
    }

    /**
     * Validates that a string is not null or empty (after trimming).
     *
     * @param value the value to validate
     * @param fieldName the name of the field for error messages
     */
    public static void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    /**
     * Basic email format validation.
     * Does not check DNS — only the structural pattern.
     */
    public static void validateEmail(String email) {
        validateNonEmpty(email, "email");

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Basic URL validation (accepts http/https only).
     */
    public static void validateWebsiteLink(String url) {
        validateNonEmpty(url, "websiteLink");

        if (!url.matches("^(https?://).+")) {
            throw new IllegalArgumentException("websiteLink must start with http:// or https://");
        }
    }

    /**
     * Very simple phone number validation.
     * Accepts digits, spaces, +, -, parentheses.
     */
    public static void validatePhoneNumber(String phoneNumber) {
        validateNonEmpty(phoneNumber, "phoneNumber");

        if (!phoneNumber.matches("^[0-9+()\\-\\s]+$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
