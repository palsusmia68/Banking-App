package com.cmb_collector.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmb_collector.R;

import java.text.DateFormat;
import java.util.Date;

public class NewDeposit extends AppCompatActivity {

    private ImageView depo_back;
    private ImageView logout;
    private Spinner plan_name_spinner;
    private String[] PlanName = {"RD", "FD", "PPF"};
    private TextView txt_current_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deposit);

        depo_back = findViewById(R.id.depo_back);
        logout = findViewById(R.id.logout);
        plan_name_spinner = findViewById(R.id.plan_name_spinner);
        txt_current_date = findViewById(R.id.txt_current_date);


        depo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewDeposit.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PlanName);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan_name_spinner.setAdapter(ad);

        //Current Date & Time Fetched:
//       String current_date = DateFormat.getDateTimeInstance().format(new Date());
//       txt_current_date.setText(current_date);

        //Current Date Fetched:
        String current_date = DateFormat.getDateInstance().format(new Date());
        txt_current_date.setText(current_date);


    }


}
