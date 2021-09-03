package com.cmb_collector.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cmb_collector.R;
import com.cmb_collector.adapter.ShowlocationAdapter;
import com.cmb_collector.model.LocationDataClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LogviewActivity extends AppCompatActivity {
    private ImageView img_profile;
    RecyclerView recyclerView;
    private WCUserClass userClass;
    List<LocationDataClass> locationDataClasses;
    ArrayList<LocationDataClass> arrlist = new ArrayList<LocationDataClass>();
    ShowlocationAdapter adapter;
    Button cal;
    private Calendar myDob = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dobDate;
    TextView txt_tgl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logview_activity);
        img_profile = findViewById(R.id.img_profile);
        cal = (Button)findViewById(R.id.cal);
        txt_tgl = (TextView)findViewById(R.id.txt_tgl);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogviewActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userClass = WCUserClass.getInstance();
        locationDataClasses = new ArrayList<>();
        dobDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myDob.set(Calendar.YEAR, year);
                myDob.set(Calendar.MONTH, monthOfYear);
                myDob.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateFormat();
                updateDateFormat1();
            }
        };
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(LogviewActivity.this, dobDate,
                        myDob.get(Calendar.YEAR),
                        myDob.get(Calendar.MONTH),
                        myDob.get(Calendar.DAY_OF_MONTH));

                date.getDatePicker().setMaxDate(System.currentTimeMillis());
                date.show();

            }
        });
        latlongLog(Utility.setCurrentDate());
    }
    private void updateDateFormat() {
        String myformat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        txt_tgl.setText(sdf.format(myDob.getTime()));
       // et_nominee_age.setText(Integer.toString(calculateAge1(myDob1.getTimeInMillis())));
    }
    private void updateDateFormat1() {
        String myformat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        //txt_tgl.setText(sdf.format(myDob.getTime()));
        latlongLog(sdf.format(myDob.getTime()));
        // et_nominee_age.setText(Integer.toString(calculateAge1(myDob1.getTimeInMillis())));
    }
    private void latlongLog(String date){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_GetLocationGetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("PUT_InsertLocationSearch"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Error_Code = jsonObject.getString("Error_Code");
                            if (Error_Code.equals("0")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("GetLocationGetails");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String date = object.getString("date");
                                    String TIME = object.getString("TIME");
                                    String Type = object.getString("Type");
                                    String Lat = object.getString("Lat");
                                    String Long = object.getString("Long");
                                    String Address = object.getString("Address");
                                    locationDataClasses.add(new LocationDataClass(
                                            object.getString("date"),
                                            object.getString("TIME"),
                                            object.getString("Type"),
                                            object.getString("Lat"),
                                            object.getString("Long"),
                                            object.getString("Address")
                                    ));
                                    arrlist.addAll(locationDataClasses);
                                }
                                adapter = new ShowlocationAdapter(LogviewActivity.this, locationDataClasses);
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                recyclerView.setAdapter(null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ArrangerCode", userClass.getGlobalUserCode());
                params.put("Edate", date);
                System.out.println("PUT_InsertLocationSearch" + params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 1, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }



}
