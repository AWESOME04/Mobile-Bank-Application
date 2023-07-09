package com.bank.izbank.Bill;

import java.io.Serializable;

public class WaterBill extends Bill implements Serializable {

    private final String type = "Water Bill";
    private int amount;
    private Date date;

    public WaterBill( int amount, Date date) {

        this.amount = amount;
        this.date = date;
    }

    public WaterBill() {
        date=new Date();
        amount=0;

    }

    public WaterBill(int amount) {
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
