package com.example.productsearch;

public class SimilarItem {
    private int Index;
    private String Image;
    private String Title;
    private Double Shipping;
    private int Daysleft;
    private Double Price;
    private String Viewurl;

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Double getShipping() {
        return Shipping;
    }

    public void setShipping(Double shipping) {
        Shipping = shipping;
    }

    public int getDaysleft() {
        return Daysleft;
    }

    public void setDaysleft(int daysleft) {
        Daysleft = daysleft;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getViewurl() {
        return Viewurl;
    }

    public void setViewurl(String viewurl) {
        Viewurl = viewurl;
    }

    public SimilarItem(int index, String image, String title, Double shipping, int daysleft, Double price, String viewurl) {
        Index = index;
        Image = image;
        Title = title;
        Shipping = shipping;
        Daysleft = daysleft;
        Price = price;
        Viewurl = viewurl;
    }
}
