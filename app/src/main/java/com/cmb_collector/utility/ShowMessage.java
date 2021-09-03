package com.cmb_collector.utility;

import android.content.Context;
import android.widget.Toast;

public class ShowMessage {

    public ShowMessage(Context mContex, String message) {
        Toast.makeText(mContex, message, Toast.LENGTH_LONG).show();
    }
}
