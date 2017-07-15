package com.example.gindoymz.usave.model;

/**
 * Created by blmorito on 7/15/2017.
 */

public class Account {

    private String accountNo;
    private String currency;
    private String accountName;
    private String status;
    private double availableBalance;
    private double currentBalance;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNo='" + accountNo + '\'' +
                ", currency='" + currency + '\'' +
                ", accountName='" + accountName + '\'' +
                ", status='" + status + '\'' +
                ", availableBalance=" + availableBalance +
                ", currentBalance=" + currentBalance +
                '}';
    }
}
