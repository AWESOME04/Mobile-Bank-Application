package com.bank.izbank.Bill;

import java.io.Serializable;

public class InternetBill extends Bill implements Serializable {

    private final String type ="Internet Bill";
    private int amount;
    private Date date;

    public InternetBill( int amount, Date date) {

        this.amount = amount;
        this.date = date;
    }

    public InternetBill() {
        date=new Date();
        amount=0;

    }

    public InternetBill(int amount) {
        this.amount = amount;
        date=new Date();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }
}
