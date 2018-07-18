package com.riseintech.spulla.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Launk2 on 3/11/2015.
 */
public class MainCardModel implements Serializable {

    public static String KEY_FAVORITE_ARGS = "FAVORITE_KEY";
    public static String KEY_CART_ARGS = "FAVORITE_KEY";

    private String id;
    private String imageUrl;
    private String headerTitle;
    private String secondaryTitle;
    private String listImageUrl;
    private String size;
    private String quantity;
    public String status = "Available";
    public String recentlyImageURL;
    private String discount;

    private float rating;

    public BigDecimal price1, price2;

    private boolean addToFavorite = false;
    private boolean addToCart = false;

    private int sizeIndex, quantityIndex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        this.secondaryTitle = secondaryTitle;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAddToFavorite() {
        return addToFavorite;
    }

    public void setAddToFavorite(boolean addToFavorite) {
        this.addToFavorite = addToFavorite;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isAddToCart() {
        return addToCart;
    }

    public void setAddToCart(boolean addToCart) {
        this.addToCart = addToCart;
    }

    public String getListImageUrl() {
        return listImageUrl;
    }

    public void setListImageUrl(String listImageUrl) {
        this.listImageUrl = listImageUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getSizeIndex() {
        return sizeIndex;
    }

    public void setSizeIndex(int sizeIndex) {
        this.sizeIndex = sizeIndex;
    }

    public int getQuantityIndex() {
        return quantityIndex;
    }

    public void setQuantityIndex(int quantityIndex) {
        this.quantityIndex = quantityIndex;
    }

    public String getRecentlyImageURL() {
        return recentlyImageURL;
    }

    public void setRecentlyImageURL(String recentlyImageURL) {
        this.recentlyImageURL = recentlyImageURL;
    }



}
