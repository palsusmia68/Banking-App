package com.cmb_collector.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.cmb_collector.adapter.MyListEmiBreakUpAdapter;
import com.cmb_collector.model.WCEMIBreakClass;
import com.cmb_collector.model.WCLoanModeClass;
import com.cmb_collector.model.WCSchemeClass;
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

public class LoanCalculator extends AppBaseClass {

    private ImageView loan_calculator_back_img;

    private Spinner spin_scheme;
    private Spinner spin_term;

    private TextView tv_roi;
    private TextView tv_interest_type;
    private TextView tv_loan_mode;
    private TextView tv_emi_amount;

    private EditText et_loan_amount;

    private Button btn_amount;
    private Button btn_calculate;
    private Button btn_show_breakup;

    private ListView listBreak;

    private CustomProgressDialog progressDialog;

    private Dialog dialog;

    private WCSchemeClass schemeClass;
    private WCLoanModeClass loanModeClass;
    private WCEMIBreakClass emiBreakClass;

    private MyListEmiBreakUpAdapter listEmiBreakUpAdapter;

    private ArrayList<WCSchemeClass> schemeList = new ArrayList<>();
    private ArrayList<WCLoanModeClass> loanModeList = new ArrayList<>();
    private ArrayList<WCEMIBreakClass> emiBreakList = new ArrayList<>();

    private List<String> schemeName = new ArrayList<>();
    private List<String> schemeID = new ArrayList<>();
    private List<String> termList = new ArrayList<>();


