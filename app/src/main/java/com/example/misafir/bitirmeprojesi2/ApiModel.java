package com.example.misafir.bitirmeprojesi2;

/**
 * Created by MISAFIR on 24.04.2018.
 */

public class ApiModel {

    private String imageurl;
    private String visionApiData;
    private String shopID;



    public ApiModel() {

    }

    public ApiModel(String imageurl, String visionApiData, String shopID) {

            this.imageurl = imageurl;
            this.visionApiData = visionApiData;
            this.shopID = shopID;


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
}

