package com.cmb_collector.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.database.DatabaseHelper;
import com.cmb_collector.model.LoadCollectorSBAccount;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.Sp;
import com.cmb_collector.utility.Utility;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewAccountActivity extends AppCompatActivity {

    private LinearLayout li_join;
    private CircleImageView profile_image, civ_sign;
    private ImageView new_acc_back_img;
    /*
        private Spinner spin_member;
    */
    private static Spinner acc_type;
    private static Spinner pay_mode;
    private TextView tv_branch_code;
    private TextView tv_branch_name;
    private static TextView tv_opening_date;
    private static EditText tv_member_code;
    private static EditText tv_member_name;
    private static EditText txt_father_name;
    private static EditText txt_add;
    private static EditText tv_phone;
    private static TextView tv_advisor_code;
    private TextView tv_advisor_name;
    private static EditText edit_pay_amount;
    private static EditText edit_joinapp_name;
    private static EditText edit_joinapp_fath_name;
    private static EditText edit_joinapp_add;
    private Button btn_submit;
    private byte[] pics;
    private Bitmap memPic;
    private String[] paymode = {"Saving A/C"};
    private String[] accountType = {"Single", "Joint"};
    private static ArrayList<String> memberList = new ArrayList();
    private static WCUserClass userDetailsData;
    private LinearLayout llout_cash_bal;
    private static TextView et_cash_balance;
    private String Status = "";
    private static AutoCompleteTextView tvAuto_member,tvAuto_member2;
    private static ImageButton ib_search;
    private static WCUserClass userClass;
    private static Spinner spin_sbAcNo;
    private static String getsbAccount;
    private static ArrayList<String> SBAccountList = new ArrayList<>();
    static Context context;
    static int OffLineflag = 0,OnLineflag=0;
    static ProgressDialog progressDialog;
    private BroadcastReceiver mNetworkReceiver2;
    private static DatabaseHelper db;
    private static LoadCollectorSBAccount loadCollectorSBAccount;
    private static List<LoadCollectorSBAccount> loadCollectorSBAccounts;
    private static List<String> accountnumber = new ArrayList<String>();
    private static List<String> accountbalance = new ArrayList<String>();
    private static String[] AccountNoArray;
    private BroadcastReceiver broadcastReceiver;
    public static final String DATA_SAVED_BROADCAST_INSERTNEWACCOUNT = "com.cmb_collector";
    public static final int INSERACCOUNT_SYNCED_WITH_SERVER = 1;
    public static final int INSERACCOUNT_NOT_SYNCED_WITH_SERVER = 0;
    private String GlobalBCode = "NA";
    private String DOE = "NA";
    private String MemberCode = "NA";
    private String ApplicantName = "NA";
    private String Father = "NA";
    private String Address = "NA";
    private String PhoneNo = "NA";
    private String AccountType = "NA";
    private String JointApplicantName = "NA";
    private String JointFather = "NA";
    private String JointAddress = "NA";
    private String AdvisorCode = "NA";
    private String Amount = "NA";
    private String PayMode = "NA";
    private String UserId = "NA";
    private String SbAccount = "NA";
    private String accType;
    private InsertnewaccountNetworkStateChecker insertnewaccountNetworkStateChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        NewAccountActivity.context=getApplicationContext();
        OffLineflag=0;
        OnLineflag=0;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
      //  progressDialog.show();
        init();
        setDefaultValues();
      //  serviceForMemberNameList();
        setListener();
      //  serviceForSbAccountList();
        mNetworkReceiver2 = new NetworkChange1();
        registerNetworkBroadcastForNougat();
        loadCollectorSBAccounts = new ArrayList<>();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST_INSERTNEWACCOUNT));
        insertnewaccountNetworkStateChecker = new InsertnewaccountNetworkStateChecker();
        registerReceiver(insertnewaccountNetworkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }
    public static void dialog(boolean value){
        if(value){
            ib_search.setVisibility(View.VISIBLE);
            tvAuto_member.setVisibility(View.VISIBLE);
            tvAuto_member2.setVisibility(View.GONE);
            OnLineflag++;
            tv_member_code.setEnabled(false);
            tv_member_code.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_member_name.setEnabled(false);
            tv_member_name.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            txt_father_name.setEnabled(false);
            txt_father_name.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            txt_add.setEnabled(false);
            txt_add.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_phone.setEnabled(false);
            tv_phone.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
         //   Toast.makeText(NewAccountActivity.context,"data on",Toast.LENGTH_LONG).show();
            System.out.println("status_Internet"+"we are back"+OnLineflag);
            if (OnLineflag==1){
                serviceForMemberNameList();
                serviceForSbAccountList();
                System.out.println("status_Internet"+"if block"+OnLineflag);
             //   customProgressDialog = new CustomProgressDialog(NewAgentJoining.context);
            }
//
//            Handler handler = new Handler();
//            Runnable delayrunnable = new Runnable() {
//                @Override
//                public void run() {
//                    //   tv_check_connection.setVisibility(View.GONE);
//                }
//            };
//            handler.postDelayed(delayrunnable, 3000);
        }else {
            ib_search.setVisibility(View.GONE);
            tvAuto_member.setVisibility(View.GONE);
            tvAuto_member2.setVisibility(View.VISIBLE);
            OffLineflag++;
        //    Toast.makeText(NewAccountActivity.context,"data off",Toast.LENGTH_LONG).show();

            System.out.println("status_Internet"+"could not connect to the internet "+OffLineflag);
            //  Toast.makeText(getApplicationContext(),"Net off",Toast.LENGTH_LONG).show();
            tv_member_code.setEnabled(true);
            tv_member_code.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_member_name.setEnabled(true);
            tv_member_name.setBackgroundResource(R.drawable.xml_et_bg_border);
            txt_father_name.setEnabled(true);
            txt_father_name.setBackgroundResource(R.drawable.xml_et_bg_border);
            txt_add.setEnabled(true);
            txt_add.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_phone.setEnabled(true);
            tv_phone.setBackgroundResource(R.drawable.xml_et_bg_border);
            if (OffLineflag==1){
                loadCollectorSBAccount();
                //    accountnumberbalance();
            }

            // habijabi();

        }
    }

    private static void loadCollectorSBAccount() {
        db = new DatabaseHelper(NewAccountActivity.context);
        Cursor cursor = db.getAccountnumber();
        if (cursor.moveToFirst()) {
            do {
                loadCollectorSBAccount = new LoadCollectorSBAccount(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_accountnumberlist)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_BALENCE))
                );
                String aa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_accountnumberlist));
                String aaa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_BALENCE));
                System.out.println("accountnumberlist" + " " + aa + " " + aaa);
                loadCollectorSBAccounts.add(loadCollectorSBAccount);
                accountnumber.add(loadCollectorSBAccount.getAccountno());
                accountbalance.add(loadCollectorSBAccount.getBalance());
            }
            while (cursor.moveToNext());
            //   accountnumberbalance();
            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(NewAccountActivity.context, R.layout.spinner_item, accountnumber);
            spin_sbAcNo.setAdapter(spinnerSBAmount);
            spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();
                    et_cash_balance.setText(accountbalance.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver2, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver2, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
//    protected void unregisterNetworkChanges() {
//        try {
//            unregisterReceiver(mNetworkReceiver2);
//            if(insertagentNetworkStateChecker!=null)
//                unregisterReceiver(insertagentNetworkStateChecker);
//            if (broadcastReceiver!=null)
//                unregisterReceiver(broadcastReceiver);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void onDestroy() {
        try{
            if(insertnewaccountNetworkStateChecker!=null)
                unregisterReceiver(insertnewaccountNetworkStateChecker);
            if (broadcastReceiver!=null)
                unregisterReceiver(broadcastReceiver);

        }catch(Exception e){}
        super.onDestroy();
      //  unregisterNetworkChanges();
    }
    private void setListener() {
        new_acc_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });


        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAuto_member.equals("")) {
                    tvAuto_member.setError("");
                } else {
                    if (Utility.checkConnectivity(NewAccountActivity.this)) {
                        serviceForMemberSplitDetails(tvAuto_member.getText().toString());
                    } else {
                        PopupClass.showPopUpWithTitleMessageOneButton(NewAccountActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                    }
                }
            }
        });


        acc_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Joint")) {
                    accType = "joint";
                    li_join.setVisibility(View.VISIBLE);
                } else {
                    accType = "single";
                    li_join.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pay_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Saving A/C")) {

                    llout_cash_bal.setVisibility(View.VISIBLE);
                //    serviceforbalance();
                 //   serviceForCashBalanceByMember();

                } else {

              //      llout_cash_bal.setVisibility(View.GONE);
//                    Utility.serviceforbalance(NewAccountActivity.this,userClass.getGlobalUserCode());
//                    et_cash_balance.setText(Utility.Savings_Balance);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(NewAccountActivity.this)) {
                    if (checkInput()) {
                        Random rnd = new Random();
                        int number = rnd.nextInt(9999);
                        Sp.getInstance(getApplicationContext()).storeOtpValue(number);

                  //      SendSms(number);
                     //   Toast.makeText(NewAccountActivity.this, userClass.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        serviceForInsertSavingsAccount();
                    }
                } else {
                    GlobalBCode = userDetailsData.getGlobalBCode();
                    DOE = tv_opening_date.getText().toString();
                    MemberCode = tv_member_code.getText().toString();
                    ApplicantName = tv_member_name.getText().toString();
                    Father = txt_father_name.getText().toString();
                    Address = txt_add.getText().toString();
                    PhoneNo = tv_phone.getText().toString();
                    AccountType = acc_type.getSelectedItem().toString();
                    if (accType.equals("joint")) {
                        JointApplicantName = edit_joinapp_name.getText().toString();
                        JointFather = edit_joinapp_fath_name.getText().toString();
                        JointAddress = edit_joinapp_add.getText().toString();
                    }
                    AdvisorCode = tv_advisor_code.getText().toString();
                    Amount = edit_pay_amount.getText().toString();
                    PayMode = pay_mode.getSelectedItem().toString();
                    UserId = userDetailsData.getGlobalUserCode();
                    SbAccount = getsbAccount;
                    db.NewAccountInsert(GlobalBCode,DOE,MemberCode,ApplicantName,Father,Address,PhoneNo,AccountType,
                            JointApplicantName,JointFather,JointAddress,AdvisorCode,Amount,PayMode,UserId,SbAccount,INSERACCOUNT_NOT_SYNCED_WITH_SERVER);
                    PopupClass.showPopUpWithTitleMessageOneButton(NewAccountActivity.this, "OK", "", "Please Check nyour Internet Connection", "", new PopupCallBackOneButton() {
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
               // Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAccountActivity.this);
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
                    serviceForInsertSavingsAccount();
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

    private void serviceforbalance(String getsbAccount) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAccountActivity.this);
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


    private void serviceForCashBalanceByMember() {

        final CustomProgressDialog dialog = new CustomProgressDialog(NewAccountActivity.this);
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
                    PopupClass.showPopUpWithTitleMessageOneButton(NewAccountActivity.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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
                PopupClass.showPopUpWithTitleMessageOneButton(NewAccountActivity.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", userDetailsData.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);

    }

    private boolean checkInput() {

       /* if (pay_mode.getSelectedItem().toString().equalsIgnoreCase("Wallet")) {

            if (Double.parseDouble(et_cash_balance.getText().toString()) < Double.parseDouble(edit_pay_amount.getText().toString())) {
                PopupClass.showPopUpWithTitleMessageOneButton(NewAccountActivity.this, "OK", "Error!!!", "", "You havn't sufficient balance to Pay", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
                return false;
            }
        }*/
        if (edit_pay_amount.getText().toString().equals("")) {
            edit_pay_amount.setError("Please enter an Amount");
            return false;
        } else if (acc_type.getSelectedItem().toString().equals("Joint")) {
            if (edit_joinapp_name.getText().toString().equals("")) {
                edit_joinapp_name.setError("Please Enter Joint Applicant Name");
                return false;
            } else if (edit_joinapp_fath_name.getText().toString().equals("")) {
                edit_joinapp_fath_name.setError("Please Enter Father Name");
                return false;
            } else if (edit_joinapp_add.getText().toString().equals("")) {
                edit_joinapp_add.setError("Please Enter Address");
                return false;

            }
            return false;
        }
        return true;
    }

    private void serviceForInsertSavingsAccount() {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAccountActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_ACC_InsertSavingsAccount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                System.out.println("PUT_ACC_InsertSavingsAccount "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("SavingsAccountReturn");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject.optString("Errorcode").equals("0")) {
                            Status = jsonObject.optString("Status");
                            PopupClass.showPopUpWithTitleMessageOneButton(NewAccountActivity.this, "OK", Status, "Your Account No.", jsonObject.optString("AccountNo"), new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    finish();
                                }
                            });
                        } else {
                            PopupClass.showPopUpWithTitleMessageOneButton(NewAccountActivity.this, "OK", "Status", "", jsonObject.optString("Status"), new PopupCallBackOneButton() {
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
                params.put("Branch", userDetailsData.getGlobalBCode());
                params.put("DateOfEnt", tv_opening_date.getText().toString());
                params.put("MemberCode", tv_member_code.getText().toString());
                params.put("ApplicantName", tv_member_name.getText().toString());
                params.put("Father", txt_father_name.getText().toString());
                params.put("Address", txt_add.getText().toString());
                params.put("PhoneNo", tv_phone.getText().toString());
                params.put("AccountType", acc_type.getSelectedItem().toString());
                params.put("JointApplicantName", edit_joinapp_name.getText().toString());
                params.put("JointFather", edit_joinapp_fath_name.getText().toString());
                params.put("JointAddress", edit_joinapp_add.getText().toString());
                params.put("AdvisorCode", tv_advisor_code.getText().toString());
                params.put("Amount", edit_pay_amount.getText().toString());
                params.put("PayMode", pay_mode.getSelectedItem().toString());
                params.put("UserId", userDetailsData.getGlobalUserCode());
                params.put("SBAccount", getsbAccount);
                System.out.println("PUT_ACC_InsertSavingsAccount "+params.toString());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForMemberSplitDetails(final String member) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAccountActivity.this);
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
                            tv_member_code.setText(jsonObject.optString("MemberCode"));
                            tv_member_name.setText(jsonObject.optString("MemberName"));
                            txt_father_name.setText(jsonObject.optString("Father"));
                            txt_add.setText(jsonObject.optString("Address"));
                            tv_phone.setText(jsonObject.optString("Phone"));
                            Glide.with(NewAccountActivity.this)
                                    .load(LoadPic(jsonObject.optString("ProfilePic")))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.profile_view))
                                    .into(profile_image);
                            Glide.with(NewAccountActivity.this)
                                    .load(LoadPic(jsonObject.optString("SignPic")))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.ic_sign))
                                    .into(civ_sign);
                        }
