package com.bank.izbank.Bill;

import java.io.Serializable;

public class PhoneBill extends Bill implements Serializable {
    private final String type="Phone Bill";
    private int amount;
    private Date date;

    public PhoneBill( int amount, Date date) {

        this.amount = amount;
        this.date = date;
    }

    public PhoneBill() {
        date=new Date();
        amount=0;

    }

    public PhoneBill(int amount) {
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
