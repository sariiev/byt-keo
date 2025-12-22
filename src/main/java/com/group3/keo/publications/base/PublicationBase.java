package com.group3.keo.publications.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.community.Community;
import com.group3.keo.enums.PublicationAuthorType;
import com.group3.keo.enums.PublicationType;
import com.group3.keo.media.MediaAttachment;
import com.group3.keo.publications.base.visibility.PrivateVisibility;
import com.group3.keo.publications.base.visibility.PublicVisibility;
import com.group3.keo.publications.base.visibility.PublicationVisibility;
import com.group3.keo.publications.comments.Comment;
import com.group3.keo.publications.posts.Post;
import com.group3.keo.publications.quotes.Quote;
import com.group3.keo.users.User;
import com.group3.keo.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public abstract class PublicationBase {
    // region === CONSTANTS ===
    public static final int MAX_CAPTION_LENGTH = 1000;
    public static final int MAX_ATTACHMENTS_SIZE = 10;
    // endregion

    // region === EXTENT ===
    private static final Map<UUID, PublicationBase> extent = new HashMap<>();
    // endregion

    // region === FIELDS ===
    private UUID uid;
    private PublicationVisibility visibility;
    private PublicationAuthor author;
    private String caption;
    private final Set<MediaAttachment> attachments = new HashSet<>();
    private final LocalDateTime publicationDateTime;
    private int views = 0;
    private boolean wasEdited = false;

    private final Set<User> likedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final Set<Quote> quotes = new HashSet<>();
    // endregion

    // region === CONSTRUCTORS ===
    protected PublicationBase(PublicationAuthor author, String caption, List<MediaAttachment> attachments, PublicationVisibility publicationVisibility) {
        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }

        this.uid = UUID.randomUUID();
        this.author = author;
        this.publicationDateTime = LocalDateTime.now();

        addAttachments(attachments);
        setCaption(caption);

        setVisibility(publicationVisibility);

        extent.put(uid, this);

        author.addPublication(this);
    }

    protected PublicationBase(UUID uid, PublicationAuthor author, String caption, List<MediaAttachment> attachments, LocalDateTime publicationDateTime, int views, boolean wasEdited, PublicationVisibility publicationVisibility) {
        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }

        this.uid = uid;
        this.author = author;
        this.publicationDateTime = publicationDateTime;
        this.views = views;
        this.wasEdited = wasEdited;

        addAttachments(attachments);
        setCaption(caption);

        setVisibility(publicationVisibility);

        extent.put(uid, this);

        if (author instanceof User userAuthor) {
            userAuthor.addPublication(this);
        }
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    public boolean isPrivate() {
        return (visibility instanceof PrivateVisibility);
    }

    public PublicationVisibility getVisibility() {
        return visibility;
    }

    public boolean canView(User user) {
        return visibility.canView(user);
    }

    private void setVisibility(PublicationVisibility newVisibility) {
        if (newVisibility == null) {
            throw new IllegalArgumentException("visibility cannot be null");
        }

        if (this.visibility != null) {
            this.visibility.onEnd(this);
        }

        this.visibility = newVisibility;
        this.visibility.onStart(this);
    }

    public PublicationAuthor getAuthor() {
        return author;
    }

    private boolean hasCaption() {
        return caption != null && !caption.isBlank();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        String oldCaption = this.caption;

        if (caption == null || caption.trim().isEmpty()) {
            if (attachments.isEmpty()) {
                throw new IllegalStateException("Publication must have a non-empty caption or at least one attachment");
            }

            this.caption = null;

            if (oldCaption != null) {
                wasEdited = true;
            }

            return;
        }

        Utils.validateMaxLength(caption, "caption", MAX_CAPTION_LENGTH);

        String trimmed = caption.trim();

        this.caption = trimmed;

        if (!trimmed.equals(oldCaption)) {
            this.wasEdited = true;
        }
    }

    public LocalDateTime getPublicationDateTime() {
        return publicationDateTime;
    }

    public int getLikes() {
        return likedBy.size();
    }

    public Set<User> getLikedBy() {
        return Collections.unmodifiableSet(likedBy);
    }

    public void addLike(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        likedBy.add(user);
    }

    public int getViews() {
        return views;
    }

    public void addView() {
        views++;
    }

    public boolean wasEdited() {
        return wasEdited;
    }

    public void setWasEdited(boolean wasEdited) {
        this.wasEdited = wasEdited;
    }

    public Set<MediaAttachment> getAttachments() {
        return Collections.unmodifiableSet(attachments);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public Set<Quote> getQuotes() {
        return Collections.unmodifiableSet(quotes);
    }
    // endregion

    // region === MUTATORS ===
    public void addAllowedUser(User user) {
        if (!(visibility instanceof PrivateVisibility privateVisibility)) {
            throw new IllegalStateException("Publication is not private");
        }
        privateVisibility.addAllowedUser(user);
    }

    public void removeAllowedUser(User user) {
        if (!(visibility instanceof PrivateVisibility privateVisibility)) {
            throw new IllegalStateException("Publication is not private");
        }
        privateVisibility.removeAllowedUser(user);
    }

    public Set<User> getAllowedUsers() {
        if (!(visibility instanceof PrivateVisibility privateVisibility)) {
            return Set.of();
        }
        return privateVisibility.getAllowedUsers();
    }

    public void switchToPublic() {
        if (visibility instanceof PublicVisibility) {
            return;
        }

        setVisibility(new PublicVisibility());
    }

    public void switchToPrivate(Set<User> allowedUsers) {
        if (author instanceof Community) {
            throw new IllegalStateException("publication published by community cannot be private");
        }

        if (allowedUsers == null) {
            throw new IllegalArgumentException("allowedUsers cannot be null");
        }

        PrivateVisibility privateVisibility = new PrivateVisibility((User) author);

        for (User user : allowedUsers) {
            privateVisibility.addAllowedUser(user);
        }

        setVisibility(privateVisibility);
    }

    private void addAttachments(List<MediaAttachment> attachments) {
        if (attachments == null) return;

        for (MediaAttachment attachment : attachments) {
            addAttachment(attachment);
        }
    }

    public void addAttachment(MediaAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("attachment cannot be null");
        }

        if (attachments.size() >= MAX_ATTACHMENTS_SIZE) {
            throw new IllegalStateException("Publication cannot have more than " + MAX_ATTACHMENTS_SIZE + " attachments");
        }

        attachments.add(attachment);
        attachment.setPublicationInternal(this);
    }

    public void removeAttachment(MediaAttachment attachment) {
        if (attachment == null || !attachments.contains(attachment)) {
            return;
        }

        if (!hasCaption() && attachments.size() == 1) {
            throw new IllegalStateException("Publication must have a non-empty caption or at least one attachment");
        }

        attachments.remove(attachment);
        attachment.clearPublicationInternal();
    }

    void addAttachmentInternal(MediaAttachment attachment) {
        if (attachment != null && attachments.size() < MAX_ATTACHMENTS_SIZE) {
            attachments.add(attachment);
        }
    }


    public void removeAttachmentInternal(MediaAttachment attachment) {
        if (attachment != null) {
            attachments.remove(attachment);
        }
    }

    public void addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        if (comment.getCommentedPublication() != this) {
            throw new IllegalArgumentException("Comment does not belong to this publication");
        }
        if (!comments.contains(comment)) {
            comments.add(comment);
        }
    }

    public void removeComment(Comment comment) {
        if (comment == null) {
            return;
        }
        if (!comments.contains(comment)) {
            return;
        }

        comment.delete();
    }

    public void internalRemoveComment(Comment comment) {
        comments.remove(comment);
    }

    public void addQuote(Quote quote) {
        if (quote == null) {
            throw new IllegalArgumentException("Quote cannot be null");
        }
        if (quote.getReferencedPublication() != this) {
            throw new IllegalArgumentException("Quote does not quote this publication");
        }
        quotes.add(quote);
    }

    public void addQuoteInternal(Quote quote) {
        if (quote != null) {
            quotes.add(quote);
        }
    }

    public void removeQuoteInternal(Quote quote) {
        if (quote != null) {
            quotes.remove(quote);
        }
    }

    public void addLikeInternal(User user) {
        if (user != null && !likedBy.contains(user)) {
            likedBy.add(user);
        }
    }

    public void removeLikeInternal(User user) {
        if (user != null) {
            likedBy.remove(user);
        }
    }

    public void detachFromAuthor() {
        if (author == null) {
            return;
        }

        author.removePublicationInternal(this);

        author = null;
    }

    public void delete() {
        for (MediaAttachment attachment : new HashSet<>(attachments)) {
            attachment.clearPublicationInternal();
        }
        attachments.clear();

        for (Comment comment : new ArrayList<>(comments)) {
            comment.delete();
        }

        for (Quote quote : new ArrayList<>(quotes)) {
            quote.detachFromReferencedPublication();
        }

        for (Quote quote : new HashSet<>(quotes)) {
            quote.clearReferencedPublicationInternal();
        }
        quotes.clear();

        detachFromAuthor();

        extent.remove(this.uid);
    }
    // endregion

    // region === EQUALS & HASHCODE ===
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PublicationBase that)) return false;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
    // endregion

    // region === EXTENT ACCESS ===
    public static Map<UUID, PublicationBase> getExtent() {
        return Collections.unmodifiableMap(extent);
    }
    // endregion

    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<PublicationBaseDTO> dtos = new ArrayList<>();
            for (PublicationBase publication : extent.values()) {
                dtos.add(publication.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<PublicationBaseDTO>>(){}.getType();
            List<PublicationBaseDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded == null) return;

            List<PublicationBaseDTO> postDtos = new ArrayList<>();
            List<PublicationBaseDTO> quoteDtos = new ArrayList<>();
            List<PublicationBaseDTO> commentDtos = new ArrayList<>();

            for (PublicationBaseDTO dto : loaded) {
                switch (dto.type) {
                    case POST -> postDtos.add(dto);
                    case QUOTE -> quoteDtos.add(dto);
                    case COMMENT -> commentDtos.add(dto);
                    default -> throw new IllegalStateException("Unknown PublicationType: " + dto.type);
                }
            }

            for (PublicationBaseDTO dto : postDtos) {
                fromDto(dto);
            }

            for (PublicationBaseDTO dto : quoteDtos) {
                fromDto(dto);
            }

            commentDtos.sort(
                    Comparator.comparing(dto -> LocalDateTime.parse(dto.publicationDateTime))
            );

            for (PublicationBaseDTO dto : commentDtos) {
                fromDto(dto);
            }

        } catch (Exception ex) {
            extent.clear();
            ex.printStackTrace();
        }
    }
    // endregion

    // region === DTO CONVERSION ===
    private PublicationBaseDTO toDto() {
        PublicationBaseDTO dto = new PublicationBaseDTO();
        dto.uid = uid;

        switch (author) {
            case User u -> dto.authorType = PublicationAuthorType.USER;
            case Community c -> dto.authorType = PublicationAuthorType.COMMUNITY;
            default -> throw new IllegalStateException("Unknown AuthorType: " + author.getClass());
        }
        dto.author = author.getUid();

        dto.caption = caption;

        dto.attachments = new ArrayList<>();
        for (MediaAttachment attachment : attachments) {
            dto.attachments.add(attachment.getUid());
        }

        dto.publicationDateTime = publicationDateTime.toString();
        dto.views = views;
        dto.wasEdited = wasEdited;

        dto.likedBy = new ArrayList<>();
        for (User user : likedBy) {
            dto.likedBy.add(user.getUid());
        }

        dto.comments = new ArrayList<>();
        for (Comment comment : comments) {
            dto.comments.add(comment.getUid());
        }

        return switch (this) {
            case Quote q -> {
                dto.type = PublicationType.QUOTE;

                dto.wasPromoted = q.wasPromoted();
                if (visibility instanceof PrivateVisibility pp) {
                    dto.isPrivate = true;
                    dto.allowedUsers = new ArrayList<>();
                    for (User user : pp.getAllowedUsers()) {
                        dto.allowedUsers.add(user.getUid());
                    }
                } else {
                    dto.isPrivate = false;
                }
                dto.referencedPublication = q.getReferencedPublication() != null ? q.getReferencedPublication().getUid() : null;

                yield dto;
            }

            case Post p -> {
                dto.type = PublicationType.POST;

                dto.wasPromoted = p.wasPromoted();
                if (visibility instanceof PrivateVisibility pp) {
                    dto.isPrivate = true;
                    dto.allowedUsers = new ArrayList<>();
                    for (User user : pp.getAllowedUsers()) {
                        dto.allowedUsers.add(user.getUid());
                    }
                } else {
                    dto.isPrivate = false;
                }

                yield dto;
            }
            case Comment c -> {
                dto.type = PublicationType.COMMENT;
                dto.commentedPublication = c.getCommentedPublication().getUid();

                yield dto;
            }
            default -> {
                throw new IllegalStateException("Unknown PublicationBase type: " + getClass());
            }
        };
    }

    private static PublicationBase fromDto(PublicationBaseDTO dto) {
        PublicationBase publication;

        if (dto.isPrivate && dto.authorType == PublicationAuthorType.COMMUNITY) {
            throw new IllegalStateException("Community cannot be author of a private publication");
        }

        PublicationAuthor author;
        if (dto.authorType == PublicationAuthorType.USER) {
            author = User.getExtent().get(dto.author);
            if (author == null) {
                throw new IllegalStateException("user not found for UID: " + dto.author);
            }
        } else {
            author = Community.getExtent().get(dto.author);
            if (author == null) {
                throw new IllegalStateException("community not found for UID: " + dto.author);
            }
        }

        List<MediaAttachment> attachments = new ArrayList<>();
        if (dto.attachments != null) {
            for (UUID attachmentUid : dto.attachments) {
                MediaAttachment attachment = MediaAttachment.getExtent().get(attachmentUid);
                if (attachment == null) {
                    throw new IllegalStateException("media attachment not found for UID: " + attachmentUid);
                }
                attachments.add(attachment);
            }
        }

        Set<User> allowedUsers = new HashSet<>();
        if (dto.allowedUsers != null) {
            for (UUID userUid : dto.allowedUsers) {
                User user = User.getExtent().get(userUid);
                if (user == null) {
                    throw new IllegalStateException("user not found for UID: " + userUid);
                }
                allowedUsers.add(user);
            }
        }

        switch (dto.type) {
            case PublicationType.POST -> {
                if (dto.isPrivate) {
                    PrivateVisibility publicationVisibility = new PrivateVisibility((User) author);
                    for (User user : allowedUsers) {
                        publicationVisibility.addAllowedUser(user);
                    }
                    publication = new Post(dto.uid, (User) author, dto.caption, attachments, LocalDateTime.parse(dto.publicationDateTime), dto.views, dto.wasEdited, dto.wasPromoted, publicationVisibility);
                } else {
                    publication = new Post(dto.uid, author, dto.caption, attachments, LocalDateTime.parse(dto.publicationDateTime), dto.views, dto.wasEdited, dto.wasPromoted, new PublicVisibility());
                }
            }
            case PublicationType.QUOTE -> {
                PublicationBase referencedPublication = PublicationBase.getExtent().get(dto.referencedPublication);
                if (dto.isPrivate) {
                    PrivateVisibility publicationVisibility = new PrivateVisibility((User) author);
                    for (User user : allowedUsers) {
                        publicationVisibility.addAllowedUser(user);
                    }
                    publication = new Quote(dto.uid, (User) author, dto.caption, attachments, referencedPublication, LocalDateTime.parse(dto.publicationDateTime) , dto.views, dto.wasEdited, dto.wasPromoted, publicationVisibility);
                } else {
                    publication = new Quote(dto.uid, author, dto.caption, attachments, referencedPublication, LocalDateTime.parse(dto.publicationDateTime), dto.views, dto.wasEdited, dto.wasPromoted, new PublicVisibility());
                }
            }
            case PublicationType.COMMENT -> {
                PublicationBase commentedPublication = PublicationBase.getExtent().get(dto.commentedPublication);
                if (commentedPublication == null) {
                    throw new IllegalStateException("publication not found for UID: " + dto.commentedPublication);
                }

                publication = new Comment(dto.uid, author, dto.caption, commentedPublication, attachments, LocalDateTime.parse(dto.publicationDateTime), dto.views, dto.wasEdited);
            }
            default ->
                throw new IllegalStateException("Unknown PublicationType: " + dto.type);
        }

        if (dto.likedBy != null) {
            for (UUID userUid : dto.likedBy) {
                User user = User.getExtent().get(userUid);
                if (user == null) {
                    throw new IllegalStateException("user not found for UID: " + userUid);
                }
                publication.addLike(user);
            }
        }

        return publication;
    }
    // endregion
}
