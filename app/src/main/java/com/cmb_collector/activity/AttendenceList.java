package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyLocatinAdapter;

import java.util.ArrayList;
import java.util.List;

public class AttendenceList extends AppCompatActivity {
    private Intent intent;
    private ImageView img_profile;
    private GridView grid_location;
    private MyLocatinAdapter locationAdapter;
    private List<Integer> pImages = new ArrayList<>();
    private List<String> pTitles = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_list_activity);
        img_profile = findViewById(R.id.img_profile);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendenceList.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        getProfileData();

    }

    private void getProfileData() {

        pImages.add(R.drawable.address);
        pTitles.add("My Attendance");



        setUpProfileAdapter();
    }

    private void setUpProfileAdapter() {
        locationAdapter = new MyLocatinAdapter(this, pImages, pTitles);
        grid_location = findViewById(R.id.grid_location);
        grid_location.setAdapter(locationAdapter);

        grid_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(AttendenceList.this, AttendenceListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;


                    //ODMapsActivity
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AttendenceList.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }
}
