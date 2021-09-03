package com.cmb_collector.utility;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cmb_collector.activity.NoNetworkActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {
    Context context;
    Dialog dialog = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        //    dialog= new Dialog(context);
        try {
            if (isOnline(context)) {
                dialog(true);
            } else {
                dialog(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void dialog(boolean value) {
        if (value) {
            //   showDialog("We are back !!!");
           /* tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText("We are back !!!");
            tv_check_connection.setBackgroundColor(Color.GREEN);
            tv_check_connection.setTextColor(Color.WHITE);

            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    tv_check_connection.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 3000);*/

            Intent local = new Intent();
            local.setAction("com.hello.action");
            context.sendBroadcast(local);
        } else {
            Intent intent = new Intent(context, NoNetworkActivity.class);
            context.startActivity(intent);
            //      showDialog("Could not Connect to internet");
           /* tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText("Could not Connect to internet");
            tv_check_connection.setBackgroundColor(Color.RED);
            tv_check_connection.setTextColor(Color.WHITE);
            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    tv_check_connection.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 3000);*/
        }
    }
   /* public void showDialog(String msg){
       // Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        Button btn_dialog_retry = (Button) dialog.findViewById(R.id.btn_dialog_retry);
        RelativeLayout rl_main = (RelativeLayout) dialog.findViewById(R.id.rl_main);
        if (msg.equals("We are back !!!")) {
            rl_main.setVisibility(View.GONE);
            dialogButton.setText("OK");
            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                      *//*if (dialog.isShowing()) {*//*
                    dialog.dismiss();
                       //}
                }
            };
            handler.postDelayed(delayrunnable, 2000);
        }
        else{
            rl_main.setVisibility(View.VISIBLE);
            dialog.show();
            dialogButton.setText("OPEN SETTING");
        }

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.equals("Could not Connect to internet")){

                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else {
                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            }
        });
        btn_dialog_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });



    }*/

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
