package com.utopiapp.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public class UserMain implements UserDetails {
    private Long id;
    private String name;
    private String username;
    private String description;
    private String lastname;
    private String email;
    private String password;
    private LocalDateTime createdDate;
    private Collection<? extends GrantedAuthority> authorities;
    private Club club;
    private Set<Petition> petitions;
    private Set<Activity> activities;
    private Set<Heart> hearts;

    public UserMain(Long id, String name, String username, String lastname, String email, String password, LocalDateTime createdDate, Club club, Set<Petition> petitions, Set<Activity> activities, Set<Heart> hearts, String description) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.description = description;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.petitions = petitions;
        this.activities = activities;
        this.hearts = hearts;
        this.club = club;
    }

    public Client toClient(){
        return new Client(this.id, this.name, this.username, this.lastname, this.email, this.password, this.createdDate, this.club, this.petitions, this.activities, this.hearts, this.description);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    public String getPassword() {
        return this.password;
    }


    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
