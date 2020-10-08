package com.obiangetfils.kermashopadmin.model;

public class AdminModel {

    String admin_firstname, admin_Name, admin_password, admin_phone, admin_email;

    public AdminModel() {
    }

    public AdminModel(String admin_firstname, String admin_Name, String admin_password, String admin_phone, String admin_email) {
        this.admin_firstname = admin_firstname;
        this.admin_Name = admin_Name;
        this.admin_password = admin_password;
        this.admin_phone = admin_phone;
        this.admin_email = admin_email;
    }

    public String getAdmin_firstname() {
        return admin_firstname;
    }

    public void setAdmin_firstname(String admin_firstname) {
        this.admin_firstname = admin_firstname;
    }

    public String getAdmin_Name() {
        return admin_Name;
    }

    public void setAdmin_Name(String admin_Name) {
        this.admin_Name = admin_Name;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    public String getAdmin_phone() {
        return admin_phone;
    }

    public void setAdmin_phone(String admin_phone) {
        this.admin_phone = admin_phone;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }
}
