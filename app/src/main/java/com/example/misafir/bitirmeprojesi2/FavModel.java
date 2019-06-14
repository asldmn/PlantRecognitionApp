package com.example.misafir.bitirmeprojesi2;

/**
 * Created by MISAFIR on 24.04.2018.
 */

public class FavModel {

    private String imageurl;
    private String visionApiData;
    private String shopID;
    boolean favori;


    public FavModel() {

    }

    public FavModel(String imageurl, String visionApiData, String shopID, boolean favori) {

        this.imageurl = imageurl;
        this.visionApiData = visionApiData;
        this.shopID = shopID;
        this.favori = favori;


    }


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getvisionApiData() {
        return visionApiData;
    }

    public void setVisionApiData(String visionApiData) {
        this.visionApiData = visionApiData;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public boolean getFavori() {
        return favori;
    }

    public void setFavori(boolean favori) {
        this.favori = favori;
    }
}


