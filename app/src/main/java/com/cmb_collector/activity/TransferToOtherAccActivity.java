package com.cmb_collector.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cmb_collector.R;
import com.cmb_collector.utility.AppBaseClass;


public class TransferToOtherAccActivity extends AppBaseClass {
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_transfer_to_other_acc);
        initView();
        setListener();

    }

    private void initView() {
        img_back = findViewById(R.id.img_back);
    }

    private void setListener() {
        img_back.setOnClickListener(backListener);
    }

    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


}
