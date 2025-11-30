package com.group3.keo.utilsTests;

import com.group3.keo.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void testValidateNonEmpty() {
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateNonEmpty("Hello", "field");
        });
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateNonEmpty("  Trimmed  ", "field");
        });
    }

    @Test
    public void testValidateNonEmptyWithNullOrEmpty() {
        Exception ex1 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateNonEmpty((String)null, "field");
        });
        Assertions.assertEquals("field cannot be null or empty", ex1.getMessage());
        Exception ex2 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateNonEmpty("", "field");
        });
        Assertions.assertEquals("field cannot be null or empty", ex2.getMessage());
        Exception ex3 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateNonEmpty("   ", "field");
        });
        Assertions.assertEquals("field cannot be null or empty", ex3.getMessage());
    }

    @Test
    public void testValidateMaxLengthInLimit() {
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateMaxLength("Hello", "field", 10);
        });
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateMaxLength("   Trimmed  ", "field", 10);
        });
    }

    @Test
    public void testValidateMaxLengthOutOfLimit() {
        Exception ex = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateMaxLength("HelloWorld!", "field", 5);
        });
        Assertions.assertEquals("field length cannot exceed 5 characters", ex.getMessage());
    }

    @Test
    public void testValidateEmailValid() {
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateEmail("user@example.com");
        });
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateEmail("user.name+tag@domain.co");
        });
    }

    @Test
    public void testValidateEmailInvalid() {
        Exception ex1 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateEmail("invalid-email");
        });
        Assertions.assertEquals("Invalid email format", ex1.getMessage());
        Exception ex2 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateEmail("");
        });
        Assertions.assertEquals("email cannot be null or empty", ex2.getMessage());
        Exception ex3 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateEmail("   ");
        });
        Assertions.assertEquals("email cannot be null or empty", ex3.getMessage());
    }

    @Test
    public void testValidateWebsiteLinkValid() {
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateWebsiteLink("http://example.com");
        });
        Assertions.assertDoesNotThrow(() -> {
            Utils.validateWebsiteLink("https://example.com/page");
        });
    }

    @Test
    public void testValidateWebsiteLinkInvalid() {
        Exception ex1 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateWebsiteLink("ftp://example.com");
        });
        Assertions.assertEquals("websiteLink must start with http:// or https://", ex1.getMessage());
        Exception ex2 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validateWebsiteLink("");
        });
        Assertions.assertEquals("websiteLink cannot be null or empty", ex2.getMessage());
    }

    @Test
    public void testValidatePhoneNumberValid() {
        Assertions.assertDoesNotThrow(() -> {
            Utils.validatePhoneNumber("1234567890");
        });
        Assertions.assertDoesNotThrow(() -> {
            Utils.validatePhoneNumber("+1 (234) 567-8900");
        });
        Assertions.assertDoesNotThrow(() -> {
            Utils.validatePhoneNumber("012 345 6789");
        });
    }

    @Test
    public void testValidatePhoneNumberInvalid() {
        Exception ex1 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validatePhoneNumber("abc123");
        });
        Assertions.assertEquals("Invalid phone number format", ex1.getMessage());
        Exception ex2 = (Exception)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Utils.validatePhoneNumber("");
        });
        Assertions.assertEquals("phoneNumber cannot be null or empty", ex2.getMessage());
    }
}
