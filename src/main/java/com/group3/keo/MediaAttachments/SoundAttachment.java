package com.group3.keo.MediaAttachments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoundAttachment extends MediaAttachment {
    private static List<SoundAttachment> extent = new ArrayList<>();

    public static final int MaxDuration = 600;  // seconds

    private int duration;
    private int channels;  // must be 1 or 2

    public SoundAttachment(
            String source,
            int fileSize,
            int duration,
            int channels
    ) {
        super(source, fileSize);
        setDuration(duration);
        setChannels(channels);
        addToExtent(this);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration < 0 || duration > MaxDuration) {
            throw new IllegalArgumentException(
                    "Duration must be between 0 and " + MaxDuration + " seconds"
            );
        }
        this.duration = duration;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        if (channels != 1 && channels != 2) {
            throw new IllegalArgumentException(
                    "Channels must be either 1 (mono) or 2 (stereo)"
            );
        }
        this.channels = channels;
    }

    public static List<SoundAttachment> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    private static void addToExtent(SoundAttachment soundAttachment) {
        if (soundAttachment == null) throw new IllegalArgumentException("null object cannot be added to the extent");
        extent.add(soundAttachment);
    }

    public static void saveExtent(String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            List<SoundAttachmentDTO> dtos = new ArrayList<>();
            for (SoundAttachment soundAttachment : extent) {
                dtos.add(soundAttachment.toDto());
            }

            gson.toJson(dtos, fileWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadExtent(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<SoundAttachmentDTO>>(){}.getType();
            List<SoundAttachmentDTO> loaded = gson.fromJson(fileReader, listType);

            extent.clear();

            if (loaded != null) {
                for (SoundAttachmentDTO dto : loaded) {
                    SoundAttachment.fromDto(dto);
                }
            }
        } catch (Exception ex) {
            extent.clear();
            ex.printStackTrace();
        }
    }

    private static class SoundAttachmentDTO {
        String source;
        int fileSize;
        int duration;
        int channels;
    }

    private SoundAttachmentDTO toDto() {
        SoundAttachmentDTO dto = new SoundAttachmentDTO();
        dto.source = getSource();
        dto.fileSize = getFileSize();
        dto.duration = getDuration();
        dto.channels = getChannels();
        return dto;
    }

    private static SoundAttachment fromDto(SoundAttachmentDTO dto) {
        return new SoundAttachment(
                dto.source,
                dto.fileSize,
                dto.duration,
                dto.channels
        );
    }
}
