package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyProfileAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppBaseClass {

    private Intent intent;

    private ImageView img_profile;
    private GridView grid_profile;

    private MyProfileAdapter profileAdapter;

    private List<Integer> pImages = new ArrayList<>();
    private List<String> pTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_profile);

        img_profile = findViewById(R.id.img_profile);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        getProfileData();

    }

    private void getProfileData() {

        pImages.add(R.drawable.own_profile);
        pTitles.add("Own Profile");

        pImages.add(R.drawable.member_profile);
        pTitles.add("Member Profile");

        pImages.add(R.drawable.downline_view);
        pTitles.add("Downline View");

       /* pImages.add(R.drawable.chain_view);
        pTitles.add("Chain View");*/

        setUpProfileAdapter();
    }

    private void setUpProfileAdapter() {
        profileAdapter = new MyProfileAdapter(this, pImages, pTitles);
        grid_profile = findViewById(R.id.grid_profile);
        grid_profile.setAdapter(profileAdapter);

        grid_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(ProfileActivity.this, OwnProfile.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 1:
                        intent = new Intent(ProfileActivity.this, MemberProfile.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 2:
                        intent = new Intent(ProfileActivity.this, DownlineViewActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }
}
