package com.group3.keo.enumsTests;

import com.group3.keo.enums.PublicationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PublicationTypeTest {
    @Test
    public void testNumberOfPublicationTypes() {
        Assertions.assertEquals(3, PublicationType.values().length);
    }

    @Test
    public void testEnumValues() {
        Assertions.assertNotNull(PublicationType.valueOf("POST"));
        Assertions.assertNotNull(PublicationType.valueOf("QUOTE"));
        Assertions.assertNotNull(PublicationType.valueOf("COMMENT"));
    }
}
