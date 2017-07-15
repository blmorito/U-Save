package com.example.gindoymz.usave.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by blmorito on 7/15/2017.
 */

public class AccountMapper {

    public static Account map(JSONObject json){

        Account account = new Account();
        try {
            account.setAccountNo(json.getString("account_no"));
            account.setAccountName(json.getString("account_name"));
            account.setCurrency(json.getString("currency"));
            account.setStatus(json.getString("status"));
            account.setAvailableBalance(Double.parseDouble(json.getString("available_balance")));
            account.setCurrentBalance(Double.parseDouble(json.getString("current_balance")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return account;
    }

}
