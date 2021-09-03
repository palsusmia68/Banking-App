package com.cmb_collector.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cmb_collector.adapter.MyListEmiDataAdapter;
import com.cmb_collector.model.WCDueAndPaidData;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.Sp;
import com.cmb_collector.utility.Utility;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NonEMIPayment extends AppBaseClass {
    private ImageView advance_emi_back_img;

    private TextView tv_branch_code;
    private TextView tv_branch_name;
    private TextView tv_loanId;
    private TextView edit_applic_name;
    private TextView tv_emiNo;
    private TextView tv_due_principle;
    private TextView tv_due_interest;
    private TextView tv_due_date;
    private TextView tv_balance;
    //    private TextView paid;
    private TextView tv_payment_date;
    private TextView edit_principle_paid;
    private TextView edit_interest_paid;
    private TextView tv_emi_statement;
    private  TextView et_sb_balance;

    private EditText edit_pay_amount;
    private EditText edit_remarks;

    private Spinner spin_loan_name;
    private Spinner spin_pay_mode;
   // private Spinner spin_acc_no;
    private Spinner spin_sbAcNo;

    private Dialog dialog;
    private Button btn_calculate_amount;
    private Button btn_submit_non_emi;
    private Button d_ok;
    private ArrayList<WCDueAndPaidData> list = new ArrayList<>();
    private ArrayList<String> loanNameList = new ArrayList();
    private ArrayList<String> sbAccList = new ArrayList<>();
    private ArrayList<String> balanceList = new ArrayList<>();
    private  String getsbAccount;
    private ArrayList<String> SBAccountList = new ArrayList<>();


    private String[] pay_mode = {"Wallet", "Saving A/C"};
    private String memberId = "";
    private ListView due_list, paid_list;
    private LinearLayout li_sbAc,llout_cash_bal;
    private TextView et_cash_balance;;

    private static MyListEmiDataAdapter dueDataAdapter;
    private WCUserClass userClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_non_emipayment);

        init();
        setDefaultValues();
        setListener();
        serviceForLoanNameList();
        serviceForSbAccountList();
        //serviceforbalance();

    }

    private void serviceforbalance(String getsbAccount) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailSavingsBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("savings Balance NEW "+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("Balance");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    /// Savings_Balance= jsonObject.optString("SavngsBalance");
                    et_sb_balance.setText(jsonObject.optString("SavngsBalance"));

                } catch (JSONException e) {
//                    PopupClass.showPopUpWithTitleMessageOneButton(context, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
//                PopupClass.showPopUpWithTitleMessageOneButton(context, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                    @Override
//                    public void onFirstButtonClick() {
//                    }
//                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //  System.out.println("cllector code "+userClass.getGlobalUserCode());
                params.put("collectorcode",getsbAccount);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(str);

    }

    private void serviceForSbAccountList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Collector_LoadSBAccount",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("APP_SB_AccountNo" + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("LoadCollectorSBAccount");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String ac = String.valueOf(jsonArray.get(i));
                                System.out.println("Object " + object.optString("AccountNo"));
                                SBAccountList.add(object.optString("AccountNo"));
                            }
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(NonEMIPayment.this, R.layout.spinner_item, SBAccountList);
                            spin_sbAcNo.setAdapter(spinnerSBAmount);
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
                Map<String, String> params = new HashMap<>();
                params.put("UserID", userClass.getGlobalUserCode());
                // System.out.println("APP_SB_AccountNo" + params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void serviceForLoanNameList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
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
                    setLoanName();
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
                params.put("IsNonEmi", "1");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setLoanName() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, loanNameList);
        spin_loan_name.setAdapter(adapterMember);
    }

    private void serviceForSearchLoanForRepaymentNonEmi(final String loan_name) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_SearchLoanForRepaymentNonEmi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("SearchLoanForRepaymentNonEmi");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        tv_loanId.setText(jsonObject.optString("LoanId"));
                        edit_applic_name.setText(jsonObject.optString("ApplicantName"));
                        memberId = jsonObject.optString("MEMBERID");
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
                params.put("SearchLoan", loan_name);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForloadPriIntForRepaymentNonEmi(final String loandate) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_loadPriIntForRepaymentNonEmi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optString("Error_Code").equals("1")) {
                        setAllFieldEmpty();
                    } else {
                        JSONArray array = object.getJSONArray("loadPriIntForRepaymentNonEmi");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            tv_emiNo.setText(jsonObject.optString("EmiNo"));
                            tv_due_principle.setText(jsonObject.optString("DuePrincipal"));
                            tv_due_interest.setText(jsonObject.optString("DueInterest"));
                            tv_due_date.setText(jsonObject.optString("DueDate"));
                        }
                        setLoanName();
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
                params.put("LoanId", tv_loanId.getText().toString());
                params.put("LoanDate", loandate);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    private void servicForInsertLoanRepaymentNonEmi() {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Loan_InsertLoanRepaymentNonEmi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("InsertLoanRepaymentNonEmi");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject.optString("ErrorCode").equals("0")) {
                            PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "Success!!!", "", jsonObject.optString("PaidStatus"), new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    finish();
                                }
                            });
                        } else {
                            PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "Error!!!", "", jsonObject.optString("PaidStatus"), new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }
                    }
                    setLoanName();
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
                params.put("Branch", tv_branch_code.getText().toString());
                params.put("Loanid", tv_loanId.getText().toString());
                params.put("ApplicantName", edit_applic_name.getText().toString());
                params.put("PayDate", tv_payment_date.getText().toString());
                params.put("EmiNo", tv_emiNo.getText().toString());
                params.put("DueInterest", tv_due_interest.getText().toString());
                params.put("DueDate", tv_due_date.getText().toString());
                params.put("EmiAmount", edit_pay_amount.getText().toString());
                params.put("PayPrincipal", edit_principle_paid.getText().toString());
                params.put("PayInterest", edit_interest_paid.getText().toString());
                params.put("PayMode", spin_pay_mode.getSelectedItem().toString());
                params.put("SBAccount", getsbAccount);
