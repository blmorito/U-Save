package com.example.gindoymz.usave.util;

/**
 * Created by blmorito on 7/16/2017.
 */

public class VendorSession {

    private String accountNo;

    private static VendorSession instance = null;
    private VendorSession() {
        // Exists only to defeat instantiation.
    }

    public static VendorSession getInstance() {
        if(instance == null) {
            instance = new VendorSession();
        }

        return instance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
