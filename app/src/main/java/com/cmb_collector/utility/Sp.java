package com.cmb_collector.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class Sp {

    //Storage File
    public static final String SHARED_PREF_NAME = "CFC";

    //Username
    public static final String ACCOUNT_NAME = "ACCOUNTNUMBER";

    //otp
    public static final String OTP = "OTPSTORE";

    public static Sp mInstance;

    public static Context mCtx;


    public Sp(Context context) {
        mCtx = context;
    }


    public static synchronized Sp getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Sp(context);
        }
        return mInstance;
    }

    //method to store user data
    public void storeAccountName(String names) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCOUNT_NAME, names);
        editor.commit();
    }



    //find logged in user
    public String getAccountName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACCOUNT_NAME, null);

    }

    //method to store otp
    public void storeOtpValue(int otp) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OTP, otp);
        editor.commit();
    }



    //get otp
    public int getOtpValue() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(OTP, 0);

    }
}