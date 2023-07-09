package com.bank.izbank.Credit;

public class Credit {

    private int amount;
    private int installment;
    private int interestRate;
    private int payAmount;

    public Credit(int amount, int installment, int interestRate,int payAmount) {
        this.amount = amount;
        this.installment = installment;
        this.interestRate = interestRate;
        this.payAmount = payAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInstallment() {
        return installment;
    }

    public void setInstallment(int installment) {
        this.installment = installment;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }
}
