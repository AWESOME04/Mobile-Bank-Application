package com.bank.izbank.UserInfo;

import java.util.Random;

public class BankAccount {

    private int cash;
    private  String accountno;


    public BankAccount(int cash) {
        this.cash = cash;
        this.accountno = setBankAccountNo();
    }
    public BankAccount(String no,int cash) {
        this.cash = cash;
        this.accountno = no;
    }

    public String setBankAccountNo(){
        String no= "";
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int radnomint = rnd.nextInt(10);
            no = no + String.valueOf(radnomint);
        }

        return no;

    }

    public String getAccountno() {
        return accountno;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}
