package com.utopiapp.demo.dto;

import com.utopiapp.demo.model.File;

public class ClubDto {
    private String name;
    private String email;
    private String cif;
    private String whoAreWe;
    private String organization;
    private FileDto logo;
    private FileDto frontPageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
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

    public FileDto getLogo() {
        return logo;
    }

    public void setLogo(FileDto logo) {
        this.logo = logo;
    }

    public FileDto getFrontPageFile() {
        return frontPageFile;
    }

    public void setFrontPageFile(FileDto frontPageFile) {
        this.frontPageFile = frontPageFile;
    }
}
