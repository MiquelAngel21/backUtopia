package com.utopiapp.demo.dto;

public class FileDto {
    private String name;
    private String content;
    private String mediaType;
    private Long activity;

    public FileDto(String name, String content, String mediaType, Long activity) {
        this.name = name;
        this.content = content;
        this.mediaType = mediaType;
        this.activity = activity;
    }

    public FileDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getActivity() {
        return activity;
    }

    public void setActivity(Long activity) {
        this.activity = activity;
    }
}
