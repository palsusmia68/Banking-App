package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyAccountAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppBaseClass {

    private List<Integer> maImages = new ArrayList<>();
    private List<String> maTitles = new ArrayList<>();
    private MyAccountAdapter accountAdapter;
    private GridView grid_my_account;
    private ImageView acc_img;
    private TextView txt_ac_op_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_account);

        acc_img = findViewById(R.id.acc_img);
        txt_ac_op_date = findViewById(R.id.txt_ac_op_date);

        acc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        getMyAccountDetails();
    }

    private void getMyAccountDetails() {

        maImages.add(R.drawable.new_account);
        maTitles.add("Create Account");

        maImages.add(R.drawable.account_fund_transfer);
        maTitles.add("Account Transaction");

        maImages.add(R.drawable.last_10_transction);
        maTitles.add("Last 10 Transactions");

        maImages.add(R.drawable.account_statement);
        maTitles.add("Account Statement");


//        maImages.add(R.drawable.account_to_investment);
//        maTitles.add("Account To Investment");

//        maImages.add(R.drawable.utility_bill_payment);
//        maTitles.add("Utility Bill Payment");

        setUpMyAccAdapter();

    }

    private void setUpMyAccAdapter() {

        grid_my_account = findViewById(R.id.grid_my_account);
        accountAdapter = new MyAccountAdapter(this, maImages, maTitles);
        grid_my_account.setAdapter(accountAdapter);

        grid_my_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent newac_i = new Intent(AccountActivity.this, NewAccountActivity.class);
                        startActivity(newac_i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 1:
                        Intent ac_sum = new Intent(AccountActivity.this, FundTransferActivity.class);
                        startActivity(ac_sum);
                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 2:
                        Intent last = new Intent(AccountActivity.this, LastTenTransaActivity.class);
                        startActivity(last);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                        break;

                    case 3:
                        Intent aa = new Intent(getApplicationContext(), AccountSummary.class);
                        startActivity(aa);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Clicked : " + maTitles.get(position), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }
}
