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

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "longtext")
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JsonIgnore
    private Client client;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns=
            @JoinColumn(name="activities_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="tags_id", referencedColumnName="id")
    )
    private Set<Tag> tags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns=
            @JoinColumn(name="activities_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="materials_id", referencedColumnName="id")
    )
    private Set<Material> materials;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
            joinColumns=
            @JoinColumn(name="activities_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="files_id", referencedColumnName="id")
    )
    private Set<File> files;

    @OneToMany(mappedBy = "activity", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Heart> hearts;

    public Activity(String name, String description, LocalDateTime createdDate, Client client, Set<Tag> tags, Set<Material> materials, Set<File> files) {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.client = client;
        this.tags = tags;
        this.materials = materials;
        this.files = files;
        this.hearts = new HashSet<>();
    }

    public Activity(String name, String description, LocalDateTime createdDate, Client client) {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.client = client;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                '}';
    }
}
