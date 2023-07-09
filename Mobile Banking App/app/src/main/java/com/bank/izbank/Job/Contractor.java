package com.bank.izbank.Job;

public class Contractor extends Job {

    private final String name="Contractor";
    private final String maxCreditAmount="300000";
    private final String maxCreditInstallment ="40";
    private final String interestRate ="1";

    public Contractor() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMaxCreditAmount() {
        return maxCreditAmount;
    }

    @Override
    public String getMaxCreditInstallment() {
        return maxCreditInstallment;
    }

    @Override
    public String getInterestRate() {
        return interestRate;
    }
}
