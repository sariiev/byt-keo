package com.group3.keo.publications;

import java.util.List;
import java.util.UUID;

public class PublicationBaseDTO {
    public UUID uid;
    public String caption;
    public List<UUID> attachments;
    public String publicationDateTime;
    public int views;
    public boolean wasEdited;
    public List<UUID> likedBy;
    public List<UUID> comments;

    public boolean wasPromoted; // for Post

    public boolean referencedPublication; // for Quote

    public boolean commentedPublication; // for Comment


}
