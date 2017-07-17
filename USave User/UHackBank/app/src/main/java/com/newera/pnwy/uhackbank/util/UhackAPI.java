package com.newera.pnwy.uhackbank.util;

/**
 * Created by blmorito on 7/15/2017.
 */

public class UhackAPI {

    public static String UHACK_BASE_URL = "https://api-uat.unionbankph.com/uhac/sandbox/";
    public static String ACCOUNT_INFO = "accounts/";
    public static String FUND_TRANSFER = "transfers/initiate";
    public static String REQUEST_TEST_ACCOUNT = "test/accounts";

    //API KEYS -- need to secure
    public static String CLIENT_ID = "REPLACE_WITH_PROVIDED_CLIENT_ID";
    public static String CLIENT_SECRET = "REPLACE_WITH_PROVIDED_CLIENT_ID_SECRET";


    //headers
    public static String HEADER_CLIENTID = "x-ibm-client-id";
    public static String HEADER_CLIENTSECRET = "x-ibm-client-secret";


}
