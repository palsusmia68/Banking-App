package com.cmb_collector.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.cmb_collector.R;
import com.cmb_collector.adapter.MyRecyclerEmiPaymentAdapter;
import com.cmb_collector.model.WCEMIPaymentData;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EMICollection extends AppBaseClass {

    private AutoCompleteTextView et_loan_search;
    LinearLayout li_header;
    private Button btn_show;
    private ImageView emi_payment_img_back;
    private List<WCEMIPaymentData> emiPaymentDataList = new ArrayList<>();

    private MyRecyclerEmiPaymentAdapter adapter;
    private RecyclerView list_emi;

    private ArrayList<String> loanNameList = new ArrayList();

    private WCUserClass userClass;
    private String txtS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_emicollection);

        init();
        setListener();
        serviceForLoanNameList();
        serviceForSearchLoanForRepayment("");
    }

    private void serviceForLoanNameList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(EMICollection.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoanNameList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoanNameList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        loanNameList.add(jsonObject.optString("APPLICANTNAME"));
                    }
/*
                    setLoanName();
*/
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
                params.put("UserType", "COLLECTOR");
                params.put("CollectorCode", userClass.getGlobalUserCode());
                params.put("IsNonEmi", "0");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);

    }

   /*
    private void setLoanName() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, android.R.layout.simple_list_item_1, loanNameList);
        et_loan_search.setAdapter(adapterMember);
    }
    */

    private void serviceForSearchLoanForRepayment(String name) {
        final CustomProgressDialog dialog = new CustomProgressDialog(EMICollection.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_SearchLoanForRepayment", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("SearchLoanForRepayment");
                    emiPaymentDataList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        getEMIPaymentData(jsonObject.optString("LOANID"), jsonObject.optString("APPLICANTNAME"), jsonObject.optString("LOANAMOUNT"), jsonObject.optString("EMI"), jsonObject.optString("TOTALPAID"));
                    }
                    li_header.setVisibility(View.VISIBLE);
                    list_emi.setVisibility(View.VISIBLE);
/*
                    setLoanName();
*/
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
               // params.put("ApplicantName", name);
                params.put("UserID",userClass.getGlobalUserCode());
                // params.put("ApplicantName","");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setListener() {
        emi_payment_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        /*btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(EMICollection.this)) {
                    if (!et_loan_search.getText().toString().equals("")) {
                        serviceForSearchLoanForRepayment(et_loan_search.getText().toString());
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(EMICollection.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });*/

        et_loan_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (emiPaymentDataList.size() > 0) {
                    filter(s.toString());
                }
            }
        });
    }

    private void init() {
        li_header = findViewById(R.id.li_header);
        userClass = WCUserClass.getInstance();
        et_loan_search = findViewById(R.id.et_loan_search);
        btn_show = findViewById(R.id.btn_show);
        emi_payment_img_back = findViewById(R.id.emi_payment_img_back);
        list_emi = findViewById(R.id.list_loan);

        btn_show.setVisibility(View.GONE);
    }

    private void getEMIPaymentData(String loanId, String appName, String loanAmount, String emi, String totalPaid) {
        emiPaymentDataList.add(new WCEMIPaymentData(loanId, appName, loanAmount, emi, totalPaid));
        setUpRecyclerAdapter();
    }

    private void setUpRecyclerAdapter() {
        adapter = new MyRecyclerEmiPaymentAdapter(this, emiPaymentDataList);
        list_emi.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list_emi.setLayoutManager(linearLayoutManager);
    }

    public void filter(String text) {
        List<WCEMIPaymentData> filterList = new ArrayList<>();
        for (WCEMIPaymentData renewalDataClass : emiPaymentDataList) {
            if (renewalDataClass.getAppName().toLowerCase().contains(text) | renewalDataClass.getLoanId().toLowerCase().contains(text)) {
                filterList.add(renewalDataClass);
            }
        }
        adapter.filteredList(filterList, text);
    }
}
