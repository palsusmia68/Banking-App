package com.cmb_collector.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.adapter.MyRecyclerRenewalAdapter;
import com.cmb_collector.model.WCRenewalDataClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyAndMonthlyRenewal extends AppBaseClass {

    private AutoCompleteTextView et_loan_search;
    private LinearLayout li_header;
    private String renewal_title;
    private ImageView rnwl_img;
    private TextView txt_rnwl;
    private Button btn_search;

    private RecyclerView list_renewal;
    private List<String> applicantList = new ArrayList<>();
    private List<WCRenewalDataClass> renewalDataClassList = new ArrayList<>();

    private String policyNo;
    private String appName;
    private String amount;
    private String plan;
    private String deposit;

    private MyRecyclerRenewalAdapter adapter;
    private WCUserClass userClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_daily_and_monthly_renewal);

        init();
        setDefaultValues();
        setListener();
        serviceForGetAllName();

        if (Utility.checkConnectivity(DailyAndMonthlyRenewal.this)) {
            serviceForRenewal_SearchPolicy();
        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(DailyAndMonthlyRenewal.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {

                }
            });
        }
    }

    private void serviceForGetAllName() {
        final CustomProgressDialog dialog = new CustomProgressDialog(DailyAndMonthlyRenewal.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Renewal_SearchPolicy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optString("Error_Code").equals("0")) {
                        JSONArray array = object.getJSONArray("SearchPolicy");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            applicantList.add(jsonObject.optString("ApplicantName"));
                        }
                        /*setApplicantName();*/
                    } else {
                        PopupClass.showPopUpWithTitleMessageOneButton(DailyAndMonthlyRenewal.this, "OK", "", "", "No Policy Found", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {
                                finish();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ApplicantName", "");
                params.put("CollectorCode", userClass.getGlobalUserCode());
                if (txt_rnwl.getText().toString().equalsIgnoreCase("Daily Renewal")) {
                    params.put("PolicyType", "DD");
                } else {
                    params.put("PolicyType", "RD");
                }
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    /*private void setApplicantName() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, android.R.layout.simple_list_item_1, applicantList);
        et_loan_search.setAdapter(adapterMember);
    }*/

    private void serviceForRenewal_SearchPolicy() {

        final CustomProgressDialog dialog = new CustomProgressDialog(DailyAndMonthlyRenewal.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Renewal_SearchPolicy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    renewalDataClassList.clear();
                    if (object.optString("Error_Code").equals("0")) {
                        JSONArray array = object.getJSONArray("SearchPolicy");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            policyNo = jsonObject.optString("Policyno");
                            appName = jsonObject.optString("ApplicantName");
                            amount = jsonObject.optString("Amount");
                            plan = jsonObject.optString("Ptable");
                            deposit = jsonObject.optString("TotalDeposit");
                            getRenewalData(policyNo, appName, amount, plan, deposit);
                        }
                        li_header.setVisibility(View.VISIBLE);
                        list_renewal.setVisibility(View.VISIBLE);
                    } else {
                        PopupClass.showPopUpWithTitleMessageOneButton(DailyAndMonthlyRenewal.this, "OK", "", "", "No Policy Found", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ApplicantName", et_loan_search.getText().toString());
                params.put("CollectorCode", userClass.getGlobalUserCode());
                if (txt_rnwl.getText().toString().equalsIgnoreCase("Daily Renewal")) {
                    params.put("PolicyType", "DD");
                } else {
                    params.put("PolicyType", "RD");
                }
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setListener() {
        rnwl_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

       /* btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if (Utility.checkConnectivity(DailyAndMonthlyRenewal.this)) {
                    serviceForRenewal_SearchPolicy();
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(DailyAndMonthlyRenewal.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }*//*
            }
        });
*/
        et_loan_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (renewalDataClassList.size() > 0){
                    filter(s.toString());
            }
            }
        });
    }

    private void setDefaultValues() {
        renewal_title = getIntent().getExtras().getString("RN_TEXT");
        txt_rnwl.setText(renewal_title);
    }

    private void init() {
        li_header = findViewById(R.id.li_header);
        et_loan_search = findViewById(R.id.et_loan_search);
        rnwl_img = findViewById(R.id.rnwl_img);
        txt_rnwl = findViewById(R.id.txt_rnwl);
        btn_search = findViewById(R.id.btn_search);

        btn_search.setVisibility(View.GONE);

        list_renewal = findViewById(R.id.list_renewal);
        userClass = WCUserClass.getInstance();
    }

    private void getRenewalData(String policyNo,String appName,String amount,String plan,String deposit) {
        renewalDataClassList.add(new WCRenewalDataClass(policyNo, appName, amount, plan, deposit));
        setUpRecyclerAdapter();
    }

    private void setUpRecyclerAdapter() {
        adapter = new MyRecyclerRenewalAdapter(this, renewalDataClassList);
        list_renewal.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list_renewal.setLayoutManager(linearLayoutManager);
    }

    public void filter(String text) {
        List<WCRenewalDataClass> filterList = new ArrayList<>();
        for (WCRenewalDataClass renewalDataClass : renewalDataClassList) {
            if (renewalDataClass.getAppName().toLowerCase().contains(text)|renewalDataClass.getPolicyNo().toLowerCase().contains(text) ) {
                filterList.add(renewalDataClass);
            }
        }
        adapter.filteredList(filterList,text);
    }

}
