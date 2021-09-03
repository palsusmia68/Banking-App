package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyInvestGridAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class InvestmentActivity extends AppBaseClass {

    private ImageView back;
    // private RecyclerView investment_recycler;
    // private MyInvestmentAdapter investmentAdapter;
    private List<Integer> ivImages = new ArrayList<>();
    private List<String> ivTexts = new ArrayList<>();
    private List<String> planCode = new ArrayList<>();
    private MyInvestGridAdapter investGridAdapter;
    private GridView grid_invest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_investment);
        setBodyContentView(R.layout.activity_investment);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvestmentActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        getInvestmentData();

        setPlanCode();

    }

    private void setPlanCode() {
        planCode.add("1003");
        planCode.add("1002");
        planCode.add("1004");
        planCode.add("1005");
    }

    private void getInvestmentData() {

        ivImages.add(R.drawable.rd);
        ivTexts.add("New RD");

        ivImages.add(R.drawable.fd);
        ivTexts.add("New FD");

        ivImages.add(R.drawable.mis);
        ivTexts.add("New MIS");

        ivImages.add(R.drawable.investment_calculator);
        ivTexts.add("Investment Calculator");

        setUpInvestGridAdapter();

    }


    private void setUpInvestGridAdapter() {

        investGridAdapter = new MyInvestGridAdapter(this, ivImages, ivTexts);
        grid_invest = findViewById(R.id.grid_invest);
        grid_invest.setAdapter(investGridAdapter);

        grid_invest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent rd = new Intent(InvestmentActivity.this, RDActivity.class);
                        rd.putExtra("title", ivTexts.get(position));
                        rd.putExtra("plancode", planCode.get(0));
                        startActivity(rd);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 1:
                        Intent fd = new Intent(InvestmentActivity.this, RDActivity.class);
                        fd.putExtra("title", ivTexts.get(position));
                        fd.putExtra("plancode", planCode.get(1));
                        startActivity(fd);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 2:
                        Intent mis = new Intent(InvestmentActivity.this, RDActivity.class);
                        mis.putExtra("title", ivTexts.get(position));
                        mis.putExtra("plancode", planCode.get(3));
                        startActivity(mis);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 3:
                        Intent inv_calc = new Intent(InvestmentActivity.this, InvestmentCalculator.class);
                        startActivity(inv_calc);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    default:
                        break;

                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InvestmentActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }

}
