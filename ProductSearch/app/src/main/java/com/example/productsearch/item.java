package com.example.productsearch;

import android.media.Image;

import org.json.JSONArray;
import org.json.JSONObject;

public class item {
    private String Title;
    private String Zip;
    private String Shipping;
    private String Condition;
    private String Price;
    private String Image;
    private String Id;

    public item() {
    }

    public item(String title,String zip,String shipping,String condition,String price,String image,String id){
        Title = title;
        Zip = zip;
        Shipping = shipping;
        Condition = condition;
        Price = price;
        Image = image;
        Id = id;

    }


    public void setId(String id) { Id = id; }

    public String getId() { return Id; }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getShipping() {
        return Shipping;
    }

    public void setShipping(String shipping) {
        Shipping = shipping;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage(){
        return Image;
    }
    public void setImage(String image){
        Image = image;
    }
}
