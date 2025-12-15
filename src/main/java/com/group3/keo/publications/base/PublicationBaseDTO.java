package com.group3.keo.publications.base;

import com.group3.keo.enums.PublicationAuthorType;
import com.group3.keo.enums.PublicationType;

import java.util.List;
import java.util.UUID;

public class PublicationBaseDTO {
    public PublicationType type;
    public PublicationAuthorType authorType;
    public UUID author;
    public UUID uid;
    public String caption;
    public List<UUID> attachments;
    public String publicationDateTime;
    public int views;
    public boolean wasEdited;
    public List<UUID> likedBy;
    public List<UUID> comments;

    public boolean wasPromoted; // for Post

    public UUID referencedPublication; // for Quote

    public UUID commentedPublication; // for Comment

    public boolean isPrivate;
    public List<UUID> allowedUsers;

    public UUID publishedByCommunity;
}
