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
    public static String CLIENT_ID = "ed000e77-71f0-4592-991d-aa1bd4bc49cc";
    public static String CLIENT_SECRET = "qV7gL5oL8kX2rL7tR3jX5dO7fC8iX4pL5kU7kX5lG1fB7dT7yK";


    //headers
    public static String HEADER_CLIENTID = "x-ibm-client-id";
    public static String HEADER_CLIENTSECRET = "x-ibm-client-secret";


}
