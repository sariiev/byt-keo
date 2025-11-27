package com.group3.keo.media;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.enums.MediaFormat;
import com.group3.keo.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.rmi.registry.Registry;
import java.util.*;

public abstract class MediaAttachment {
    // region === CONSTANTS ===
    public static final int MAX_FILE_SIZE = 500;
    public static final int MAX_SOURCE_LENGTH = 128;

    public static final Set<MediaFormat> ALLOWED_FORMATS =
            Collections.unmodifiableSet(EnumSet.of(
                    MediaFormat.JPG,
                    MediaFormat.JPEG,
                    MediaFormat.PNG,
                    MediaFormat.GIF,
                    MediaFormat.WEBP,
                    MediaFormat.SVG,
                    MediaFormat.MP4,
                    MediaFormat.MOV,
                    MediaFormat.MKV,
                    MediaFormat.WEBM,
                    MediaFormat.MP3,
                    MediaFormat.WAV,
                    MediaFormat.OGG
            ));
    // endregion

    // region === EXTENT ===
    private static final Map<UUID, MediaAttachment> extent = new HashMap<>();
    // endregion

    // region === FIELDS ===
    private final UUID uid;
    private String source;
    private int fileSize;
    // endregion

    // region === CONSTRUCTORS ===
    protected MediaAttachment(String source, int fileSize) {
        uid = UUID.randomUUID();
        setSource(source);
        setFileSize(fileSize);
        extent.put(uid, this);
    }

    protected MediaAttachment(UUID uid, String source, int fileSize) {
        this.uid = uid;
        setSource(source);
        setFileSize(fileSize);
        extent.put(uid, this);
    }
    // endregion

    // region === GETTERS & SETTERS ===
    public UUID getUid() {
        return uid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        Utils.validateNonEmpty(source, "source");
        Utils.validateMaxLength(source, "source", MAX_SOURCE_LENGTH);
        this.source = source.trim();
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        if (fileSize < 0 || fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "FileSize must be between 0 and " + MAX_FILE_SIZE + " MB"
            );
        }
        this.fileSize = fileSize;
    }
    // endregion

    // region === EXTENT ACCESS===
    public static Map<UUID, MediaAttachment> getExtent() {
        return Collections.unmodifiableMap(extent);
    }
    // endregion

    // region === PERSISTENCE ===
    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<MediaAttachmentDTO> dtos = new ArrayList<>();
            for (MediaAttachment mediaAttachment : extent.values()) {
                dtos.add(mediaAttachment.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<MediaAttachmentDTO>>(){}.getType();
            List<MediaAttachmentDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (MediaAttachmentDTO dto : loaded) {
                    fromDto(dto);
                }
            }
        } catch (Exception ex) {
            extent.clear();
            ex.printStackTrace();
        }
    }
    // endregion

    // region === DTO CONVERSION ===
    private MediaAttachmentDTO toDto() {
        MediaAttachmentDTO dto = new MediaAttachmentDTO();
        dto.uid = uid;
        dto.source = source;
        dto.fileSize = fileSize;

        switch (this) {
            case Picture p -> {
                dto.type = "picture";
                dto.width = p.getWidth();
                dto.height = p.getHeight();
                dto.isAnimated = p.isAnimated();
            }
            case Video v -> {
                dto.type = "video";
                dto.width = v.getWidth();
                dto.height = v.getHeight();
                dto.duration = v.getDuration();
                dto.hasAudio = v.hasAudio();
                dto.channels = v.getChannels();
            }
            case SoundAttachment sa -> {
                dto.type = "sound";
                dto.duration = sa.getDuration();
                dto.channels = sa.getChannels();
            }
            default -> {
                throw new IllegalStateException("Unknown MediaAttachment type: " + getClass());
            }
        }
        return dto;
    }

    private static MediaAttachment fromDto(MediaAttachmentDTO dto) {
        return switch (dto.type) {
            case "picture" ->
                    new Picture(dto.uid, dto.source, dto.fileSize, dto.width, dto.height, dto.isAnimated);
            case "video" ->
                    new Video(dto.uid, dto.source, dto.fileSize, dto.width, dto.height, dto.duration, dto.hasAudio, dto.channels);
            case "sound" ->
                    new SoundAttachment(dto.uid, dto.source, dto.fileSize, dto.duration, dto.channels);

            default ->
                    throw new IllegalStateException("Unknown MediaAttachment type: " + dto.type);

        };
    }
    // endregion
}