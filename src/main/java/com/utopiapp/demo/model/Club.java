package com.utopiapp.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Club {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(nullable = false, columnDefinition = "longtext")
    private String whoAreWe;

    @Column(nullable = false, columnDefinition = "longtext")
    private String organization;

    @Column(nullable = false, unique = true, length = 200)
    private String accessCode;

    @Column(nullable = false)
    private String cif;

    @Column(nullable = false)
    private LocalDate createDate;

    @OneToOne()
    private Address address;

    @OneToMany(mappedBy = "club")
    private Set<ActivitySheet> activitySheets;

    @OneToMany(mappedBy = "club")
    private Set<Petition> petitions;

    @OneToMany(mappedBy = "club")
    private Set<Client> clients;

    @OneToMany(mappedBy = "club")
    private Set<File> files;

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

    public String getWhoAreWe() {
        return whoAreWe;
    }

    public void setWhoAreWe(String whoAreWe) {
        this.whoAreWe = whoAreWe;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<ActivitySheet> getActivitySheets() {
        return activitySheets;
    }

    public void setActivitySheets(Set<ActivitySheet> activitySheets) {
        this.activitySheets = activitySheets;
    }

    public Set<Petition> getPetitions() {
        return petitions;
    }

    public void setPetitions(Set<Petition> petitions) {
        this.petitions = petitions;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }
}
