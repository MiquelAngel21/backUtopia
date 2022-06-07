package com.utopiapp.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    private Long id;

    @Column(nullable = false, length = 8)
    private String zipCode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String number;

    @OneToOne(mappedBy = "address")
    private Club club;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
