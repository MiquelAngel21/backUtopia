package com.utopiapp.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Activity {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isEvent;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "activity")
    private Set<Guide> guides;

    @ManyToOne
    @JsonIgnore
    private Client client;

    @ManyToMany(mappedBy = "activities")
    private Set<Tag> tags;

    @OneToMany(mappedBy = "activity")
    private Set<Material> materials;

    @OneToMany(mappedBy = "activity")
    private Set<File> files;

    @OneToMany(mappedBy = "activity")
    private Set<Heart> hearts;

    public Activity(String name, boolean isEvent, String description, LocalDateTime createdDate, Client client, Set<Tag> tags, Set<Material> materials, Set<File> files) {
        this.name = name;
        this.isEvent = isEvent;
        this.description = description;
        this.createdDate = createdDate;
        this.guides = new HashSet<>();
        this.client = client;
        this.tags = tags;
        this.materials = materials;
        this.files = files;
        this.hearts = new HashSet<>();
    }

    public Activity() {

    }

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

    public Set<Heart> getHearts() {
        return hearts;
    }

    public void setHearts(Set<Heart> hearts) {
        this.hearts = hearts;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public Set<Guide> getGuide() {
        return guides;
    }

    public void setGuide(Set<Guide> guide) {
        this.guides = guide;
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

    public void setTags(Set<Tag> tag) {
        this.tags = tag;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public Set<Heart> getLikes() {
        return hearts;
    }

    public void setLikes(Set<Heart> hearts) {
        this.hearts = hearts;
    }
}
