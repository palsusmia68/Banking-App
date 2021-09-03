package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyQueryAccountAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class QueryAccountActivity extends AppBaseClass {
    private ImageView img_query;
    private MyQueryAccountAdapter queryAccountAdapter;
    private List<Integer> qImages = new ArrayList<>();
    private List<String> qTitles = new ArrayList<>();
    private GridView grid_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_query_account);

        img_query = findViewById(R.id.img_query);

        img_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QueryAccountActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        getQueryDetails();

    }

    private void getQueryDetails() {
        qImages.add(R.drawable.daily_rd);
        qTitles.add("Daily RD");

        qImages.add(R.drawable.rd);
        qTitles.add("RD");

        qImages.add(R.drawable.sb_account);
        qTitles.add("SB ACCOUNT");

        qImages.add(R.drawable.fd);
        qTitles.add("FD");

        qImages.add(R.drawable.loan_emi);
        qTitles.add("LOAN EMI");

        setUpQueryAccountAdapter();
    }

    private void setUpQueryAccountAdapter() {
        grid_query = findViewById(R.id.grid_query);
        queryAccountAdapter = new MyQueryAccountAdapter(this, qImages, qTitles);
        grid_query.setAdapter(queryAccountAdapter);
        grid_query.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent rec_i = new Intent(QueryAccountActivity.this, RecurringRenewal.class);
                        rec_i.putExtra("Policy_type","Dly.");
                        startActivity(rec_i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 1:
                        Intent recc_i = new Intent(QueryAccountActivity.this, RecurringRenewal.class);
                        recc_i.putExtra("Policy_type","Mly.");
                        startActivity(recc_i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 2:
                        Intent sb_i = new Intent(QueryAccountActivity.this, SBAcount.class);
                        startActivity(sb_i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 3:
                        Intent fd_i = new Intent(QueryAccountActivity.this, FDActivity.class);
                        startActivity(fd_i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 4:
                        Intent loan_i = new Intent(QueryAccountActivity.this, LoanEMI.class);
                        startActivity(loan_i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Clicked : " + qTitles.get(position), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QueryAccountActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }

}
