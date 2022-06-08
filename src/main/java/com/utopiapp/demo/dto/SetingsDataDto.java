package com.utopiapp.demo.dto;

public class SetingsDataDto {
    private String name;
    private String lastname;
    private String email;
    private String confirmPassword1;
    private String newPassword;
    private String repeatPassword;
    private String confirmPassword2;
    private String updatingPassword;

    public SetingsDataDto(String name, String lastname, String email) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
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

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword1() {
        return confirmPassword1;
    }

    public void setConfirmPassword1(String confirmPassword1) {
        this.confirmPassword1 = confirmPassword1;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getConfirmPassword2() {
        return confirmPassword2;
    }

    public void setConfirmPassword2(String confirmPassword2) {
        this.confirmPassword2 = confirmPassword2;
    }

    public String getUpdatingPassword() {
        return updatingPassword;
    }

    public void setUpdatingPassword(String updatingPassword) {
        this.updatingPassword = updatingPassword;
    }
}
