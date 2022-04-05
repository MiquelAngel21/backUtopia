package com.utopiapp.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Activity {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    private Long id;

    @Column(nullable = false)
    private boolean isEvent;

    @Column(nullable = false)
    private String description;

    @OneToOne(mappedBy = "activity")
    private Guide guide;

    @ManyToOne
    private Client client;

    @ManyToMany(mappedBy = "activities")
    private Set<Tag> tag;

    @OneToMany(mappedBy = "activity")
    private Set<Material> materials;

    @OneToMany(mappedBy = "activity")
    private Set<File> files;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
