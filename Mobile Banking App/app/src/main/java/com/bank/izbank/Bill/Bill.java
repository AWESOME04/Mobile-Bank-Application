package com.bank.izbank.Bill;

import java.io.Serializable;

public class Bill implements Serializable {

    private String type;
    private int amount;
    private Date date;

    public Bill(String type, int amount, Date date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public Bill() {
    }

    public Bill(String type, int amount) {
        this.type = type;
        this.amount = amount;
        date.setYear("0");
        date.setMonth("0");
        date.setDay("0");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
