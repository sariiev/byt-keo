package com.group3.keo.Enums;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTypeTest {
    @Test
    public void testNumberOfRoleTypes(){
        assertEquals(3, RoleType.values().length);
    }
    @Test
    public void testEnumValues(){
        assertNotNull(RoleType.valueOf("HEAD"));
        assertNotNull(RoleType.valueOf("EDITOR"));
        assertNotNull(RoleType.valueOf("MEMBER"));
    }
}
