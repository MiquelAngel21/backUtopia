package com.utopiapp.demo.dto;

public class SettingsDataDto {
    private String name;
    private String lastname;
    private String email;
    private String description;
    private String password;
    private FileDto file;

    public SettingsDataDto(String name, String lastname, String email, String description, String password, FileDto file) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.description = description;
        this.password = password;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FileDto getFile() {
        return file;
    }

    public void setFile(FileDto file) {
        this.file = file;
    }
}

