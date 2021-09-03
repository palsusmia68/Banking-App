package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyLoanSectionAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class LoanSection extends AppBaseClass {

    private Intent intent;
    private ImageView img_loan;
    private List<Integer> lImages = new ArrayList<>();
    private List<String> lTitles = new ArrayList<>();
    private GridView grid_loan_section;
    private MyLoanSectionAdapter loanSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_loan_section);

        img_loan = findViewById(R.id.img_loan);

        img_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoanSection.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });


        getLoanSection();


    }

    private void getLoanSection() {

        lImages.add(R.drawable.loan_requisition);
        lTitles.add("New Loan Requisition");

        lImages.add(R.drawable.emi_payment);
        lTitles.add("EMI Payment");

        lImages.add(R.drawable.non_emi_payment);
        lTitles.add("Non EMI Payment");

        lImages.add(R.drawable.loan_calculator);
        lTitles.add("Loan Calculator");

        setUpLoanAdapter();

    }

    private void setUpLoanAdapter() {

        grid_loan_section = findViewById(R.id.grid_loan_section);
        loanSectionAdapter = new MyLoanSectionAdapter(this, lImages, lTitles);
        grid_loan_section.setAdapter(loanSectionAdapter);

        grid_loan_section.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        intent = new Intent(LoanSection.this, LoanRequisition.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 1:
                        intent = new Intent(LoanSection.this, EMICollection.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 2:
                        intent = new Intent(LoanSection.this, NonEMIPayment.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 3:
                        intent = new Intent(LoanSection.this, LoanCalculator.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoanSection.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }


}
