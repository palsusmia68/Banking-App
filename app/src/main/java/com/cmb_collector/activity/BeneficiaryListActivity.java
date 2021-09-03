package com.cmb_collector.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyBeneficiaryListAdapter;
import com.cmb_collector.model.WCBeneficiaryClass;

import java.util.ArrayList;
import java.util.List;

public class BeneficiaryListActivity extends AppCompatActivity {
    private ImageView img_back;

    private RecyclerView recy_bene_list;

    private List<WCBeneficiaryClass> beneficiaryList = new ArrayList<>();
    private MyBeneficiaryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_list);

        initView();
        setListener();
        getBeneficiaryData();
    }

    private void getBeneficiaryData() {
        beneficiaryList.add(new WCBeneficiaryClass("1234567898", "TEST", "1234"));
        beneficiaryList.add(new WCBeneficiaryClass("1234567898", "TEST", "1234"));
        beneficiaryList.add(new WCBeneficiaryClass("1234567898", "TEST", "1234"));
        setUpBeneficiaryListAdapter();
    }


    private void setUpBeneficiaryListAdapter() {
        adapter = new MyBeneficiaryListAdapter(this, beneficiaryList);
        recy_bene_list.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recy_bene_list.setLayoutManager(layoutManager);
    }

    private void setListener() {
        img_back.setOnClickListener(backListener);

    }

    private void initView() {
        img_back = findViewById(R.id.img_back);

        recy_bene_list = findViewById(R.id.recy_bene_list);
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