    private String EMI = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_loan_calculator);

        init();
        serviceForGetScheme();
    }


    private void init() {

        progressDialog = new CustomProgressDialog(LoanCalculator.this);

        //
        loan_calculator_back_img = findViewById(R.id.loan_calculator_back_img);

        spin_scheme = findViewById(R.id.spin_scheme);
        spin_term = findViewById(R.id.spin_term);

        tv_roi = findViewById(R.id.tv_roi);
        tv_interest_type = findViewById(R.id.tv_interest_type);
        tv_loan_mode = findViewById(R.id.tv_loan_mode);
        tv_emi_amount = findViewById(R.id.tv_emi_amount);

        et_loan_amount = findViewById(R.id.et_loan_amount);

        btn_amount = findViewById(R.id.btn_amount);
        btn_calculate = findViewById(R.id.btn_calculate);
        btn_show_breakup = findViewById(R.id.btn_show_breakup);


        //
        loan_calculator_back_img.setOnClickListener(backListener);

        btn_amount.setOnClickListener(ButtonAmountAction);

        btn_show_breakup.setOnClickListener(ButtonBreakUpAction);

    }

    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    View.OnClickListener ButtonAmountAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String loan_amount = et_loan_amount.getText().toString();

            if (loan_amount.isEmpty()) {
                et_loan_amount.setError("Enter Loan Amount");
            } else {
                serviceForAmountChecking(loan_amount);
            }

        }
    };

    View.OnClickListener ButtonBreakUpAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emiBreakUp();
        }
    };


    private void emiBreakUp() {

        dialog = new Dialog(LoanCalculator.this);
        dialog.setContentView(R.layout.dialog_emi_break);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        listBreak = dialog.findViewById(R.id.listBreak);
        Button btn_emi_break_ok = dialog.findViewById(R.id.btn_emi_break_ok);

        btn_emi_break_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        serviceForEMIBreakUp();


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wip = window.getAttributes();
        wip.gravity = Gravity.CENTER;
        wip.width = WindowManager.LayoutParams.MATCH_PARENT;
        wip.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wip);
        dialog.show();
    }


    private void serviceForGetScheme() {
        if (Utility.checkConnectivity(this)) {
            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceConnector.base_URL + "GET_Loan_LoadLoanScheme",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("LoanScheme");

                                schemeList.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    schemeClass = new WCSchemeClass();
                                    schemeClass.setSchemeName(object.optString("SchemeName"));
                                    schemeClass.setSchemeID(object.optString("SchemeId"));

                                    schemeList.add(schemeClass);
                                }

                                schemeName.clear();
                                for (int i = 0; i < schemeList.size(); i++) {
                                    schemeName.add(schemeList.get(i).getSchemeName());
                                }

                                schemeID.clear();
                                for (int i = 0; i < schemeList.size(); i++) {
                                    schemeID.add(schemeList.get(i).getSchemeID());
                                }

                                ArrayAdapter adapterMember = new ArrayAdapter(LoanCalculator.this, R.layout.spinner_item, schemeName);
                                spin_scheme.setAdapter(adapterMember);

                                spin_scheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        String scheme_id = schemeID.get(position);

                                        et_loan_amount.setText("");
                                        tv_emi_amount.setText("");

                                        serviceForGetTerm(scheme_id);
                                        serviceForGetSchemeDetails(scheme_id);

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

                    PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", error.getMessage(), "", new PopupCallBackOneButton() {
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
            PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }


    }

    private void serviceForGetTerm(final String SID) {
        if (Utility.checkConnectivity(this)) {

            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanTerm",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("LoanTerm");

                                termList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    termList.add(jsonArray.optString(i));
                                }

                                ArrayAdapter adapterMember = new ArrayAdapter(LoanCalculator.this, R.layout.spinner_item, termList);
                                spin_term.setAdapter(adapterMember);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismissDialog();

                    PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", error.getMessage(), "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("SchemeId", SID);

                    System.out.println("schemeid..." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }


    }

    private void serviceForGetSchemeDetails(final String SID) {
        if (Utility.checkConnectivity(this)) {
            // progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanSchemeDetails",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("LoanSchemeDetails");

                                loanModeList.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    loanModeClass = new WCLoanModeClass();
                                    loanModeClass.setLoanMode(object.optString("LoanMode"));
                                    loanModeClass.setLoanROI(object.optString("LoanRoi"));
                                    loanModeClass.setInterestMode(object.optString("InterestMode"));

                                    loanModeList.add(loanModeClass);
                                }

                                tv_roi.setText(loanModeList.get(0).getLoanROI());
                                tv_interest_type.setText(loanModeList.get(0).getInterestMode());
                                tv_loan_mode.setText(loanModeList.get(0).getLoanMode());


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // progressDialog.dismissDialog();

                    PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", error.getMessage(), "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("SchemeId", SID);

                    System.out.println("schemeDetails..." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });

        }


    }

    private void serviceForAmountChecking(final String amount) {
        if (Utility.checkConnectivity(this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanAmountChecking",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("LoadLoanAmountChecking");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String amountStatus = object.optString("AmountStatus");

                                    if (amountStatus.equalsIgnoreCase("FALSE")) {
                                        et_loan_amount.setError("Enter valid Loan Scheme Amount");
                                    } else {
                                        serviceForLoanCalculateEMI(amount);
                                        btn_show_breakup.setEnabled(true);


                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", error.getMessage(), "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("SchemeId", schemeID.get(spin_scheme.getSelectedItemPosition()));
                    params.put("Amount", amount);

                    System.out.println("amountChecking...." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }


    }

    private void serviceForLoanCalculateEMI(final String amount) {
        if (Utility.checkConnectivity(this)) {

            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_CalculateEmi",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("Emi");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    EMI = object.optString("EMI");

                                }

                                tv_emi_amount.setText(EMI);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismissDialog();

                    PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Server Not Response", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("SchemeId", schemeID.get(spin_scheme.getSelectedItemPosition()));
                    params.put("Term", termList.get(spin_term.getSelectedItemPosition()));
                    params.put("Mode", tv_loan_mode.getText().toString());
                    params.put("Roi", tv_roi.getText().toString());
                    params.put("InterestMode", tv_interest_type.getText().toString());
                    params.put("Amount", amount);

                    System.out.println("loanCalculate...." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        } else {
            PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }


    }


    private void serviceForEMIBreakUp() {
        if (Utility.checkConnectivity(this)) {
            progressDialog.showLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoanEmiBreakUp",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("EmiBreakUp");

                                emiBreakList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    emiBreakClass = new WCEMIBreakClass();
                                    emiBreakClass.setLoanID(object.optString("LOANID"));
                                    emiBreakClass.setEMINo(object.optString("EMINO"));
                                    emiBreakClass.setEmi(object.optString("EMI"));
                                    emiBreakClass.setInterest(object.optString("INTEREST"));
                                    emiBreakClass.setPrinciple(object.optString("PRINCIPLE"));
                                    emiBreakClass.setCurrent_balance(object.optString("CURRENT_BALANCE"));
                                    emiBreakClass.setDueDate(object.optString("DueDate"));

                                    emiBreakList.add(emiBreakClass);

                                }

                                setUpEMIBreakAdapter(emiBreakList);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismissDialog();

                    PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Server Not Response", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                        }
                    });
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("LoanAmount", et_loan_amount.getText().toString());
                    params.put("TERM", termList.get(spin_term.getSelectedItemPosition()));
                    params.put("ROI", tv_roi.getText().toString());
                    params.put("MODE", tv_loan_mode.getText().toString());
                    params.put("EMI", tv_emi_amount.getText().toString());
                    params.put("InterestType", tv_interest_type.getText().toString());

                    System.out.println("emiBreakUp...." + params);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        } else {

            PopupClass.showPopUpWithTitleMessageOneButton(LoanCalculator.this, "Ok", "", "Check Your Internet Connection", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        }


    }

    private void setUpEMIBreakAdapter(ArrayList<WCEMIBreakClass> emiBreakList) {
        listEmiBreakUpAdapter = new MyListEmiBreakUpAdapter(this, emiBreakList);
        listBreak.setAdapter(listEmiBreakUpAdapter);

    }

}
