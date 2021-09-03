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
import com.cmb_collector.adapter.AttendencelistAdapter;
import com.cmb_collector.model.AttendenceDataClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;

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

public class AttendenceListActivity extends AppCompatActivity {
    private WCUserClass userClass;
    List<AttendenceDataClass> attendenceDataClasses;
    ArrayList<AttendenceDataClass> arrlist = new ArrayList<AttendenceDataClass>();
    AttendencelistAdapter adapter;
    RecyclerView recyclerView;
    ImageView ff_date,tt_date;
    private Calendar myDob = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dobDate;
    private Calendar myDob1 = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dobDate1;
    TextView f_date,t_date;
    Button searchID;
    private ImageView img_profile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendenceview_activity);
        userClass = WCUserClass.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendenceDataClasses = new ArrayList<>();
        ff_date = (ImageView)findViewById(R.id.ff_date);
        tt_date = (ImageView)findViewById(R.id.tt_date);
        f_date = (TextView)findViewById(R.id.f_date);
        t_date = (TextView)findViewById(R.id.t_date);
        searchID = (Button)findViewById(R.id.searchID);
        img_profile = findViewById(R.id.img_profile);
        dobDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myDob.set(Calendar.YEAR, year);
                myDob.set(Calendar.MONTH, monthOfYear);
                myDob.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateFormat();
            }
        };
        ff_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(AttendenceListActivity.this, dobDate,
                        myDob.get(Calendar.YEAR),
                        myDob.get(Calendar.MONTH),
                        myDob.get(Calendar.DAY_OF_MONTH));

                date.getDatePicker().setMaxDate(System.currentTimeMillis());
                date.show();

            }
        });
        dobDate1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myDob1.set(Calendar.YEAR, year);
                myDob1.set(Calendar.MONTH, monthOfYear);
                myDob1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateFormat1();
            }
        };
        tt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(AttendenceListActivity.this, dobDate1,
                        myDob1.get(Calendar.YEAR),
                        myDob1.get(Calendar.MONTH),
                        myDob1.get(Calendar.DAY_OF_MONTH));
                date.getDatePicker().setMaxDate(System.currentTimeMillis());
                date.show();
            }
        });
        searchID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendenceDataClasses.clear();
                Attendence();
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendenceListActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }
    private void updateDateFormat() {
        String myformat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        f_date.setText(sdf.format(myDob.getTime()));
    }
    private void updateDateFormat1() {
        String myformat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        t_date.setText(sdf.format(myDob.getTime()));
    }
    public void Attendence(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_GetWorkingDetailsDateWise",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("GET_GetWorkingDetailsDateWise"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Error_Code = jsonObject.getString("Error_Code");
                            if (Error_Code.equals("0")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("GetLocationGetails");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String CollectorCode = object.getString("CollectorCode");
                                    String date = object.getString("date");
                                    String InTime = object.getString("InTime");
                                    String RunTime = object.getString("RunTime");
                                    String OutTime = object.getString("OutTime");
                                    String TYPE = object.getString("TYPE");
                                    String HH = object.getString("HH");
                                    String MI = object.getString("MI");
                                    attendenceDataClasses.add(new AttendenceDataClass(
                                            object.getString("CollectorCode"),
                                            object.getString("date"),
                                            object.getString("InTime"),
                                            object.getString("RunTime"),
                                            object.getString("OutTime"),
                                            object.getString("TYPE"),
                                            object.getString("HH"),
                                            object.getString("MI")
                                    ));
                                    arrlist.addAll(attendenceDataClasses);
                                }
                                adapter = new AttendencelistAdapter(AttendenceListActivity.this, attendenceDataClasses);
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                recyclerView.setAdapter(null);
                            }
                            adapter.notifyDataSetChanged();
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
                params.put("CollectorCode", userClass.getGlobalUserCode());
                params.put("Fdate", f_date.getText().toString());
                params.put("Tdate", t_date.getText().toString());
                System.out.println("GET_GetWorkingDetailsDateWise" + params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 1, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
