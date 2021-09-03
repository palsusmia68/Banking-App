package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyBillSectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class BillPaymentActivity extends AppCompatActivity {

    private ImageView img_bill;
    private GridView grid_bill;
    private List<Integer> billImages = new ArrayList<>();
    private List<String> billTitles = new ArrayList<>();
    private MyBillSectionAdapter billSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);

        img_bill = findViewById(R.id.img_bill);
        img_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillPaymentActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });


        getBillSectionDetails();

    }

    private void getBillSectionDetails() {

        billImages.add(R.drawable.mobile);
        billTitles.add("Mobile");

        billImages.add(R.drawable.postpaid);
        billTitles.add("Postpaid");

        billImages.add(R.drawable.dth);
        billTitles.add("DTH");

        billImages.add(R.drawable.data_card);
        billTitles.add("Data Card");

        setUpBillSectionAdapter();
    }

    private void setUpBillSectionAdapter() {
        grid_bill = findViewById(R.id.grid_bill);
        billSectionAdapter = new MyBillSectionAdapter(this, billImages, billTitles);
        grid_bill.setAdapter(billSectionAdapter);

        grid_bill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent mm = new Intent(BillPaymentActivity.this, MobileRecharge.class);
                        mm.putExtra("title", "Mobile Recharge");
                        startActivity(mm);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 1:
                        Intent mm2 = new Intent(BillPaymentActivity.this, MobileRecharge.class);
                        mm2.putExtra("title", "Postpaid Recharge");
                        startActivity(mm2);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 2:
                        Intent mm3 = new Intent(BillPaymentActivity.this, MobileRecharge.class);
                        mm3.putExtra("title", "DTH Recharge");
                        startActivity(mm3);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 3:
                        Intent mm4 = new Intent(BillPaymentActivity.this, MobileRecharge.class);
                        mm4.putExtra("title", "Data Card Recharge");
                        startActivity(mm4);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Clicked : " + billTitles.get(position), Toast.LENGTH_SHORT).show();
                        break;

                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BillPaymentActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }

}
