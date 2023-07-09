package com.bank.izbank.Bill;

import java.io.Serializable;

public class ElectricBill extends Bill implements Serializable {
    private final String type = "Electric Bill";
    private int amount;
    private Date date;

    public ElectricBill(int amount, Date date) {
        this.amount = amount;
        this.date = date;
    }

    public ElectricBill() {
        date=new Date();
        amount=0;
    }

    public ElectricBill(int amount) {
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
