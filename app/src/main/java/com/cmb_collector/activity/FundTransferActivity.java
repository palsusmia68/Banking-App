package com.cmb_collector.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cmb_collector.adapter.MyAcTransAdapter;
import com.cmb_collector.model.WCAccountTransactionClass;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class FundTransferActivity extends AppBaseClass {

    private TextView tv_branch_code;
    private TextView tv_branch_name;
    private TextView tv_ac_no;
    private TextView txt_ac_name;
    private TextView txt_bal;
    private TextView edt_date;

    private EditText edt_payee_name;
    private EditText edt_amount;
    private EditText edt_remarks;


    private static final int REQUEST_IMAGE_FINGERPRINT = 102;

    private ImageView img_fund;
    private Spinner spin_acc_name;

    private RadioGroup radio_tran_type;
    private RadioButton radio_button;

    private String tran_type;
    private AutoCompleteTextView et_acc_name;
    private ImageButton ib_search;
    private Button btn_fund;
    private Dialog dialog;
    private Button d_yes, d_no;
    private ImageView d_cancel;

    private Bitmap fingerprintBitmapData;
    private String stringFinger = "";


    private int mYear, mMonth, mDay, mHour, mMinute;

    private RecyclerView recycler_statement;
    private ArrayList<String> accountNameList = new ArrayList();
    private List<WCAccountTransactionClass> transactionClassList = new ArrayList<>();
    private MyAcTransAdapter myAcTransAdapter;

    private WCUserClass userClass;
    private TextView et_cash_balance;

    Calendar c = Calendar.getInstance();
    Calendar minCal = Calendar.getInstance();

   /* private Button btn_fingerprint;
    private CircleImageView img_fingerprint;
    private LinearLayout llout_finger;*/
   private Spinner spin_sbAcNo;
    private  String getsbAccount;
    private ArrayList<String> SBAccountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_fund_transfer);

        init();

        setDefaultValues();

        setListener();

        serviceForAccountNameList();
       // serviceforbalance();
        serviceForSbAccountList();
//        Utility.serviceforbalance(FundTransferActivity.this,userClass.getGlobalUserCode());
//        et_cash_balance.setText(Utility.Savings_Balance);
      //  serviceForCashBalanceByMember();

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
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(FundTransferActivity.this, R.layout.spinner_item, SBAccountList);
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
    private void serviceforbalance(String getsbAccount) {
        final CustomProgressDialog dialog = new CustomProgressDialog(FundTransferActivity.this);
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
                    et_cash_balance.setText(jsonObject.optString("SavngsBalance"));

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

    private void setListener() {
        img_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.checkConnectivity(FundTransferActivity.this)) {
                    serviceForACC_SplitDetails(et_acc_name.getText().toString());
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });

        /* btn_fingerprint.setOnClickListener(scanFingerListener);*/


//        spin_acc_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            String acc_no;
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                acc_no = parent.getItemAtPosition(position).toString();
//                if (Utility.checkConnectivity(FundTransferActivity.this)) {
//                    serviceForACC_SplitDetails(acc_no);
//                } else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        radio_tran_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_button = findViewById(checkedId);
                tran_type = radio_button.getText().toString();


            }
        });

