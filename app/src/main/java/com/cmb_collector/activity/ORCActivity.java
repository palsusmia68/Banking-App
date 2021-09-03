package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyOrcGridAdapter;
import com.cmb_collector.utility.AppBaseClass;

import java.util.ArrayList;
import java.util.List;

public class ORCActivity extends AppBaseClass {

    private ImageView img_orc;
    private GridView grid_orc;
    private MyOrcGridAdapter orcGridAdapter;
    private List<Integer> oImages = new ArrayList<>();
    private List<String> oTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_orc);

        img_orc = findViewById(R.id.img_orc);

        img_orc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ORCActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        getORCData();
    }

    private void getORCData() {

        oImages.add(R.drawable.own_orc);
        oTitles.add("View Own ORC");

        oImages.add(R.drawable.own_orc);
        oTitles.add("View Down Agent ORC");

       /* oImages.add(R.drawable.own_orc);
        oTitles.add("View ORC List");*/

        setUpORCAdapter();

    }

    private void setUpORCAdapter() {
        grid_orc = findViewById(R.id.grid_orc);
        orcGridAdapter = new MyOrcGridAdapter(this, oImages, oTitles);
        grid_orc.setAdapter(orcGridAdapter);

        grid_orc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(ORCActivity.this, ViewORCActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

                    case 1:
                        startActivity(new Intent(ORCActivity.this, ViewDownORCActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;

               /*     case 2:

                        break;*/
                    default:
                        Toast.makeText(getApplicationContext(), "Clicked :" + oTitles.get(position), Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ORCActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }


}
