package com.group3.keo.enumsTests;

import com.group3.keo.enums.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTypeTest {
    @Test
    public void testNumberOfUserTypes() {
        Assertions.assertEquals(2, UserType.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(UserType.valueOf("PERSONAL"));
        Assertions.assertNotNull(UserType.valueOf("BUSINESS"));
    }
}