//                        serviceForLoadProfilePic(tv_member_code.getText().toString());
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

    private Bitmap LoadPic(String picPath) {
        pics = Base64.decode(picPath, Base64.DEFAULT);
        memPic = BitmapFactory.decodeByteArray(pics, 0, pics.length);
        return memPic;
    }

    private void serviceForLoadProfilePic(final String memberNo) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAccountActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "Get_FImage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ImageList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Log.d("XXXXXXXurl", jsonObject.optString("Link"));
                        if (jsonObject.optString("Head").equalsIgnoreCase("ProfilePic")) {
                            Glide.with(NewAccountActivity.this)
                                    .load(jsonObject.optString("Link"))
                                    .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .placeholder(R.drawable.profile_view)
                                            .skipMemoryCache(true)
                                    )
                                    .into(profile_image);
                            return;
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
                params.put("MemberCode", memberNo);
                Log.d("XXXXXXXparams", params.toString());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        str.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(str);
    }

    private static void serviceForMemberNameList() {
        progressDialog.show();
      //  final CustomProgressDialog dialog = new CustomProgressDialog(NewAccountActivity.context);
     //   dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Member_MemberNameList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
       //         dialog.dismissDialog();
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
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
          //      dialog.dismissDialog();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserType", "COLLECTOR");
                params.put("CollectorCode", userDetailsData.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(NewAccountActivity.context);
        requestQueue.add(str);
    }

    private static void setMember() {
        ArrayAdapter adapterMember = new ArrayAdapter(NewAccountActivity.context, R.layout.spinner_item, memberList);
        tvAuto_member.setAdapter(adapterMember);

    }

    private void setDefaultValues() {
        tv_branch_code.setText(userDetailsData.getGlobalBCode());
        tv_branch_name.setText(userDetailsData.getGlobalBranchName());
        tv_opening_date.setText(Utility.setCurrentDate());
        tv_advisor_code.setText(userDetailsData.getGlobalUserCode());
        tv_advisor_name.setText(userDetailsData.getGlobalUserName());

        setAccType();

        setPayMode();

    }

    private void setPayMode() {
        // Spinner Dropdown set with Adapter for join payment mode:
        ArrayAdapter payment = new ArrayAdapter(this, R.layout.spinner_item, paymode);
        pay_mode.setAdapter(payment);
    }

    private void setAccType() {
        // Spinner Dropdown set with Adapter for account type:
        ArrayAdapter ac_type = new ArrayAdapter(this, R.layout.spinner_item, accountType);
        acc_type.setAdapter(ac_type);
    }

    private void init() {
        profile_image = findViewById(R.id.profile_image);
        civ_sign = findViewById(R.id.civ_sign);
        new_acc_back_img = findViewById(R.id.new_acc_back_img);
        tv_branch_code = findViewById(R.id.tv_branch_code);
        tv_branch_name = findViewById(R.id.tv_branch_name);
        tv_opening_date = findViewById(R.id.tv_opening_date);
        tv_member_code = findViewById(R.id.tv_member_code);
        tv_member_name = findViewById(R.id.tv_member_name);
        txt_father_name = findViewById(R.id.edit_fath_name);
        txt_add = findViewById(R.id.edit_address);
        tv_phone = findViewById(R.id.edit_mb_no);
        tv_advisor_code = findViewById(R.id.tv_advisor_code);
        tv_advisor_name = findViewById(R.id.tv_advisor_name);

        edit_pay_amount = findViewById(R.id.edit_pay_amount);
        edit_joinapp_name = findViewById(R.id.edit_joinapp_name);
        edit_joinapp_fath_name = findViewById(R.id.edit_joinapp_fath_name);
        edit_joinapp_add = findViewById(R.id.edit_joinapp_add);


        btn_submit = findViewById(R.id.btn_submit);


        acc_type = findViewById(R.id.acc_type);

        li_join = findViewById(R.id.li_join);
        pay_mode = findViewById(R.id.pay_mode);

        llout_cash_bal = findViewById(R.id.llout_cash_bal);
        et_cash_balance = findViewById(R.id.et_cash_balance);

        ib_search = findViewById(R.id.ib_search);
        tvAuto_member = findViewById(R.id.tvAuto_member);
        tvAuto_member2 = findViewById(R.id.tvAuto_member2);


        userDetailsData = WCUserClass.getInstance();
        userClass = WCUserClass.getInstance();
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);
    }


    private static void serviceForSbAccountList() {
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
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(NewAccountActivity.context, R.layout.spinner_item, SBAccountList);
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

        RequestQueue requestQueue = Volley.newRequestQueue(NewAccountActivity.context);
        requestQueue.add(stringRequest);
    }
