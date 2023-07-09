package com.bank.izbank.UserInfo;

import java.util.Random;

public class CreditCard {
    private int limit;
    private String creditCardNo;

    public CreditCard(int limit) {
        this.limit = limit;
        this.creditCardNo = setCreditCardNo();
    }
    public CreditCard(String no, int limit) {
        this.limit = limit;
        this.creditCardNo = no;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String setCreditCardNo(){
        String no = "**** **** **** ";
        Random rnd = new Random();
        for (int i = 0; i <4; i++){
            int random = rnd.nextInt(10);
            no = no + String.valueOf(random);
        }

        return no;

    }

    public String getCreditCardNo() {
        return creditCardNo;
    }
}
