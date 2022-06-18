package com.utopiapp.demo.dto;

public class PasswordsSettingsDto {
    private String newPassword;
    private String repeatPassword;
    private String confirmPassword1;
    private String confirmPassword2;

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

    public String getConfirmPassword1() {
        return confirmPassword1;
    }

    public void setConfirmPassword1(String confirmPassword1) {
        this.confirmPassword1 = confirmPassword1;
    }

    public String getConfirmPassword2() {
        return confirmPassword2;
    }

    public void setConfirmPassword2(String confirmPassword2) {
        this.confirmPassword2 = confirmPassword2;
    }


}
