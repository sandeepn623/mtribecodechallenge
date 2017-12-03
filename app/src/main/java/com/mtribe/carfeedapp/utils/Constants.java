package com.mtribe.carfeedapp.utils;

/**
 * Created by Sandeepn on 03-12-2017.
 */

public class Constants {
    public static final String URL = "http://data.m-tribes.com/locations.json";

    public static final int HTTP_OK_MSG         = 1001;

    public static final int HTTP_FAILED_MSG     = HTTP_OK_MSG + 1;

    public static final int INSERT_DB_SUCCESS   = HTTP_FAILED_MSG + 1;

    public static final int NUMBER_OF_RECORDS   = INSERT_DB_SUCCESS + 1;

    public static final int NOTIFY_UI_SUCCESS   = NUMBER_OF_RECORDS + 1;

    public static final int NOTIFY_UI_FAILED    = NOTIFY_UI_SUCCESS + 1;

}
