package com.cmb_collector.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cmb_collector.utility.Sp;
import com.goodiebag.pinview.Pinview;
import com.itextpdf.text.PageSize;
import com.cmb_collector.BuildConfig;
import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.model.WCRenewalDataClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.NotificationHelper;
import com.cmb_collector.utility.PDFExportClass;
import com.cmb_collector.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EmiPay extends AppBaseClass {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    final private int REQUEST_CODE_DOWNLOAD_PERMISSIONS = 222;
    private TextView branch_code;
    private TextView branch_name;
    private TextView et_member_code;
    private TextView tv_loanId;
    private TextView tv_emi_no;
    private TextView tv_amount;
    private TextView et_sb_balance;
    private TextView et_date;

    private EditText et_remarks;

    private Button btn_pay;

    private ImageView img_renewalPay;

    private Spinner spin_payMode;
    private Spinner spin_sbAcNo;
   // private Spinner spin_sbacno;

    private LinearLayout li_sbac,llout_cash_bal;
    private TextView et_cash_balance;
    private String loanId;
    private String[] payType = {"Saving A/C"};
    private  String getsbAccount;
    private ArrayList<String> SBAccountList = new ArrayList<>();
    private List<WCRenewalDataClass> renewalDataClassList = new ArrayList<>();
    private List<String> sbAccList = new ArrayList<>();
    private List<String> sbBalanceList = new ArrayList<>();

    private WCUserClass userClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_emi_pay);
        init();
        setDefaults();
        setListener();
        serviceForLoadLoanDetailsForRepaymentAdvance();
        serviceForSB_AccountBalanceByMember();
        serviceForSbAccountList();
      //  serviceforbalance();
    }

    private void serviceforbalance(String getsbAccount) {
        final CustomProgressDialog dialog = new CustomProgressDialog(EmiPay.this);
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
                params.put("collectorcode", getsbAccount);
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
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(EmiPay.this, R.layout.spinner_item, SBAccountList);
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

    private void serviceForLoadLoanDetailsForRepaymentAdvance() {
        final CustomProgressDialog dialog = new CustomProgressDialog(EmiPay.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoadLoanDetailsForRepaymentAdvance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoanDetailsForRepaymentAdvance");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        et_member_code.setText(jsonObject.optString("MemberId"));
                        tv_loanId.setText(jsonObject.optString("LOANID"));
//                        et_appName.setText(jsonObject.optString("ApplicantName"));
                        tv_amount.setText(jsonObject.optString("EmiAmount"));
                        tv_emi_no.setText(jsonObject.optString("EmiNo"));
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
                params.put("Loanid", loanId);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    private void serviceForSB_AccountBalanceByMember() {
        final CustomProgressDialog dialog = new CustomProgressDialog(EmiPay.this);
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
                            sbBalanceList.add(jsonObject.optString("Balance"));
                        }

                      //  setSbAcc();
                    }
                } catch (JSONException e) {
                    Log.d("XXXXXXXXexc", e.getMessage());
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
                params.put("MemberCode", et_member_code.getText().toString());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


//    private void setSbAcc() {
//        ArrayAdapter payment = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sbAccList);
//        payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin_sbacno.setAdapter(payment);
//    }

    private void serviceForInsertRepaymentAdvance() {
        final CustomProgressDialog dialog = new CustomProgressDialog(EmiPay.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Loan_InsertRepaymentAdvance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                System.out.println("EMIPay response "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("InsertRepaymentAdvance");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String PaidStatus = jsonObject.optString("PaidStatus");
                        if (jsonObject.optString("Errorcode").equals("0")) {
                            showMessageOKCancel("Payment Successful..Do you want to save as PDF?",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(EmiPay.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                                        showMessageOKCancel("You need to allow access to Storage",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                                                REQUEST_CODE_DOWNLOAD_PERMISSIONS);
                                                                    }
                                                                }, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                        return;
                                                    }

                                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                            REQUEST_CODE_DOWNLOAD_PERMISSIONS);
                                                } else {
                                                    PopupClass.showPopUpWithTitleMessageOneButton(EmiPay.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
                                                        @Override
                                                        public void onFirstButtonClick() {
                                                            finish();
                                                        }
                                                    });
                                                }
                                                return;
                                            } else {
                                                generatePDF();
                                            }
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        }else{
                            PopupClass.showPopUpWithTitleMessageOneButton(EmiPay.this, "OK", "Success!!", "Policy No:", jsonObject.optString("PaidStatus"), new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    finish();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    Log.d("XXXXXXXXexc", e.getMessage());
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
                params.put("Branch", branch_code.getText().toString());
                params.put("Loanid", tv_loanId.getText().toString());
                params.put("Paydate", et_date.getText().toString());
                params.put("Amount", tv_amount.getText().toString());
                params.put("Paymode", spin_payMode.getSelectedItem().toString());
                params.put("SBAccount", getsbAccount);
//                if (li_sbac.getVisibility() == View.VISIBLE) {
//                    params.put("SbAccountNo", spin_sbacno.getSelectedItem().toString());
//                } else {
//                    params.put("SbAccountNo", "");
//                }
                params.put("Remarks", et_remarks.getText().toString());
                params.put("UserID", userClass.getGlobalUserCode());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void generatePDF() {
        View renewalView = getLayoutInflater().inflate(R.layout.print_emi_pay, new LinearLayout(this), false);
        TextView tv_branch_code_p = renewalView.findViewById(R.id.tv_branch_code_p);
        TextView tv_branch_name_p = renewalView.findViewById(R.id.tv_branch_name_p);
        TextView tv_member_code_p = renewalView.findViewById(R.id.tv_member_code_p);
        TextView tv_Loanid_p = renewalView.findViewById(R.id.tv_Loanid_p);
        TextView tv_date_p = renewalView.findViewById(R.id.tv_date_p);
        TextView tv_emino_p = renewalView.findViewById(R.id.tv_emino_p);
        TextView tv_amount_p = renewalView.findViewById(R.id.tv_amount_p);
        TextView tv_pay_mode_p = renewalView.findViewById(R.id.tv_pay_mode_p);
        TextView tv_remarks_p = renewalView.findViewById(R.id.tv_remarks_p);
        LinearLayout li_sbac_p = renewalView.findViewById(R.id.li_sbac_p);
        TextView tv_acc_no_p = renewalView.findViewById(R.id.tv_acc_no_p);
        TextView tv_sb_balance_p = renewalView.findViewById(R.id.tv_sb_balance_p);

        tv_branch_code_p.setText(branch_code.getText().toString());
        tv_branch_name_p.setText(branch_name.getText().toString());
        tv_member_code_p.setText(et_member_code.getText().toString());
        tv_Loanid_p.setText(tv_loanId.getText().toString());
        tv_date_p.setText(Utility.changeDateFormat("yyyyMMdd", "dd/MM/yyyy", et_date.getText().toString()));
        tv_emino_p.setText(tv_emi_no.getText().toString());
        tv_amount_p.setText(tv_amount.getText().toString());
        tv_pay_mode_p.setText(spin_payMode.getSelectedItem().toString());
        tv_remarks_p.setText(et_remarks.getText().toString());
        if (li_sbac.getVisibility() == View.VISIBLE) {
            li_sbac_p.setVisibility(View.VISIBLE);
         //   tv_acc_no_p.setText(spin_sbacno.getSelectedItem().toString());
            tv_sb_balance_p.setText(et_sb_balance.getText().toString());
        } else {
            li_sbac_p.setVisibility(View.GONE);
        }

        String file_name = "emi_pay_" + tv_loanId.getText().toString() + "_" + et_date.getText().toString();

        File file = PDFExportClass.exportToPdfNormalView(renewalView, 1200, 1500, getString(R.string.company_name), file_name,
                PageSize.A4);

        // Set on UI Thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(EmiPay.this, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);

                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                notificationHelper.createNotification("EMI Payment Receipt", "File Download successfully", intent);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EmiPay.this);
                builder.setTitle("Success")
                        .setMessage("PDF File Generated Successfully.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startActivity(intent);
                            }

                        }).show();
            }
        });

    }

    private void setListener() {
        img_renewalPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(EmiPay.this)) {
                    if (checkInput()) {
                        Random rnd = new Random();
                        int number = rnd.nextInt(9999);
                        Sp.getInstance(getApplicationContext()).storeOtpValue(number);
                        SendSms(number);
                  //      serviceForInsertRepaymentAdvance();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(EmiPay.this, "OK", "", "Sorry!!Internet connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EmiPay.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        CharSequence strDate = null;
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, month, year);
                        long dtDob = chosenDate.toMillis(true);
                        strDate = DateFormat.format("yyyyMMdd", dtDob);
                        et_date.setText(strDate);
                    }
                }, mYear, mMonth, mDay);

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        spin_payMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Saving A/C")) {
                    li_sbac.setVisibility(View.VISIBLE);
                    llout_cash_bal.setVisibility(View.GONE);
                    Utility.serviceforbalance(EmiPay.this,userClass.getGlobalUserCode());
                    et_cash_balance.setText(Utility.Savings_Balance);
                   // serviceForSB_AccountBalanceByMember();
                } else {
                    li_sbac.setVisibility(View.GONE);
                    llout_cash_bal.setVisibility(View.VISIBLE);
                //    serviceForCashBalanceByMember();

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

//        spin_sbacno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                et_sb_balance.setText(sbBalanceList.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(EmiPay.this);
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

    public android.app.AlertDialog createDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_otpview, null);
        builder.setView(dialogView);
        final android.app.AlertDialog dialog = builder.create();
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
                    serviceForInsertRepaymentAdvance();
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

    private void serviceForCashBalanceByMember() {

        final CustomProgressDialog dialog = new CustomProgressDialog(EmiPay.this);
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
                    PopupClass.showPopUpWithTitleMessageOneButton(EmiPay.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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
                PopupClass.showPopUpWithTitleMessageOneButton(EmiPay.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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

    private boolean checkInput() {
        if (spin_payMode.getSelectedItem().toString().equalsIgnoreCase("Saving A/C")) {
//            li_sbac.setVisibility(View.VISIBLE);
         /*   if (Double.parseDouble(et_sb_balance.getText().toString()) < Double.parseDouble(tv_amount.getText().toString())) {
                PopupClass.showPopUpWithTitleMessageOneButton(EmiPay.this, "OK", "Error!!!", "", "You havn't sufficient balance to Pay", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
                return false;
            }*/
        }/*else if(spin_payMode.getSelectedItem().toString().equalsIgnoreCase("Wallet")){

            if (Double.parseDouble(et_cash_balance.getText().toString()) < Double.parseDouble(tv_amount.getText().toString())) {
                PopupClass.showPopUpWithTitleMessageOneButton(EmiPay.this, "OK", "Error!!!", "", "You havn't sufficient balance to Pay", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
                return false;
            }

        } */ else if (et_date.getText().toString().equals("")) {
            et_date.setError("Please Select a date");
            return false;
        } else if (et_remarks.getText().toString().equals("")) {
            et_remarks.setError("please Enter Remarks");
            return false;
        }
        return true;
    }

    private void setDefaults() {
        loanId = getIntent().getExtras().getString("LoanId");
        Log.d("XXXXXXXXid", loanId + "aaa");
        branch_code.setText(userClass.getGlobalBCode());
        branch_name.setText(userClass.getGlobalBranchName());

        ArrayAdapter payment = new ArrayAdapter(this, R.layout.spinner_item, payType);
        payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_payMode.setAdapter(payment);

    }

    private void init() {
        branch_code = findViewById(R.id.tv_branch_code);
        branch_name = findViewById(R.id.tv_branch_name);
        et_member_code = findViewById(R.id.et_member_code);
        tv_loanId = findViewById(R.id.tv_loanId);
        tv_amount = findViewById(R.id.tv_amount);
        tv_emi_no = findViewById(R.id.tv_emi_no);
        et_sb_balance = findViewById(R.id.et_sb_balance);
        et_date = findViewById(R.id.et_date);
        et_remarks = findViewById(R.id.et_remarks);
        btn_pay = findViewById(R.id.btn_pay);
        img_renewalPay = findViewById(R.id.img_renewalPay);

        spin_payMode = findViewById(R.id.spin_payMode);
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);
       // spin_sbacno = findViewById(R.id.spin_sbacno);

        li_sbac = findViewById(R.id.li_sbac);
        llout_cash_bal = findViewById(R.id.llout_cash_bal);
        et_cash_balance = findViewById(R.id.et_cash_balance);

        userClass = WCUserClass.getInstance();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_DOWNLOAD_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF();
            } else {
                Toast.makeText(this, "You have to grant permission..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
