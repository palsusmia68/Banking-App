package com.cmb_collector.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class InvestmentCalculator extends AppBaseClass {


    //private String[] deposit_type = {"RD","FD","MIS","DD"};
    private String[] compound_mode = {"Daily", "Monthly", "Quarterly", "Yearly"};

    private ImageView back;

    private Spinner spin_pcode;
    private Spinner spin_ptable;
    private Spinner spin_mode;

    private TextView tv_term;
    private TextView tv_roi;
    private TextView tv_maturity;
    private TextView tv_deposit;

    private EditText et_pamount;

    private Button btn_calculate;

    private CustomProgressDialog progressDialog;

    private List<String> plancode = new ArrayList<>();
    private List<String> plantable = new ArrayList<>();
    private List<String> modeDetails = new ArrayList<>();

    private String mode, term, roi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_investment_calculator);

        init();

    }

    private void init() {
        progressDialog = new CustomProgressDialog(this);

        back = findViewById(R.id.back);

        spin_pcode = findViewById(R.id.spin_pcode);
        spin_ptable = findViewById(R.id.spin_ptable);
        spin_mode = findViewById(R.id.spin_mode);

        tv_term = findViewById(R.id.tv_term);
        tv_roi = findViewById(R.id.tv_roi);
        tv_maturity = findViewById(R.id.tv_maturity);
        tv_deposit = findViewById(R.id.tv_deposit);

        et_pamount = findViewById(R.id.et_pamount);

        btn_calculate = findViewById(R.id.btn_calculate);

        //
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        serviceForGetLoadPlanCode();

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkingAmount();
            }
        });
    }

    private void checkingAmount() {
        String amount = et_pamount.getText().toString().trim();

        if (amount.isEmpty()) {
            et_pamount.setError("Enter Amount");
        } else {

            serviceForGetAmountDetails(amount);
        }
    }


    private void serviceForGetLoadPlanCode() {

        if (Utility.checkConnectivity(this)) {

            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceConnector.base_URL + "GET_Policy_LoadPlanCodeALL",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("LoadPlanCodeALL");
                                plancode.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    plancode.add(jsonArray.optString(i));
                                }
                                ArrayAdapter type = new ArrayAdapter(InvestmentCalculator.this, R.layout.spinner_item, plancode);
                                spin_pcode.setAdapter(type);

                                spin_pcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String PlanCode = plancode.get(position);
                                        serviceForGetLoadPlanTable(PlanCode);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Server Not Response..", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }

    }

    private void serviceForGetLoadPlanTable(final String plancode) {

        if (Utility.checkConnectivity(this)) {

            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPTable",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("PTable");

                                plantable.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    plantable.add(jsonArray.optString(i));
                                }

                                ArrayAdapter table = new ArrayAdapter(InvestmentCalculator.this, R.layout.spinner_item, plantable);
                                spin_ptable.setAdapter(table);


                                spin_ptable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String PlanTable = plantable.get(position);
                                        serviceForGetPlanDetails(PlanTable);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismissDialog();

                    PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Server Not Response..", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("PlanCode", plancode);

                    System.out.println("planTable..." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {

            PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });

        }

    }

    private void serviceForGetPlanDetails(final String tableCode) {
        if (Utility.checkConnectivity(this)) {
            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPolicyMode",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("ModeDetails");

                                modeDetails.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    //mode = object.optString("Mode");
                                    modeDetails.add(object.optString("Mode"));

                                    term = object.optString("Term");
                                    roi = object.optString("ROI");
                                }
                                ArrayAdapter mode = new ArrayAdapter(InvestmentCalculator.this, R.layout.spinner_item, modeDetails);
                                spin_mode.setAdapter(mode);

                                tv_term.setText(term);
                                tv_roi.setText(roi);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismissDialog();

                    PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Server Not Response..", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("PTable", tableCode);

                    System.out.println("planDetail..." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }


    }


    private void serviceForGetAmountDetails(final String amount) {
        if (Utility.checkConnectivity(this)) {
            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadAmountDetails",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("AmountDetails");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if (object.optString("ErrorCode").equals("0")) {
                                        tv_deposit.setText(object.optString("DepositAmount"));
                                        tv_maturity.setText(object.optString("MaturityAmount"));
                                    } else {
                                        et_pamount.setError(object.optString("ErrorMsg"));
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismissDialog();

                    PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Server Not Response..", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("PTable", spin_ptable.getSelectedItem().toString());
                    params.put("Amount", amount);
                    params.put("Mode", spin_mode.getSelectedItem().toString());

                    System.out.println("AmountDetails..." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(InvestmentCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }


    }


}
