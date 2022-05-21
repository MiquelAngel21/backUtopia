package com.utopiapp.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class ActivitySheet {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate activityDate;

    @ManyToMany()
    private Set<Petition> petitions;

    @ManyToOne()
    private Category category;

    @ManyToOne
    private Club club;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "activitySheet")
    private Set<Guide> guides;

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

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }
}
