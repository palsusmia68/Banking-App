package com.cmb_collector.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {

    public static final String ERROR_TITLE = "Connectivity Error !!!";
    public static final String ERROR_MSG = "Sorry! Internet connection not found";
    public static final String ERROR = "Error!!";

    public static boolean internetOnline(Context c) {

        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                status = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                status = true;
            }
        } else {
            // not connected to the internet
            status = false;
        }

        return status;
    }


}
