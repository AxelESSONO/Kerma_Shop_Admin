package com.obiangetfils.kermashopadmin.model;

public class HomeObject {

    private String nameHome;
    private int homeImage;

    public HomeObject(String nameHome, int homeImage) {
        this.nameHome = nameHome;
        this.homeImage = homeImage;
    }

    public String getNameHome() {
        return nameHome;
    }

    public void setNameHome(String nameHome) {
        this.nameHome = nameHome;
    }

    public int getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(int homeImage) {
        this.homeImage = homeImage;
    }
}
