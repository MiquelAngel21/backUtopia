package com.utopiapp.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Club {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, unique = true, length = 200)
    private String accessCode;

    @Column(nullable = false)
    private String cif;

    @Column(nullable = false)
    private LocalDate createDate;

    @OneToMany(mappedBy = "club")
    private Set<Category> categories;

    @OneToOne(mappedBy = "club")
    private Address addresses;

    @OneToMany(mappedBy = "club")
    private Set<ActivitySheet> activitySheets;

    @OneToMany(mappedBy = "club")
    private Set<Petition> petitions;

    @OneToMany(mappedBy = "club")
    private Set<Client> clients;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
}
