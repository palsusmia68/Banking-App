package com.cmb_collector.utility;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.cmb_collector.R;
import com.cmb_collector.activity.AppBaseActivity;
import com.cmb_collector.activity.LogActivity;
import com.cmb_collector.activity.MainActivity;
import com.cmb_collector.applicationClass.WCApplicationClass;

public class AppBaseClass extends AppBaseActivity {

    private LinearLayout li_blank;
    private LinearLayout li_footer;

    private LinearLayout li_home;
    private LinearLayout li_logout;

    private Intent intent;

    private WCApplicationClass wcApplicationClass;

    private ImageView img_home;
    private ImageView img_logout;
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_base_class);
        initView();
    }

    private void initView() {
        li_blank = findViewById(R.id.li_blank);
        li_footer = findViewById(R.id.li_footer);
        li_home = findViewById(R.id.li_home);
        li_logout = findViewById(R.id.li_logout);

        img_home = findViewById(R.id.img_home);
        img_logout = findViewById(R.id.img_logout);

        li_home.setOnClickListener(homeButton);
        li_logout.setOnClickListener(logoutButton);

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();
    }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void setBodyContentView(int layoutResId) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutResId, null);
        li_blank.addView(view);

    }

    View.OnClickListener homeButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            img_home.setImageResource(R.drawable.home_click);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    };

    View.OnClickListener logoutButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            img_home.setImageResource(R.drawable.tab_icon_one_white);
            img_logout.setImageResource(R.drawable.log_out_click);
            showLogoutDialog();

        }
    };

    private void showLogoutDialog() {
        final Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setCancelable(false);

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.popup_log_out, null);
        customDialog.setContentView(view);

        TextView tv_popup_title = view.findViewById(R.id.tv_popup_title);
        Button btn_1 = view.findViewById(R.id.btn_1);
        Button btn_2 = view.findViewById(R.id.btn_2);

        Typeface regularFont = ResourcesCompat.getFont(this, R.font.calibri_normal);
        Typeface boldFont = ResourcesCompat.getFont(this, R.font.calibri_bold);
        tv_popup_title.setTypeface(boldFont);
        btn_1.setTypeface(regularFont);
        btn_2.setTypeface(regularFont);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finishAffinity();
                customDialog.dismiss();
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.active) {
                    img_home.setImageResource(R.drawable.home_click);
                }
                img_logout.setImageResource(R.drawable.log_out);
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

}
