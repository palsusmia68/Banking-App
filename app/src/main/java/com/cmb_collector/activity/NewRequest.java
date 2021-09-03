package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyNewRequestAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class NewRequest extends AppBaseClass {

    private ImageView img_back;
    private MyNewRequestAdapter myNewRequestAdapter;
    private List<Integer> nrImages = new ArrayList<>();
    private List<String> nrTitles = new ArrayList<>();
    private GridView grid_new_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBodyContentView(R.layout.activity_new_request);


        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewRequest.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        getRequest();
    }

    private void getRequest() {

        nrImages.add(R.drawable.member_request);
        nrTitles.add("New Member Request");


        nrImages.add(R.drawable.agent_request);
        nrTitles.add("New Agent Request");

        setUpNewRequestUpdate();

    }

    private void setUpNewRequestUpdate() {

        myNewRequestAdapter = new MyNewRequestAdapter(this, nrImages, nrTitles);
        grid_new_request = findViewById(R.id.grid_new_request);
        grid_new_request.setAdapter(myNewRequestAdapter);


        grid_new_request.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent zero = new Intent(NewRequest.this, InsertNewMember.class);
                        startActivity(zero);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 1:
                        Intent one = new Intent(NewRequest.this, NewAgentJoining.class);
                        startActivity(one);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NewRequest.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }

}