///////////////////////////////////////////////broadcast class///////////////////////

    public class NetworkChange1 extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {
                if (isOnline(context)) {
                    dialog(true);
                    Log.e("keshav", "Online Connect Intenet ");
                } else {
                    dialog(false);
                    Log.e("keshav", "Conectivity Failure !!! ");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    public static class InsertnewaccountNetworkStateChecker extends BroadcastReceiver {
        private Context context;
        private DatabaseHelper db;
        String fund;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;
            db = new DatabaseHelper(context);

     //       Toast.makeText(context, "broadcast call",Toast.LENGTH_SHORT).show();

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            //if there is a network
            if (activeNetwork != null) {
                //if connected to wifi or mobile data plan
            //    Toast.makeText(context, "network on",Toast.LENGTH_SHORT).show();
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                   // Toast.makeText(context, "activeNetwork.getType()",Toast.LENGTH_SHORT).show();
                    Cursor cursor = db.getUnsyncedInsertNA();
                    int i = 1;
                    if (cursor.moveToFirst()) {
                     //   Toast.makeText(context, "activeNetwork.getType()",Toast.LENGTH_SHORT).show();
                        do {
                            INSERTNEWACOOUNTSYNC(
                                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACID)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACBRANCH)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACDATEOFENT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACMEMBERCODE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACAPPLICANTNAME)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACFATHER)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACADDRESS)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACPHONENO)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACACCOUNTTYPE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACJOINTAPPLICANTNAME)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACJOINTFATHER)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACJOINTADDRESS)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACADVISORCODE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACAMOUNT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACPAYMODE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACUSERID)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACSBACCOUNT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NEWACSTATUS)));

                        //    Toast.makeText(context, "Network Loop INSERT Account "+i,Toast.LENGTH_SHORT).show();
                            i++;

                        } while (cursor.moveToNext());
                    }

                }

            }else{
             //   Toast.makeText(context, "network off",Toast.LENGTH_SHORT).show();
            }
        }

        private void INSERTNEWACOOUNTSYNC(final int newacid, final String newacbranch, final String newacdateofent, final String newacmembercode,
                                      final String newacapplicantname, final String newacfather, final String newacaddress, final String newacphoneno, final String newacaccounttype,
                                      final String newacjointapplicantname, final String newacjointfather, final String newacjointaddress, final String newacadvisorcode, final String newacamount, final String newacpaymode,
                                      final String newacuserid, final String newacsbaccount, final String newacstatus) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_ACC_InsertSavingsAccount",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("PUT_Member_Insert_NEW_ACCOUNT_syncresponse" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                db.updateNewAccountValue(newacid, NewAccountActivity.INSERACCOUNT_SYNCED_WITH_SERVER);
                                context.sendBroadcast(new Intent(NewAccountActivity.DATA_SAVED_BROADCAST_INSERTNEWACCOUNT));
                                JSONArray jsonArray = jsonObject.getJSONArray("SuccessMsg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String  error_code = object.optString("ErrorCode");
                                    String  memberCode = object.optString("AccountNo");
                                    String Status = object.optString("Status");
                                    System.out.println("SuccessMsg"+error_code+" "+memberCode+" "+Status);

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
                    Map<String, String> params = new HashMap<>();
                    params.put("Branch", newacbranch);
                    params.put("DateOfEnt", newacdateofent);
                    params.put("MemberCode", newacmembercode);
                    params.put("ApplicantName", newacapplicantname);
                    params.put("Father", newacfather);
                    params.put("Address", newacaddress);
                    params.put("PhoneNo", newacphoneno);
                    params.put("AccountType", newacaccounttype);
                    params.put("JointApplicantName", newacjointapplicantname);
                    params.put("JointFather", newacjointfather);
                    params.put("JointAddress", newacjointaddress);
                    params.put("AdvisorCode", newacadvisorcode);
                    params.put("Amount", newacamount);
                    params.put("PayMode", newacpaymode);
                    params.put("UserId", newacuserid);
                    params.put("SBAccount", newacsbaccount);
                    System.out.println("PUT_Member_Insert_NEW_ACCOUNT_syncresponse" + params.toString());
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }

    }
}
