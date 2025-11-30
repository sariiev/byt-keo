package com.group3.keo.enumsTests;

import com.group3.keo.enums.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTypeTest {
    @Test
    public void testNumberOfRoleTypes() {
        Assertions.assertEquals(3, RoleType.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(RoleType.valueOf("HEAD"));
        Assertions.assertNotNull(RoleType.valueOf("EDITOR"));
        Assertions.assertNotNull(RoleType.valueOf("MEMBER"));
    }
}
