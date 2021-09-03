package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyMoneyTransferAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class MoneyTransferActivity extends AppBaseClass {
    private GridView first_gridView;
    private ImageView img_mon_tran;

    private List<Integer> listImages = new ArrayList<>();
    private List<String> listTexts = new ArrayList<>();

    private MyMoneyTransferAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_money_transfer2);

        img_mon_tran = findViewById(R.id.img_mon_tran);

        img_mon_tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyTransferActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        getGridData();
    }

    private void getGridData() {

        listImages.add(R.drawable.account_fund_transfer);
        listTexts.add("Add Beneficiary");

        listImages.add(R.drawable.account_fund_transfer);
        listTexts.add("Beneficiary List");

        listImages.add(R.drawable.account_fund_transfer);
        listTexts.add("Add Fund");

        listImages.add(R.drawable.account_fund_transfer);
        listTexts.add("Transfer To Own Account");

        listImages.add(R.drawable.account_fund_transfer);
        listTexts.add("Transfer To Other Bank Account");

        listImages.add(R.drawable.account_fund_transfer);
        listTexts.add("Transfer To Other Account");


        initSetUpAdapter();
    }

    private void initSetUpAdapter() {

        first_gridView = findViewById(R.id.first_gridView);
        gridAdapter = new MyMoneyTransferAdapter(this, listImages, listTexts);
        first_gridView.setAdapter(gridAdapter);

        first_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        Intent i = new Intent(getApplicationContext(), AddBeneficiaryActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 1:
                        Intent ii = new Intent(getApplicationContext(), BeneficiaryListActivity.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 2:
                        Intent jj = new Intent(MoneyTransferActivity.this, AddFundActivity.class);
                        startActivity(jj);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 3:
                        Intent lll = new Intent(MoneyTransferActivity.this, TransferToOwnAccActivity.class);
                        startActivity(lll);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 4:
                        Intent bil = new Intent(MoneyTransferActivity.this, TransferOtherBankActivity.class);
                        startActivity(bil);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 5:
                        Intent inv = new Intent(MoneyTransferActivity.this, TransferToOtherAccActivity.class);
                        startActivity(inv);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;


                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MoneyTransferActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }
}
