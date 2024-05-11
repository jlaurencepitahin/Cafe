package com.example.cafe.DataModel;

import java.io.Serializable;

public class Sale implements Serializable {
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String productName;

    public String getSaleID() {
        return saleID;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    public String saleID;
    public String price;
    public String quantity;
    public String date;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String category;
}
