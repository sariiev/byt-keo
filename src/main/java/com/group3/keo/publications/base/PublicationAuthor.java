package com.group3.keo.publications.base;

import java.util.UUID;

public interface PublicationAuthor {
    UUID getUid();
    void addPublication(PublicationBase publication);
    void removePublicationInternal(PublicationBase publication);
}
