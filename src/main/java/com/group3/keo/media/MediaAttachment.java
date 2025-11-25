package com.group3.keo.MediaAttachments;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group3.keo.Enums.MediaFormat;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public abstract class MediaAttachment {
    private static final Map<UUID, MediaAttachment> extent = new HashMap<>();

    private final UUID uid;

    public static final int MaxFileSize = 500;

    private String source;
    private int fileSize;

    private static final Set<MediaFormat> ALLOWED_FORMATS =
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        this.source = source;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        if (fileSize < 0 || fileSize > MaxFileSize) {
            throw new IllegalArgumentException(
                    "FileSize must be between 0 and " + MaxFileSize + " MB"
            );
        }
        this.fileSize = fileSize;
    }

    public static Map<UUID, MediaAttachment> getExtent() {
        return Collections.unmodifiableMap(extent);
    }

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
                dto.hasAudio = v.isHasAudio();
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
}