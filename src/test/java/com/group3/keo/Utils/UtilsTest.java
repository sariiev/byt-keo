package com.group3.keo.Utils;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    public void testNonEmptyException() {
        assertThrows(IllegalArgumentException.class, () -> Utils.validateNonEmpty(null, "field"));
        assertThrows(IllegalArgumentException.class, () -> Utils.validateNonEmpty("", "field"));
        assertThrows(IllegalArgumentException.class, () -> Utils.validateNonEmpty("   ", "field"));
    }

    @Test
    public void testNonEmptyValid() {
        assertDoesNotThrow(() -> Utils.validateNonEmpty("value", "field"));
    }

    @Test
    public void testEmailNullOrEmptyException() {
        assertThrows(IllegalArgumentException.class, () -> Utils.validateEmail(null));
        assertThrows(IllegalArgumentException.class, () -> Utils.validateEmail("   "));
    }

    @Test
    public void testEmailInvalidFormatException() {
        assertThrows(IllegalArgumentException.class, () -> Utils.validateEmail("emailaddress"));
        assertThrows(IllegalArgumentException.class, () -> Utils.validateEmail("wrong@domain,com"));
    }

    @Test
    public void testEmailValid() {
        assertDoesNotThrow(() -> Utils.validateEmail("test@example.com"));
        assertDoesNotThrow(() -> Utils.validateEmail("user.name+tag+sorting@example.co.uk"));
    }

    @Test
    public void testValidateWebsiteLinkNullOrEmptyException() {
        assertThrows(IllegalArgumentException.class, () -> Utils.validateWebsiteLink(null));
        assertThrows(IllegalArgumentException.class, () -> Utils.validateWebsiteLink("  "));
    }

    @Test
    public void testWebsiteLinkInvalidUrlException() {
        assertThrows(IllegalArgumentException.class, () -> Utils.validateWebsiteLink("www.example.com"));
        assertThrows(IllegalArgumentException.class, () -> Utils.validateWebsiteLink("ftp://example.com"));
    }

    @Test
    public void testWebsiteLinkValid() {
        assertDoesNotThrow(() -> Utils.validateWebsiteLink("http://example.com"));
        assertDoesNotThrow(() -> Utils.validateWebsiteLink("https://example.com/page"));
    }

    @Test
    public void testPhoneNumberNullOrEmptyException() {
        assertThrows(IllegalArgumentException.class, () -> Utils.validatePhoneNumber(null));
        assertThrows(IllegalArgumentException.class, () -> Utils.validatePhoneNumber("   "));
    }

    @Test
    public void testPhoneNumberInvalidException() {
        assertThrows(IllegalArgumentException.class, () -> Utils.validatePhoneNumber("abc123"));
        assertThrows(IllegalArgumentException.class, () -> Utils.validatePhoneNumber("123-456-789x"));
    }

    @Test
    public void testPhoneNumberValid() {
        assertDoesNotThrow(() -> Utils.validatePhoneNumber("+48 123 456 789"));
        assertDoesNotThrow(() -> Utils.validatePhoneNumber("123456789"));
        assertDoesNotThrow(() -> Utils.validatePhoneNumber("(123) 456-789"));
    }
}
