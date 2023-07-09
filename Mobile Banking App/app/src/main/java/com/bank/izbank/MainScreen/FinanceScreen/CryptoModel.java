package com.bank.izbank.MainScreen.FinanceScreen;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

public class CryptoModel {
    @SerializedName("symbol")
    private String currencySymbol;
    @SerializedName("name")
    private String currencyName;
    @SerializedName("price")
    private String price;
    @SerializedName("logo_url")
    private String logoUrl;
    private ImageView image;
    private String amount;

    public CryptoModel(String currencySymbol, String currencyName, String price, ImageView image,String amount,String logoUrl) {
        this.currencySymbol = currencySymbol;
        this.currencyName = currencyName;
        this.price = price;
        this.image = image;
        this.amount=amount;
        this.logoUrl=logoUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getPrice() {
        return price;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
