package com.utopiapp.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Client{

    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, unique = true, length = 200)
    private String username;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Club club;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private File file;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Petition> petitions;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Activity> activities;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Heart> hearts;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Coordinator coordinator;


    public Client(Long id, String name, String username, String lastname, String email, String password, LocalDateTime createdDate, Coordinator coordinator, Club club, Set<Petition> petitions, Set<Activity> activities, Set<Heart> hearts, String description, File file) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.club = club;
        this.petitions = petitions;
        this.activities = activities;
        this.hearts = hearts;
        this.description = description;
        this.coordinator = coordinator;
        this.file = file;
    }

    public Client(String name, String username, String lastname, String email, String password, LocalDateTime createdDate) {
        this.name = name;
        this.username = username;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.petitions = new HashSet<>();
        this.activities = new HashSet<>();
        this.hearts = new HashSet<>();
    }

    public Client() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Heart> getHearts() {
        return hearts;
    }

    public void setHearts(Set<Heart> hearts) {
        this.hearts = hearts;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Set<Petition> getPetitions() {
        return petitions;
    }

    public void setPetitions(Set<Petition> petitions) {
        this.petitions = petitions;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public UserMain toUserMain(){
        return new UserMain(this.id, this.name, this.username, this.lastname, this.email, this.password, this.createdDate, this.club, this.petitions, this.activities, this.hearts, this.description, this.coordinator, this.file);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(name, client.name) && Objects.equals(username, client.username) && Objects.equals(lastname, client.lastname) && Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, lastname, email, createdDate, club);
    }
}
