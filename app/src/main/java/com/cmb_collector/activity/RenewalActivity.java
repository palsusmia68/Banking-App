package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyRenewalAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class RenewalActivity extends AppBaseClass {

    private ImageView rnwl_img;
    private GridView grid_renewal;
    private MyRenewalAdapter myRenewalAdapter;
    private List<Integer> rnImages = new ArrayList<>();
    private List<String> rnTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_renewal);

        rnwl_img = findViewById(R.id.rnwl_img);

        rnwl_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RenewalActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        getRenewal();

    }

    private void getRenewal() {

        rnImages.add(R.drawable.daily_renewal);
        rnTitles.add("Daily Renewal");

        rnImages.add(R.drawable.monthly_renewal);
        rnTitles.add("Monthly Renewal");

        setUpRenewalAdapter();
    }

    private void setUpRenewalAdapter() {

        myRenewalAdapter = new MyRenewalAdapter(this, rnImages, rnTitles);
        grid_renewal = findViewById(R.id.grid_renewal);
        grid_renewal.setAdapter(myRenewalAdapter);

        grid_renewal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent renewal_intent = new Intent(RenewalActivity.this, DailyAndMonthlyRenewal.class);
                renewal_intent.putExtra("RN_TEXT", rnTitles.get(position));
                startActivity(renewal_intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RenewalActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }


}
