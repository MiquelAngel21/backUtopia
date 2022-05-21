package com.utopiapp.demo.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
public class Client{

    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 200)
    private String username;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne()
    private Category category;

    @ManyToOne()
    private Club club;

    @OneToMany(mappedBy = "client")
    private Set<Petition> petitions;

    @OneToMany(mappedBy = "client")
    private Set<Activity> activities;

    @OneToMany(mappedBy = "client")
    private Set<ActivitySheet> activitySheets;

    @OneToMany(mappedBy = "client")
    private Set<Heart> hearts;

    public Client(Long id, String name, String username, String lastname, String email, String password, LocalDateTime createdDate, Role role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.role = role;
    }

    public Client(String name, String username, String lastname, String email, String password, LocalDateTime createdDate, Role role) {
        this.name = name;
        this.username = username;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public Set<ActivitySheet> getActivitySheets() {
        return activitySheets;
    }

    public void setActivitySheets(Set<ActivitySheet> activitySheets) {
        this.activitySheets = activitySheets;
    }

    public Set<Heart> getLikes() {
        return hearts;
    }

    public void setLikes(Set<Heart> hearts) {
        this.hearts = hearts;
    }

    public UserMain toUserMain(){
        return new UserMain(this.id, this.name, this.username, this.lastname, this.email, this.password, this.createdDate, this.role);
    }
}
