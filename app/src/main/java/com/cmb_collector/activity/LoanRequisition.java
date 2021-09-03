package com.cmb_collector.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

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

public class LoanRequisition extends AppBaseClass {

    private ImageView loan_requisition_back_img;

    private TextView tv_branch_code;
    private TextView tv_branch_name;
    private TextView edit_member_code;
    private TextView tv_member_name;
    private TextView txt_father_name;
    private TextView txt_add;
    private TextView edt_date;
    private TextView edit_mode;
    private TextView edit_interest;
    private TextView tv_interest_type;
    private TextView edit_emi_amount;
    private TextView edit_collector_code;
    private TextView edit_collector_name;

    private EditText edt_loan_amount;
    private EditText edit_purpose;
    private Button btn_amount;
    private Button btn_calculate_emi;
    private Button btn_loan_requisition;

    private Spinner spin_member;
    private Spinner spin_loan_name;
    private Spinner spin_term;

    private ArrayList<String> memberList = new ArrayList();
    private ArrayList<String> loanSchemeList = new ArrayList();
    private ArrayList<String> loanSchemeIdList = new ArrayList();
    private ArrayList<String> loanTermList = new ArrayList();

    private String AmountStatus = "FALSE";

    private WCUserClass userClass;
    private int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_loan_requisition);

        init();
        setDefaultValues();
        setListener();
        serviceForMemberNameList();
        serviceForLoadLoanScheme();


    }

    //todo location permission starts

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    private void serviceForLoadLoanScheme() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanScheme", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    loanSchemeList.clear();
                    loanSchemeIdList.clear();
                    JSONArray array = object.getJSONArray("LoanScheme");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        loanSchemeList.add(jsonObject.optString("SchemeName"));
                        loanSchemeIdList.add(jsonObject.optString("SchemeId"));
                    }
                    setLoanScheme();
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
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setLoanScheme() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, loanSchemeList);
        spin_loan_name.setAdapter(adapterMember);

    }

    private void serviceForMemberNameList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Member_MemberNameList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        memberList.clear();
                        JSONArray array = object.getJSONArray("MemberList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            memberList.add(jsonObject.optString("MemberName"));
                        }
                        setMember();
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
                params.put("UserType", "COLLECTOR");
                params.put("CollectorCode", userClass.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setMember() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, memberList);
        spin_member.setAdapter(adapterMember);

    }

    private void serviceForMemberSplitDetails(final String member) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Member_SplitDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        JSONArray array = object.getJSONArray("MemberDetails");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            edit_member_code.setText(jsonObject.optString("MemberCode"));
                            tv_member_name.setText(jsonObject.optString("MemberName"));
                            txt_father_name.setText(jsonObject.optString("Father"));
                            txt_add.setText(jsonObject.optString("Address"));
                        }
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
                params.put("NameText", member);
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadLoanTerm(final String schemeId) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanTerm", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoanTerm");
                    loanTermList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        loanTermList.add(array.optString(i));
                        Log.d("XXXXXXXXXXtermlist", loanTermList.get(i));
                    }
                    setLoanTerm();
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
                params.put("SchemeId", schemeId);
                Log.d("XXXXXXid", schemeId);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setLoanTerm() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, loanTermList);
        spin_term.setAdapter(adapterMember);

    }

    private void serviceForLoadLoanSchemeDetails(final String schemeId) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanSchemeDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoanSchemeDetails");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        edit_mode.setText(jsonObject.optString("LoanMode"));
                        edit_interest.setText(jsonObject.optString("LoanRoi"));
                        tv_interest_type.setText(jsonObject.optString("InterestMode"));
                    }
                    setLoanTerm();
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
                params.put("SchemeId", schemeId);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadLoanAmountChecking() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanAmountChecking", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoadLoanAmountChecking");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject.optString("AmountStatus").equals("TRUE")) {
                            AmountStatus = "TRUE";
                        } else {
                            AmountStatus = "FALSE";
                        }
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
                params.put("SchemeId", loanSchemeIdList.get(spin_loan_name.getSelectedItemPosition()));
                params.put("Amount", edt_loan_amount.getText().toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForCalculateEmi() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_CalculateEmi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("Emi");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        edit_emi_amount.setText(jsonObject.optString("EMI"));
                    }
                    //setLoanTerm();
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
                params.put("SchemeId", loanSchemeIdList.get(spin_loan_name.getSelectedItemPosition()));
                params.put("Term", spin_term.getSelectedItem().toString());
                params.put("Mode", edit_mode.getText().toString());
                params.put("Roi", edit_interest.getText().toString());
                params.put("InterestMode", tv_interest_type.getText().toString());
                params.put("Amount", edt_loan_amount.getText().toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoanInsert() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Loan_LoanInsert", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoanInsert");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject.optString("ErrorCode").equals("0")) {
                            PopupClass.showPopUpWithTitleMessageOneButton(LoanRequisition.this, "OK", "Success!!!", "Your Loan Id", jsonObject.optString("LoanID"), new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        } else {
                            PopupClass.showPopUpWithTitleMessageOneButton(LoanRequisition.this, "OK", "Error!!!", "", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }
                    }
                    setLoanTerm();
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
                params.put("Branch", userClass.getGlobalBCode());
                params.put("MemberCode", edit_member_code.getText().toString());
                params.put("MemberName", tv_member_name.getText().toString());
                params.put("FatherName", txt_father_name.getText().toString());
                params.put("Address", txt_add.getText().toString());
                params.put("RegDate", edt_date.getText().toString());
                params.put("Scheme", loanSchemeIdList.get(spin_loan_name.getSelectedItemPosition()));
                params.put("Term", spin_term.getSelectedItem().toString());
                params.put("Mode", edit_mode.getText().toString());
                params.put("Roi", edit_interest.getText().toString());
                params.put("InterestMode", tv_interest_type.getText().toString());
                params.put("LoanAmount", edt_loan_amount.getText().toString());
                params.put("EmiAmount", edit_emi_amount.getText().toString());
                params.put("CollectorCode", userClass.getGlobalUserCode());
                params.put("Purpose", edit_purpose.getText().toString());
                params.put("UserId", userClass.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setListener() {
        loan_requisition_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(LoanRequisition.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        CharSequence strDate = null;
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, month, year);
                        long dtDob = chosenDate.toMillis(true);
                        strDate = DateFormat.format("yyyyMMdd", dtDob);
                        edt_date.setText(strDate);
                    }
                }, mYear, mMonth, mDay);

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        spin_member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String member;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                member = parent.getItemAtPosition(position).toString();
                if (Utility.checkConnectivity(LoanRequisition.this))
                    serviceForMemberSplitDetails(member);
                else {
                    PopupClass.showPopUpWithTitleMessageOneButton(LoanRequisition.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
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

        spin_loan_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Utility.checkConnectivity(LoanRequisition.this)) {
                    serviceForLoadLoanTerm(loanSchemeIdList.get(position));
                    serviceForLoadLoanSchemeDetails(loanSchemeIdList.get(position));
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(LoanRequisition.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
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

        btn_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceForLoadLoanAmountChecking();
            }
        });

        btn_calculate_emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(LoanRequisition.this)) {
                    if (checkInput()) {
                        serviceForCalculateEmi();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(LoanRequisition.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });

        btn_loan_requisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(LoanRequisition.this)) {
                    if (checkInput()) {
                        //serviceForLoanInsert();
                        Random rnd = new Random();
                        int number = rnd.nextInt(9999);
                        Sp.getInstance(getApplicationContext()).storeOtpValue(number);
                        SendSms(number);
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(LoanRequisition.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });
    }

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanRequisition.this);
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
                    serviceForLoanInsert();
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
        if (edt_loan_amount.getText().toString().equals("")) {
            edt_loan_amount.setError("Please Enter Amount");
            return false;
        } else if (edt_date.getText().toString().equals("")) {
            edt_date.setError("Please enter date");
            return false;
        }
        return true;
    }

    private void setDefaultValues() {
        tv_branch_code.setText(userClass.getGlobalBCode());
        tv_branch_name.setText(userClass.getGlobalBranchName());
        edit_collector_code.setText(userClass.getGlobalUserCode());
        edit_collector_name.setText(userClass.getGlobalUserName());

    }

    private void init() {
        loan_requisition_back_img = findViewById(R.id.loan_requisition_back_img);
        tv_branch_code = findViewById(R.id.tv_branch_code);
        tv_branch_name = findViewById(R.id.tv_branch_name);
        edit_member_code = findViewById(R.id.edit_member_code);
        tv_member_name = findViewById(R.id.tv_member_name);
        txt_father_name = findViewById(R.id.edit_fath_name);
        txt_add = findViewById(R.id.edit_address);
        edt_date = findViewById(R.id.edt_date);
        edit_mode = findViewById(R.id.edit_mode);
        edit_interest = findViewById(R.id.edit_interest);
        tv_interest_type = findViewById(R.id.tv_interest_type);
        edt_loan_amount = findViewById(R.id.edt_loan_amount);
        edit_emi_amount = findViewById(R.id.edit_emi_amount);
        edit_collector_code = findViewById(R.id.edit_collector_code);
        edit_collector_name = findViewById(R.id.edit_collector_name);
        edit_purpose = findViewById(R.id.edit_purpose);

        btn_amount = findViewById(R.id.btn_amount);
        btn_calculate_emi = findViewById(R.id.btn_calculate_emi);
        btn_loan_requisition = findViewById(R.id.btn_loan_requisition);

        spin_member = findViewById(R.id.spin_member);
        spin_loan_name = findViewById(R.id.spin_loan_name);
        spin_term = findViewById(R.id.spin_term);

        userClass = WCUserClass.getInstance();
    }

}