//                if (li_sbAc.getVisibility() == View.GONE)
//                    params.put("SbAccountNo", "");
//                else
//                    params.put("SbAccountNo", spin_acc_no.getSelectedItem().toString());
                params.put("Remarks", edit_remarks.getText().toString());
                params.put("UserId", userClass.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadSBAccNo() {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_SB_AccountBalanceByMember", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.optString("Error_Code").equals("0")) {
                        JSONArray array = object.getJSONArray("AccountNo");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            sbAccList.add(jsonObject.optString("AccountNo"));
                            balanceList.add(jsonObject.optString("Balance"));
                        }
                    }
                  //  setSbAccNo();
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
                params.put("MemberCode", memberId);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

//    private void setSbAccNo() {
//        ArrayAdapter adapterMember = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sbAccList);
//        adapterMember.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin_acc_no.setAdapter(adapterMember);
//    }


    public void serviceForEmiStatement() {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoanEmiStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);

                    JSONArray array = object.getJSONArray("LoanStatement");
                    list.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        getEmiStatementData(jsonObject.optString("LoanId"), jsonObject.optString("PayNo"), jsonObject.optString("Paydate"), jsonObject.optString("PayAmount"), jsonObject.optString("Principal"), jsonObject.optString("Interest"));
                    }
                    //setSbAccNo();
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
                params.put("Loanid", tv_loanId.getText().toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    private void setListener() {
        advance_emi_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });


        spin_loan_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String loan_name;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Utility.checkConnectivity(NonEMIPayment.this)) {
                    loan_name = parent.getItemAtPosition(position).toString();
                    serviceForSearchLoanForRepaymentNonEmi(loan_name);
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_pay_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Saving A/C")) {
                    li_sbAc.setVisibility(View.VISIBLE);
                    llout_cash_bal.setVisibility(View.GONE);
                    serviceForLoadSBAccNo();
                } else {
                    li_sbAc.setVisibility(View.GONE);
                    llout_cash_bal.setVisibility(View.VISIBLE);
                    serviceForCashBalanceByMember();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String payMode;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getsbAccount = SBAccountList.get(position);
                Sp.getInstance(getApplicationContext()).storeAccountName(getsbAccount);
                serviceforbalance(getsbAccount);
                //  Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        spin_acc_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                tv_balance.setText(balanceList.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        tv_payment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NonEMIPayment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        CharSequence strDate = null;
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, month, year);
                        long dtDob = chosenDate.toMillis(true);
                        strDate = DateFormat.format("yyyyMMdd", dtDob);
                        tv_payment_date.setText(strDate);

                        if (!tv_payment_date.getText().toString().equals("")) {
                            serviceForloadPriIntForRepaymentNonEmi(tv_payment_date.getText().toString());
                        }
                    }
                }, mYear, mMonth, mDay);

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        btn_calculate_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int payable_principle;
                if (!edit_pay_amount.getText().toString().equals("") && !tv_payment_date.getText().toString().equals("") && !tv_due_interest.getText().toString().equals("")) {
                    if (Double.parseDouble(edit_pay_amount.getText().toString()) > Double.parseDouble(tv_due_interest.getText().toString())) {
                        payable_principle = Integer.parseInt(edit_pay_amount.getText().toString()) - Integer.parseInt(tv_due_interest.getText().toString());
                        edit_principle_paid.setText(payable_principle + "");
                        edit_interest_paid.setText(tv_due_interest.getText().toString());
                    } else {
                        edit_interest_paid.setText(edit_pay_amount.getText().toString());
                        edit_principle_paid.setText("0");
                    }
                } else {
                    if (tv_payment_date.getText().toString().equals("")) {
                        tv_payment_date.setError("Choose a payment date");
                    } else if (edit_pay_amount.getText().toString().equals("")) {
                        edit_pay_amount.setError("Please Enter amount");
                    }
                }
            }
        });

        btn_submit_non_emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(NonEMIPayment.this)) {
                    if (checkInput()) {
                        Random rnd = new Random();
                        int number = rnd.nextInt(9999);
                        Sp.getInstance(getApplicationContext()).storeOtpValue(number);
                        SendSms(number);
                        //servicForInsertLoanRepaymentNonEmi();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });


//        due.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                duePopUpShow();
//            }
//        });
        tv_emi_statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paidPopUpShow();
            }
        });
    }

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.sms_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("SMS response "+response);
                dialog.dismissDialog();


                createDialog();


                //  Toast.makeText(getApplicationContext(), "Message send Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Failed to Send Message", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", "cfcnidhi");
                map.put("password", "cfcnidhi@918");
                map.put("to", userClass.getPhoneNumber());
                map.put("senderid", "CFCLTD");
                map.put("text", String.valueOf(number));
                map.put("route", "Informative");
                map.put("type", "text");
                return map;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 0));
        Volley.newRequestQueue(getApplicationContext()).add(str);
    }

    public AlertDialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_otpview, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        Button button = dialogView.findViewById(R.id.No);
        Pinview pinview = dialogView.findViewById(R.id.pinview);
        TextView wrongOtp = dialogView.findViewById(R.id.wrongOtp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int  getpOtpNumber = Sp.getInstance(getApplicationContext()).getOtpValue();
                String SgetpOtpNumber = String.valueOf(getpOtpNumber);
                String user_input_password = pinview.getValue().toString().trim();
                if (SgetpOtpNumber.equals(user_input_password)) {
                    dialog.dismiss();
                    servicForInsertLoanRepaymentNonEmi();
                    // Toast.makeText(getApplicationContext(), "" + getpOtpNumber + "  " + user_input_password, Toast.LENGTH_LONG).show();
                }else{
                    wrongOtp.setVisibility(View.VISIBLE);

                    //  Toast.makeText(getApplicationContext(), "Wrong OTP"+userClass.getPhoneNumber(), Toast.LENGTH_LONG).show();

                }
            }
        });
        dialog.show();
        return dialog;
    }

    private boolean checkInput() {
        if (spin_pay_mode.getSelectedItem().toString().equalsIgnoreCase("Saving A/C")) {
//            li_sbac.setVisibility(View.VISIBLE);
            if (Double.parseDouble(tv_balance.getText().toString()) < Double.parseDouble(edit_pay_amount.getText().toString())) {
                PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "Error!!!", "", "You havn't sufficient balance to Pay", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
                return false;
            }
        }else if(spin_pay_mode.getSelectedItem().toString().equalsIgnoreCase("Wallet")){

            if (Double.parseDouble(et_cash_balance.getText().toString()) < Double.parseDouble(edit_pay_amount.getText().toString())) {
                PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "Error!!!", "", "You havn't sufficient balance to Pay", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
                return false;
            }

        }
        else if (edit_pay_amount.getText().toString().equals("")) {
            edit_pay_amount.setError("Please Enter amount");
            return false;
        } else if (edit_remarks.getText().toString().equals("")) {
            edit_remarks.setError("Please Enter Remarks");
            return false;
        } else if (edit_principle_paid.getText().toString().equals("")) {
            edit_principle_paid.setError("Please Click on Amount Button");
            return false;
        }
        return true;
    }

    private void serviceForCashBalanceByMember() {

        final CustomProgressDialog dialog = new CustomProgressDialog(NonEMIPayment.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailWalletBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    String Error_Code=object.optString("Error_Code");
                    if (Error_Code.equals("0")){

                        JSONArray jsonArray = object.getJSONArray("AvailBalance");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            et_cash_balance.setText(jsonObject.optString("OpenBal"));
                        }

                    }else {

                        et_cash_balance.setText("0");
                    }

                } catch (JSONException e) {
                    PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
                PopupClass.showPopUpWithTitleMessageOneButton(NonEMIPayment.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", userClass.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);

    }

    private void setAllFieldEmpty() {
        tv_emiNo.setText("");
        tv_due_principle.setText("");
        tv_due_interest.setText("");
        tv_due_date.setText("");
        edit_pay_amount.setText("");
        edit_principle_paid.setText("");
        edit_interest_paid.setText("");

    }

    private void setDefaultValues() {
        tv_branch_code.setText(userClass.getGlobalBCode());
        tv_branch_name.setText(userClass.getGlobalBranchName());

        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, pay_mode);
        spin_pay_mode.setAdapter(adapterMember);
    }

    private void init() {
        tv_branch_code = findViewById(R.id.tv_branch_code);
        tv_branch_name = findViewById(R.id.tv_branch_name);
        tv_loanId = findViewById(R.id.tv_loanId);
        et_sb_balance = findViewById(R.id.et_sb_balance);
        edit_applic_name = findViewById(R.id.edit_applic_name);
        tv_emiNo = findViewById(R.id.tv_emiNo);
        tv_due_principle = findViewById(R.id.tv_due_principle);
        spin_loan_name = findViewById(R.id.spin_loan_name);
        spin_pay_mode = findViewById(R.id.spin_pay_mode);
      //  spin_acc_no = findViewById(R.id.spin_acc_no);
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);
        tv_payment_date = findViewById(R.id.tv_payment_date);
        edit_principle_paid = findViewById(R.id.edit_principle_paid);
        edit_interest_paid = findViewById(R.id.edit_interest_paid);
        tv_due_interest = findViewById(R.id.tv_due_interest);
        tv_due_date = findViewById(R.id.tv_due_date);
        tv_balance = findViewById(R.id.tv_balance);
        edit_pay_amount = findViewById(R.id.edit_pay_amount);
        edit_remarks = findViewById(R.id.edit_remarks);
        li_sbAc = findViewById(R.id.li_sbAc);
        btn_calculate_amount = findViewById(R.id.btn_calculate_amount);
        btn_submit_non_emi = findViewById(R.id.btn_submit_non_emi);
        advance_emi_back_img = findViewById(R.id.advance_emi_back_img);
        tv_emi_statement = findViewById(R.id.tv_emi_statement);
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);
        llout_cash_bal = findViewById(R.id.llout_cash_bal);
        et_cash_balance = findViewById(R.id.et_cash_balance);

        userClass = WCUserClass.getInstance();
    }

    private void paidPopUpShow() {

        dialog = new Dialog(NonEMIPayment.this);
        dialog.setContentView(R.layout.dialog_emi_statement);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d_ok = dialog.findViewById(R.id.btn_ok);
        paid_list = dialog.findViewById(R.id.paid_list);

        d_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        serviceForEmiStatement();


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wip = window.getAttributes();
        wip.gravity = Gravity.CENTER;
        wip.width = WindowManager.LayoutParams.MATCH_PARENT;
        wip.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wip);
        dialog.show();
    }


    private void getEmiStatementData(String loanId, String emiNo, String date, String amount, String principle, String interest) {
        list.add(new WCDueAndPaidData(loanId, emiNo, date, amount, principle, interest));
        setEmiStatementAdapter();
    }

    private void setEmiStatementAdapter() {
        dueDataAdapter = new MyListEmiDataAdapter(NonEMIPayment.this, list);
        paid_list.setAdapter(dueDataAdapter);
    }
}
