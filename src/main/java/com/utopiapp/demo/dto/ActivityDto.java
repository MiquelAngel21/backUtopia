package com.utopiapp.demo.dto;

import com.utopiapp.demo.model.*;

import java.time.LocalDateTime;
import java.util.Set;

public class ActivityDto {
    private Long id;
    private String name;
    private boolean isEvent;
    private String description;
    private LocalDateTime createdDate;
    private Set<Guide> guides;
    private Client client;
    private Set<Tag> tags;
    private Set<Material> materials;
    private Set<FileDto> files;
    private Set<Heart> hearts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Guide> getGuides() {
        return guides;
    }

    public void setGuides(Set<Guide> guides) {
        this.guides = guides;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    public Set<FileDto> getFiles() {
        return files;
    }

    public void setFiles(Set<FileDto> files) {
        this.files = files;
    }

    public Set<Heart> getHearts() {
        return hearts;
    }

    public void setHearts(Set<Heart> hearts) {
        this.hearts = hearts;
    }
}