//        edt_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                String dateTran = transactionClassList.get(transactionClassList.size() - 1).getDate();
//                Date date = null;
//                try {
//                    date = sdf.parse(dateTran);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                minCal = dateToCalender(date);
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(FundTransferActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                CharSequence strDate = null;
//                                Time chosenDate = new Time();
//                                chosenDate.set(dayOfMonth, monthOfYear, year);
//                                long dtDob = chosenDate.toMillis(true);
//                                strDate = DateFormat.format("yyyyMMdd", dtDob);
//                                edt_date.setText(strDate);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.setCanceledOnTouchOutside(false);
//                datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());
//                datePickerDialog.show();
//            }
//        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//
//                int mYear = calendar.get(Calendar.YEAR);
//                int mMonth = calendar.get(Calendar.MONTH);
//                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(FundTransferActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        CharSequence strDate = null;
//                        Time chosenDate = new Time();
//                        chosenDate.set(dayOfMonth, month, year);
//                        long dtDob = chosenDate.toMillis(true);
//                        strDate = DateFormat.format("yyyyMMdd", dtDob);
//                        edt_date.setText(strDate);
//                    }
//                }, mYear, mMonth, mDay);
//
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                String formattedDate = df.format(c);
                System.out.println("date "+formattedDate);
                edt_date.setText(formattedDate);
//                Calendar calendar = Calendar.getInstance();
//
//                int mYear = calendar.get(Calendar.YEAR);
//                int mMonth = calendar.get(Calendar.MONTH);
//                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(FundTransferActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        CharSequence strDate = null;
//                        Time chosenDate = new Time();
//                        chosenDate.set(dayOfMonth, month, year);
//                        long dtDob = chosenDate.toMillis(true);
//                        strDate = DateFormat.format("yyyyMMdd", dtDob);
//                        edt_date.setText(strDate);
//                    }
//                }, mYear, mMonth, mDay);
//
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
            }
        });
        btn_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(FundTransferActivity.this)) {
                    if (checkInput()) {
                        //todo--
                        if (tran_type.equalsIgnoreCase("Withdrawl")) {
                          //  serviceForACC_TransactionInsert();
//                            Intent intent = new Intent(FundTransferActivity.this, FingerPrintActivity.class);
//                            intent.putExtra("scanner_action", Utility.ScannerAction.Verify);
//                            startActivityForResult(intent, REQUEST_IMAGE_FINGERPRINT);
                        }else {
                            tran_type = "Withdrawl";
                          //  serviceForACC_TransactionInsert();
                            Random rnd = new Random();
                            int number = rnd.nextInt(9999);
                            Sp.getInstance(getApplicationContext()).storeOtpValue(number);
                          //  Toast.makeText(getApplicationContext(),userClass.getPhoneNumber(),Toast.LENGTH_LONG).show();
                            SendSms(number);
                        }
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "", "Please Check nyour Internet Connection", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });


        spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String payMode;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getsbAccount = SBAccountList.get(position);
                Sp.getInstance(getApplicationContext()).storeAccountName(getsbAccount);
                serviceforbalance(getsbAccount);
                //Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(FundTransferActivity.this);
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
                    serviceForACC_TransactionInsert();
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

    private Calendar dateToCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private boolean checkInput() {
      if (radio_tran_type.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Transaction Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_payee_name.getText().toString().equals("")) {
            edt_payee_name.setError("Please Enter Payee Name");
            return false;
        } else if (edt_amount.getText().toString().equals("")) {
            edt_amount.setError("Please Enter Amount ");
            return false;
        } else if (edt_date.getText().toString().equals("")) {
            edt_date.setError("Please select a date");
            return false;
        } else if (edt_remarks.getText().toString().equals("")) {
            edt_remarks.setError("Please Enter Remarks");
            return false;
        } /*else if (tran_type.equalsIgnoreCase("Withdrawl")) {
            if (Integer.parseInt(txt_bal.getText().toString()) < Integer.parseInt(edt_amount.getText().toString()) || txt_bal.getText().toString().contains("-")) {
                PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "Error!!!", "", "You haven't sufficient balance to Withdrawal", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
                return false;
            }
        }
        if (Double.parseDouble(et_cash_balance.getText().toString()) < Double.parseDouble(edt_amount.getText().toString())) {
            PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "Error!!!", "", "You havn't sufficient balance to Pay", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {

                }
            });
            return false;
        }*/
        return true;
    }

    private void serviceForACC_TransactionInsert() {
        //Toast.makeText(getApplicationContext(),"transaction",Toast.LENGTH_SHORT).show();
        final CustomProgressDialog dialog = new CustomProgressDialog(FundTransferActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_ACC_TransactionInsert", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                  //  Toast.makeText(getApplicationContext(),"transaction response"+response,Toast.LENGTH_SHORT).show();
                    System.out.println("Account Transaction "+response);
                    JSONArray array = object.getJSONArray("TransactionInsert");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String status = jsonObject.optString("Status");
                        if (jsonObject.optString("ErrorCode").equals("0")) {
                            PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "Success!!!", status, "Transaction Successful", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    Intent intent = new Intent(FundTransferActivity.this,AccountActivity.class);
                                    startActivity(intent);
                                  //  serviceForACC_SplitDetails(et_acc_name.getText().toString());
                                   // serviceForLoadStatement(tv_ac_no.getText().toString());

                                }
                            });
                        } else {
                            PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "Failed !!!", "Transaction Failed",status, new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
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
                params.put("Branch", userClass.getGlobalBCode());
                params.put("SBAccount", getsbAccount);
                params.put("AccountNo", tv_ac_no.getText().toString());
                params.put("PayeeName", edt_payee_name.getText().toString());
                params.put("TranType", tran_type);
                params.put("Amount", edt_amount.getText().toString());
                params.put("Tdate", edt_date.getText().toString());
                params.put("UserId", userClass.getGlobalUserCode());
                params.put("Remarks", edt_remarks.getText().toString());
                System.out.println("Account Transaction "+params.toString());
                return params;

            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForACC_SplitDetails(final String acc_no) {
        final CustomProgressDialog dialog = new CustomProgressDialog(FundTransferActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_SplitDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("AccountDetails");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        tv_ac_no.setText(jsonObject.optString("ACCOUNTNO"));
                        txt_ac_name.setText(jsonObject.optString("AccountHolderName"));
                        //txt_bal.setText(jsonObject.optString("BALANCE"));

                        serviceForLoadStatement(jsonObject.optString("ACCOUNTNO"));
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
                params.put("AccountNo", acc_no);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadStatement(final String AccNo) {
        final CustomProgressDialog dialog = new CustomProgressDialog(FundTransferActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_LoadStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_ACC_LoadStatement"+response);
                dialog.dismissDialog();
                try {
                    transactionClassList.clear();
                    JSONObject object = new JSONObject(response);
                    String Error_Code = object.getString("Error_Code");
                    if (Error_Code.equals("0")){
                        JSONArray array = object.getJSONArray("LoadStatement");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);

                            getAccountTrans(jsonObject.optString("INSTNO"), jsonObject.optString("TDATE"),
                                    jsonObject.optString("DEPOSIT"), jsonObject.optString("WITHDRAWAL"),
                                    jsonObject.optString("BALENCE"));
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
                params.put("AccountNo", AccNo);
                System.out.println("GET_ACC_LoadStatement"+params.toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForAccountNameList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(FundTransferActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_AccountNameList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("AccountList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        accountNameList.add(jsonObject.optString("MEMBERNAME"));
                    }

                    setAccountName();
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

    private void setAccountName() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accountNameList);
//        spin_acc_name.setAdapter(adapterMember);
        et_acc_name.setAdapter(adapterMember);
    }

    private void setDefaultValues() {
        tv_branch_code.setText(userClass.getGlobalBCode());
        tv_branch_name.setText(userClass.getGlobalBranchName());
    }

    private void init() {
        tv_branch_code = findViewById(R.id.tv_branch_code);
        tv_branch_name = findViewById(R.id.tv_branch_name);
        tv_ac_no = findViewById(R.id.tv_ac_no);
        txt_ac_name = findViewById(R.id.txt_ac_name);
      //  txt_bal = findViewById(R.id.txt_bal);
        et_acc_name = findViewById(R.id.et_acc_name);
        edt_payee_name = findViewById(R.id.edt_payee_name);
        edt_amount = findViewById(R.id.edt_amount);
        edt_remarks = findViewById(R.id.edt_remarks);

        img_fund = findViewById(R.id.img_fund);
        spin_acc_name = findViewById(R.id.spin_acc_name);

        radio_tran_type = findViewById(R.id.radio_tran_type);
        et_cash_balance = findViewById(R.id.et_cash_balance);

        ib_search = findViewById(R.id.ib_search);
        btn_fund = findViewById(R.id.btn_fund);
        edt_date = findViewById(R.id.edt_date);

        recycler_statement = findViewById(R.id.recycler_statement);
       /* btn_fingerprint = findViewById(R.id.btn_fingerprint);
        img_fingerprint = findViewById(R.id.img_fingerprint);
        llout_finger = findViewById(R.id.llout_finger);*/

        userClass = WCUserClass.getInstance();
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);
        tran_type = "Withdrawl";
    }

    private void getAccountTrans(String instno, String date, String deposit, String withdrawl, String blnc) {

        transactionClassList.add(new WCAccountTransactionClass(instno, date, deposit, withdrawl, blnc));

        setUpAcTrAdapter();
    }

    private void setUpAcTrAdapter() {
        myAcTransAdapter = new MyAcTransAdapter(this, transactionClassList);
        recycler_statement.setAdapter(myAcTransAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_statement.setLayoutManager(layoutManager);

    }

    private void popUpShow() {

        dialog = new Dialog(FundTransferActivity.this);
        dialog.setContentView(R.layout.item_dialog_confirm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d_yes = dialog.findViewById(R.id.btn_yes);
        d_no = dialog.findViewById(R.id.btn_no);
        d_cancel = dialog.findViewById(R.id.dialog_img_cancel);

        d_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FundTransferActivity.this, "Fund Transfer Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        d_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        d_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wip = window.getAttributes();
        wip.gravity = Gravity.CENTER;
        wip.width = WindowManager.LayoutParams.MATCH_PARENT;
        wip.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wip);
        dialog.show();

    }

    private void serviceForCashBalanceByMember() {

        final CustomProgressDialog dialog = new CustomProgressDialog(FundTransferActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailWalletBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    String Error_Code = object.optString("Error_Code");
                    if (Error_Code.equals("0")) {

                        JSONArray jsonArray = object.getJSONArray("AvailBalance");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            et_cash_balance.setText(jsonObject.optString("OpenBal"));
                        }

                    } else {

                        et_cash_balance.setText("0");
                    }

                } catch (JSONException e) {
                    PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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
                PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQUEST_IMAGE_FINGERPRINT) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data.getBooleanExtra("is_match", false)) {
//                    serviceForACC_TransactionInsert();
//                }else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "", "", "Error in Transaction", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                }
//            }else {
//                PopupClass.showPopUpWithTitleMessageOneButton(FundTransferActivity.this, "OK", "", "", "Error in Transaction", new PopupCallBackOneButton() {
//                    @Override
//                    public void onFirstButtonClick() {
//
//                    }
//                });
//            }
//        }
//    }

   /* View.OnClickListener scanFingerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };*/

    private Bitmap convertBytearrayToBitmap(byte[] fingerData) {
        return BitmapFactory.decodeByteArray(fingerData, 0, fingerData.length);
    }

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
