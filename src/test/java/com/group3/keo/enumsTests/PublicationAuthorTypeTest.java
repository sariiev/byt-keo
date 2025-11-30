package com.group3.keo.enumsTests;

import com.group3.keo.enums.PublicationAuthorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PublicationAuthorTypeTest {
    @Test
    public void testNumberOfPublicationAuthorTypes() {
        Assertions.assertEquals(2, PublicationAuthorType.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(PublicationAuthorType.valueOf("USER"));
        Assertions.assertNotNull(PublicationAuthorType.valueOf("COMMUNITY"));
    }
}
