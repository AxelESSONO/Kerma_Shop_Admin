package com.obiangetfils.kermashopadmin.model;

public class ProductOBJ {

    private int ID;
    private String title;
    private String image;
    private String newPrice;
    private String oldPrice;
    private boolean isNewTag;
    private boolean isSaleTag;
    private boolean isFeaturedTag;
    private boolean isFavTag;
    private int categoryID;
    private int defaultStock;
    private String productType;

    public ProductOBJ(int ID,
                      String title,
                      String image,

                      String newPrice,
                      String oldPrice,

                      boolean isNewTag,
                      boolean isSaleTag,
                      boolean isFeaturedTag,
                      boolean isFavTag,

                      int categoryID,
                      int defaultStock,
                      String productType)
    {
        this.ID = ID;
        this.title = title;
        this.image = image;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.isNewTag = isNewTag;
        this.isSaleTag = isSaleTag;
        this.isFeaturedTag = isFeaturedTag;
        this.isFavTag = isFavTag;
        this.categoryID = categoryID;
        this.defaultStock = defaultStock;
        this.productType = productType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public boolean isNewTag() {
        return isNewTag;
    }

    public void setNewTag(boolean newTag) {
        isNewTag = newTag;
    }

    public boolean isSaleTag() {
        return isSaleTag;
    }

    public void setSaleTag(boolean saleTag) {
        isSaleTag = saleTag;
    }

    public boolean isFeaturedTag() {
        return isFeaturedTag;
    }

    public void setFeaturedTag(boolean featuredTag) {
        isFeaturedTag = featuredTag;
    }

    public boolean isFavTag() {
        return isFavTag;
    }

    public void setFavTag(boolean favTag) {
        isFavTag = favTag;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getDefaultStock() {
        return defaultStock;
    }

    public void setDefaultStock(int defaultStock) {
        this.defaultStock = defaultStock;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}


