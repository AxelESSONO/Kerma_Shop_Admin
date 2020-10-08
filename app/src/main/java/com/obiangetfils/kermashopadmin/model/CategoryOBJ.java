package com.obiangetfils.kermashopadmin.model;

public class CategoryOBJ {

    String image;
    int icon;
    String name;
    int count;

    public CategoryOBJ() {
    }

    public CategoryOBJ(String image, int icon, String name, int count) {
        this.image = image;
        this.icon = icon;
        this.name = name;
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

