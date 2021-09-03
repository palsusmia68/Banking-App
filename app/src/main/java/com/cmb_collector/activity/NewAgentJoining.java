package com.cmb_collector.activity;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.fingerprint.FingerprintManager;
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
import com.cmb_collector.model.WCMemberDetailsClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.model.newagentsetget;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.NetworkChange;
import com.cmb_collector.utility.Sp;
import com.cmb_collector.utility.Utility;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewAgentJoining extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_STORAGE_CODE = 1;
    private static CustomProgressDialog customProgressDialog;
    private KeyStore keyStore;
    private static final String KEY_NAME = "WCreditCooperative";
    private Cipher cipher;
    public KeyguardManager keyguardManager;
    public FingerprintManager fingerprintManager;
    private LinearLayout li_sb_accNo,llout_cash_bal;
    private static TextView et_cash_balance;
    private LinearLayout li_balance;

    private CircleImageView profile_image, civ_sign;
    private ImageView back, logout;

    private TextView tv_bCode;
    private TextView tv_bName;
    private static EditText tv_memberNo;
    private static EditText tv_memberName;
    private static EditText tv_fath_name;
    private static EditText tv_address;
    private static EditText tv_phone;
    private TextView tv_doj;
    private TextView edit_introducer_code;
    private TextView edit_introducer_name;
    private TextView tv_sb_accNo;
    private TextView tv_balance;
    private String GlobalBCode = "NA";
    private String memberNo  = "NA";
    private String memberName = "NA";
    private String fathname = "NA";
    private String address = "NA";
    private String doj = "NA";
    private String GlobalUserCode = "NA";
    private String regamount = "NA";
    private String paytype = "NA";
    private String getGlobalUserCode = "NA";
    private Button btn_save;
    private Spinner pay_type;
    private static Spinner rank_type;
    private Spinner spin_member;
    private static Spinner reg_amount;

    private String[] payType = {"Saving A/C"};

    private static ArrayList<String> memberList = new ArrayList();
    private static ArrayList<String> rankList = new ArrayList<>();
    private static ArrayList<String> rankStagesList = new ArrayList<>();
    private static ArrayList<String> regAmountList = new ArrayList<>();

    private byte[] pics;
    private Bitmap memPic;

    private static WCUserClass userDetailsData;
    private WCMemberDetailsClass memberDetails;
    private static WCUserClass userClass;

    private static AutoCompleteTextView tvAuto_member,tvAuto_member2;
    private static ImageButton ib_search;

    private String Status="";
    private static Spinner spin_sbAcNo;
    private static String getsbAccount = "NA";
    private static ArrayList<String> SBAccountList = new ArrayList<>();
    private BroadcastReceiver mNetworkReceiver1;
    private static DatabaseHelper db;
    private static LoadCollectorSBAccount loadCollectorSBAccount;
    private static List<LoadCollectorSBAccount> loadCollectorSBAccounts;
    private static List<String> accountnumber = new ArrayList<String>();
    private static List<String> accountbalance = new ArrayList<String>();
    private static String[] AccountNoArray;
    private static HashMap<Integer, String> balanceId;
    private static String balanceID, accoountId;
    private static newagentsetget newagentsetget;
    private static List<newagentsetget> newagentsetgets;
    private static List<String> agentregamountlistid = new ArrayList<String>();
    private static List<String> regvalue = new ArrayList<String>();
    private static String[] RegisterNoArray;
    private static  HashMap<Integer, String> registerId;
    private static Context context;
    public static final int INSERAGENT_SYNCED_WITH_SERVER = 1;
    public static final int INSERAGENT_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST_INSERTAGENT = "com.cmb_collector";
    private BroadcastReceiver broadcastReceiver;
    private InsertagentNetworkStateChecker insertagentNetworkStateChecker;
    private String[] ranktype = {"0"};
    static int OffLineflag = 0,OnLineflag=0;
    static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_agent_joining);
        NewAgentJoining.context = getApplicationContext();
        OffLineflag=0;
        OnLineflag=0;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        init();
        setDefaultDetails();
        setListener();
//        serviceForMemberNameList();
//        serviceForGetDownRankList();
//        serviceForRegAmountList();
//        serviceForSbAccountList();
        //  offlinecheck();
        mNetworkReceiver1 = new NetworkChange();
        registerNetworkBroadcastForNougat();

     //   db = new DatabaseHelper(NewAgentJoining.context);
        loadCollectorSBAccounts = new ArrayList<>();
        accountnumber = new ArrayList<>();
        accountbalance = new ArrayList<>();
        newagentsetgets = new ArrayList<>();
        agentregamountlistid = new ArrayList<>();
        regvalue = new ArrayList<>();
//        if (Utility.checkConnectivity(NewAgentJoining.this)) {
//            spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                String payMode;
//
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    getsbAccount = SBAccountList.get(position);
//                    Sp.getInstance(getApplicationContext()).storeAccountName(getsbAccount);
//                    serviceforbalance(getsbAccount);
//                    // Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        } else {
//            spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    balanceID = balanceId.get(spin_sbAcNo.getSelectedItemPosition());
//                    accoountId = spin_sbAcNo.getSelectedItem().toString();
//                    getsbAccount = spin_sbAcNo.getSelectedItem().toString();
//                    // Toast.makeText(parent.getContext(),"OFF"+balanceID+" "+" "+accoountId, Toast.LENGTH_LONG).show();
//                    et_cash_balance.setText(balanceID);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//
//            });
//            loadCollectorSBAccount();
//            accountnumberbalance();
//            registeramount();
//            registerbalance();
//            ArrayAdapter pre = new ArrayAdapter(this, R.layout.spinner_item, ranktype);
//            rank_type.setAdapter(pre);
//        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST_INSERTAGENT));
        insertagentNetworkStateChecker = new InsertagentNetworkStateChecker();
        registerReceiver(insertagentNetworkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    public static void dialog(boolean value){
        if(value){
            ib_search.setVisibility(View.VISIBLE);
            tvAuto_member.setVisibility(View.VISIBLE);
            tvAuto_member2.setVisibility(View.GONE);
            OnLineflag++;
            tv_memberNo.setEnabled(false);
            tv_memberNo.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_memberName.setEnabled(false);
            tv_memberName.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_fath_name.setEnabled(false);
            tv_fath_name.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_address.setEnabled(false);
            tv_address.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_phone.setEnabled(false);
            tv_phone.setBackgroundResource(R.drawable.xml_et_bg_border_gray);

            System.out.println("status_Internet"+"we are back"+OnLineflag);
            if (OnLineflag==1){
                System.out.println("status_Internet"+"if block"+OnLineflag);
                customProgressDialog = new CustomProgressDialog(NewAgentJoining.context);
                serviceForMemberNameList();
        serviceForGetDownRankList();
        serviceForRegAmountList();
        serviceForSbAccountList();
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
            System.out.println("status_Internet"+"could not connect to the internet "+OffLineflag);
            //  Toast.makeText(getApplicationContext(),"Net off",Toast.LENGTH_LONG).show();
            tv_memberNo.setEnabled(true);
            tv_memberNo.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_memberName.setEnabled(true);
            tv_memberName.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_fath_name.setEnabled(true);
            tv_fath_name.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_address.setEnabled(true);
            tv_address.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_phone.setEnabled(true);
            tv_phone.setBackgroundResource(R.drawable.xml_et_bg_border);
            if (OffLineflag==1){
                loadCollectorSBAccount();
            //    accountnumberbalance();
            }

            // habijabi();

        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver1, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver1, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver1);
            if(insertagentNetworkStateChecker!=null)
                unregisterReceiver(insertagentNetworkStateChecker);
            if (broadcastReceiver!=null)
                unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
    private void offlinecheck() {
        if (Utility.checkConnectivity(NewAgentJoining.this)) {
            tv_memberNo.setFocusable(false);
            Toast.makeText(getApplicationContext(),"Net on",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),"Net off",Toast.LENGTH_LONG).show();
            tv_memberNo.setFocusable(true);
            tv_memberNo.setBackgroundResource(R.drawable.xml_et_bg_border);
        }
    }
    private void init() {
        tv_bCode = findViewById(R.id.tv_bCode);
        tv_bName = findViewById(R.id.tv_bName);

        profile_image = findViewById(R.id.profile_image);
        civ_sign = findViewById(R.id.civ_sign);
        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);

        li_sb_accNo = findViewById(R.id.li_sb_accNo);
        li_balance = findViewById(R.id.li_balance);

        tv_memberNo = findViewById(R.id.tv_memberNo);
        tv_memberName = findViewById(R.id.tv_memberName);
        tv_fath_name = findViewById(R.id.tv_fath_name);
        tv_address = findViewById(R.id.tv_address);
        tv_phone = findViewById(R.id.tv_phone);
        tv_doj = findViewById(R.id.tv_doj);
        edit_introducer_code = findViewById(R.id.edit_introducer_code);
        edit_introducer_name = findViewById(R.id.edit_introducer_name);
        tv_sb_accNo = findViewById(R.id.tv_sb_accNo);
        tv_balance = findViewById(R.id.tv_balance);

        btn_save = findViewById(R.id.btn_save);

        spin_member = findViewById(R.id.spin_member);
        pay_type = findViewById(R.id.pay_type);
        rank_type = findViewById(R.id.rank_type);
        reg_amount = findViewById(R.id.reg_amount);

        llout_cash_bal = findViewById(R.id.llout_cash_bal);
        et_cash_balance = findViewById(R.id.et_cash_balance);

        ib_search = findViewById(R.id.ib_search);
        tvAuto_member = findViewById(R.id.tvAuto_member);

        userDetailsData = WCUserClass.getInstance();
        memberDetails = WCMemberDetailsClass.getInstance();
        userClass = WCUserClass.getInstance();
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);
        tvAuto_member2 = findViewById(R.id.tvAuto_member2);

        customProgressDialog = new CustomProgressDialog(NewAgentJoining.this);

    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewAgentJoining.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });


        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAuto_member.equals("")){
                    tvAuto_member.setError("");
                }else {
                    serviceForMemberDetails(tvAuto_member.getText().toString());

                }
            }
        });

      /*  spin_member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String memberName;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                memberName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        pay_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String payMode;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payMode = parent.getItemAtPosition(position).toString();
                li_sb_accNo.setVisibility(View.GONE);
                li_balance.setVisibility(View.GONE);
                llout_cash_bal.setVisibility(View.VISIBLE);
                //  serviceforbalance();
//                Utility.serviceforbalance(NewAgentJoining.this,userClass.getGlobalUserCode());
//                et_cash_balance.setText(Utility.Savings_Balance);
//                if (payMode.equalsIgnoreCase("Saving A/C")) {
//                    li_sb_accNo.setVisibility(View.VISIBLE);
//                    li_balance.setVisibility(View.VISIBLE);
//                    llout_cash_bal.setVisibility(View.GONE);
//
//                    serviceForSB_AccountBalance();
//                } else {
//                    li_sb_accNo.setVisibility(View.GONE);
//                    li_balance.setVisibility(View.GONE);
//                    llout_cash_bal.setVisibility(View.VISIBLE);
//
//                    Utility.serviceforbalance(NewAgentJoining.this,userClass.getGlobalUserCode());
//                    et_cash_balance.setText(Utility.Savings_Balance);
//                    serviceForCashBalanceByMember();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pay_type.getSelectedItem().toString().equalsIgnoreCase("Saving A/C")) {

                    if (Double.parseDouble(et_cash_balance.getText().toString()) < Double.parseDouble(reg_amount.getSelectedItem().toString())) {
                        PopupClass.showPopUpWithTitleMessageOneButton(NewAgentJoining.this, "OK", "Error!!!", "", "Not sufficient Wallet Balance to carry out Transac", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                    } else {
                        Random rnd = new Random();
                        int number = rnd.nextInt(9999);
                        Sp.getInstance(getApplicationContext()).storeOtpValue(number);
                        //  Toast.makeText(getApplicationContext(),userClass.getPhoneNumber(),Toast.LENGTH_LONG).show();
                       // SendSms(number);
                         serviceForInsertAgent();
                    }
                } else {
                    // serviceForInsertAgent();
                }
            }
        });

//
//        spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            String payMode;
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                getsbAccount = SBAccountList.get(position);
//                Sp.getInstance(getApplicationContext()).storeAccountName(getsbAccount);
//                serviceforbalance(getsbAccount);
//                // Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAgentJoining.this);
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
                    serviceForInsertAgent();
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


    private static void serviceforbalance(String getsbAccount) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAgentJoining.context);
      //  dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailSavingsBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("savings Balance NEW "+response);
           //     dialog.dismissDialog();
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
           //     dialog.dismissDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewAgentJoining.context);
        requestQueue.add(str);

    }

    private void setDefaultDetails() {
        tv_bCode.setText(userDetailsData.getGlobalBCode());
        tv_bName.setText(userDetailsData.getGlobalBranchName());
        //Current Date:

        tv_doj.setText(Utility.setCurrentDate());

        edit_introducer_code.setText(userDetailsData.getGlobalUserCode());
        edit_introducer_name.setText(userDetailsData.getGlobalUserName());

        setPayTypeList();

    }

    private static void setMember() {
        ArrayAdapter adapterMember = new ArrayAdapter(NewAgentJoining.context, R.layout.spinner_item, memberList);
        tvAuto_member.setAdapter(adapterMember);
    }

    private void setPayTypeList() {
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, payType);
        pay_type.setAdapter(aa);
    }

    private static void serviceForMemberNameList() {
        final CustomProgressDialog dialog1 = new CustomProgressDialog(NewAgentJoining.context);

     //   progressDialog.show();
     //   dialog1.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Member_MemberNameList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
           //     progressDialog.dismiss();
//                dialog1.dismissDialog();
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
          //      progressDialog.dismiss();
            //    dialog1.dismissDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserType", "COLLECTOR");
                params.put("CollectorCode", userDetailsData.getGlobalUserCode());

                System.out.println("params..." + params);

                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(NewAgentJoining.context);
        requestQueue.add(str);
    }

    private static void serviceForGetDownRankList() {
        final CustomProgressDialog dialog1 = new CustomProgressDialog(NewAgentJoining.context);
     //   dialog1.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_GetDownRankList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //    dialog1.dismissDialog();
                System.out.println(" GET_Agent_GetDownRankList "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        JSONArray array = object.getJSONArray("RankList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            rankList.add(jsonObject.optString("Rank"));
                            rankStagesList.add(jsonObject.optString("Stages"));
                        }
                        setRankList();

                    }else {
                        rankList.add("0");
                        rankStagesList.add("0");
                        setRankList();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   dialog1.dismissDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("SponsorCode", userDetailsData.getGlobalUserCode());
                params.put("UserType", "COLLECTOR");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(NewAgentJoining.context);
        requestQueue.add(str);
    }

    private void serviceForCashBalanceByMember() {

        final CustomProgressDialog dialog = new CustomProgressDialog(NewAgentJoining.this);
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
                    PopupClass.showPopUpWithTitleMessageOneButton(NewAgentJoining.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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
                PopupClass.showPopUpWithTitleMessageOneButton(NewAgentJoining.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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
    private static void setRankList() {
        ArrayAdapter rank = new ArrayAdapter(NewAgentJoining.context, R.layout.spinner_item, rankStagesList);
        rank_type.setAdapter(rank);
    }

    private static void serviceForRegAmountList() {
        final CustomProgressDialog dialog1 = new CustomProgressDialog(NewAgentJoining.context);
     //   dialog1.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Config_AgentRegAmountList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //    dialog1.dismissDialog();
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        JSONArray array = object.getJSONArray("RegAmount");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            regAmountList.add(jsonObject.optString("Value"));
                        }
                        // regAmountList.add("1000000");
                        setRegAmountList();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
             //   dialog1.dismissDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //            params.put("SponsorCode", userDetailsData.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(NewAgentJoining.context);
        requestQueue.add(str);

    }

    private static void setRegAmountList() {
        ArrayAdapter amount = new ArrayAdapter(NewAgentJoining.context, R.layout.spinner_item, regAmountList);
        reg_amount.setAdapter(amount);
    }

    private void serviceForLoadProfilePic(final String memberNo) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAgentJoining.this);
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
                            Glide.with(NewAgentJoining.this)
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

    private void serviceForMemberDetails(final String memberName) {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAgentJoining.this);
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

                            memberDetails.setMemberNo(jsonObject.optString("MemberCode"));
                            memberDetails.setMemberName(jsonObject.optString("MemberName"));
                            memberDetails.setFatherName(jsonObject.optString("Father"));
                            memberDetails.setAddress(jsonObject.optString("Address"));
                            memberDetails.setPhno(jsonObject.optString("Phone"));
                            memberDetails.setProfilePic(jsonObject.optString("ProfilePic"));
                            memberDetails.setSignPic(jsonObject.optString("SignPic"));

                            tv_memberNo.setText(memberDetails.getMemberNo());
                            tv_memberName.setText(memberDetails.getMemberName());
                            tv_fath_name.setText(memberDetails.getFatherName());
                            tv_address.setText(memberDetails.getAddress());
                            tv_phone.setText(memberDetails.getPhno());
                            Glide.with(NewAgentJoining.this)
                                    .load(LoadPic(memberDetails.getProfilePic()))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.profile_view)
                                            .skipMemoryCache(true))
                                    .into(profile_image);
                            Glide.with(NewAgentJoining.this)
                                    .load(LoadPic(memberDetails.getSignPic()))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.ic_sign)
                                            .skipMemoryCache(true))
                                    .into(civ_sign);
                        }
//                        serviceForLoadProfilePic(tv_memberNo.getText().toString());
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
                params.put("NameText", memberName);
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

    private void serviceForSB_AccountBalance() {
        final CustomProgressDialog dialog = new CustomProgressDialog(NewAgentJoining.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_SB_AccountBalanceByMember", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        JSONArray array = object.getJSONArray("AccountNo");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            tv_sb_accNo.setText(jsonObject.optString("AccountNo"));
                            tv_balance.setText(jsonObject.optString("Balance"));
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
                params.put("MemberCode", userDetailsData.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForInsertAgent() {
        customProgressDialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Agent_InsertAgent", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("AGENT "+response);
                try {
                    String agentCode = null;
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("AgentReturn");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);

                        if (jsonObject.getInt("ErrorCode") == 0)
                            agentCode = jsonObject.optString("AgentCode");
                        Status=jsonObject.optString("Status");
                    }

                    PopupClass.showPopUpWithTitleMessageOneButton(NewAgentJoining.this, "OK", Status, "Agent Code is:", agentCode, new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                            customProgressDialog.dismissDialog();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalBCode = userDetailsData.getGlobalBCode();
                memberNo = tv_memberNo.getText().toString();
                memberName = tv_memberName.getText().toString();
                fathname = tv_fath_name.getText().toString();
                address = tv_address.getText().toString();
                doj = tv_doj.getText().toString();
                GlobalUserCode = userDetailsData.getGlobalUserCode();
                regamount = reg_amount.getSelectedItem().toString();
                paytype =  pay_type.getSelectedItem().toString();
                getGlobalUserCode = userDetailsData.getGlobalUserCode();
                db.agentInsert(GlobalBCode, memberNo, memberName, fathname,
                      address, doj,GlobalUserCode, "0",
                      regamount, paytype, getsbAccount, userDetailsData.getGlobalUserCode(),
                      INSERAGENT_NOT_SYNCED_WITH_SERVER
                            );
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("BCODE", userDetailsData.getGlobalBCode());
                params.put("MemberNo", memberDetails.getMemberNo());
                params.put("ANAME", memberDetails.getMemberName());
                params.put("GNAME", memberDetails.getFatherName());
                params.put("ADD", memberDetails.getAddress());
                params.put("ADOJ", tv_doj.getText().toString());
                params.put("IC", userDetailsData.getGlobalUserCode());
                //      Log.d("XXXXXXXXrank", rankList.get(rank_type.getSelectedItemPosition()));
                // params.put("RANK", "0");
                params.put("RANK", rankList.get(rank_type.getSelectedItemPosition()));
                params.put("REGAMOUNT", reg_amount.getSelectedItem().toString());
                params.put("PAYMODE", pay_type.getSelectedItem().toString());
              //  params.put("SBAccount", getsbAccount);
                params.put("SBACNo", getsbAccount);
//                if (pay_type.getSelectedItem().toString().equalsIgnoreCase("Cash"))
//                    params.put("SBACNo", "");
//                else
//                    params.put("SBACNo", tv_sb_accNo.getText().toString());
                params.put("USERNAME", userDetailsData.getGlobalUserCode());
                System.out.println("params_NA"+params.toString());
                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
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
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(NewAgentJoining.context, R.layout.spinner_item, SBAccountList);
                            spin_sbAcNo.setAdapter(spinnerSBAmount);
                            spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String payMode;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getsbAccount = SBAccountList.get(position);
                Sp.getInstance(NewAgentJoining.context).storeAccountName(getsbAccount);
                serviceforbalance(getsbAccount);
                // Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(NewAgentJoining.context);
        requestQueue.add(stringRequest);
    }

    public static void loadCollectorSBAccount() {
        db = new DatabaseHelper(NewAgentJoining.context);
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
            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(NewAgentJoining.context, R.layout.spinner_item, accountnumber);
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



    private static void accountnumberbalance() {
        AccountNoArray = new String[loadCollectorSBAccounts.size()];
        balanceId = new HashMap<Integer, String>();
        for (int i = 0; i < loadCollectorSBAccounts.size(); i++) {
            balanceId.put(i, accountbalance.get(i));
            AccountNoArray[i] = accountnumber.get(i);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NewAgentJoining.context,
                android.R.layout.simple_spinner_item, AccountNoArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sbAcNo.setAdapter(dataAdapter);

    }

    public   void registeramount() {
        db = new DatabaseHelper(getApplicationContext());
        Cursor cursor = db.AgentRegAmount();
        if (cursor.moveToFirst()) {
            do {
                newagentsetget = new newagentsetget(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENTREGAMOUNTID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_REGVALUE))
                );
                String aa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENTREGAMOUNTID));
                String aaa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_REGVALUE));
                System.out.println("accountnumberlist" + " " + aa + " " + aaa);
                newagentsetgets.add(newagentsetget);
                agentregamountlistid.add(newagentsetget.getAgentregamountlistid());
                regvalue.add(newagentsetget.getRegvalue());
            }
            while (cursor.moveToNext());
        }
    }
    public  void registerbalance() {
        RegisterNoArray = new String[newagentsetgets.size()];
        registerId = new HashMap<Integer, String>();
        for (int i = 0; i < loadCollectorSBAccounts.size(); i++) {
            registerId.put(i, agentregamountlistid.get(i));
            RegisterNoArray[i] = regvalue.get(i);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, RegisterNoArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reg_amount.setAdapter(dataAdapter);
    }

    public static class InsertagentNetworkStateChecker extends BroadcastReceiver {
        private Context context;
        private DatabaseHelper db;
        String fund;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;
            db = new DatabaseHelper(context);

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            //if there is a network
            if (activeNetwork != null) {
                //if connected to wifi or mobile data plan
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Cursor cursor = db.agentInsertmembervalue();
                    int i = 1;
                    if (cursor.moveToFirst()) {
                        do {
                            INSERTMEMBERSYNC(
                                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_INSERTAGENTID)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_BCODE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_MEMBERMO)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_ANAME)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_GNAME)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_ADD)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_ADOJ)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_IC)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_RANK)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_REGAMOUNT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_PAYMODE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_SBACCOUNT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGENT_USERNAME)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_STATUSAGENT))

                            );
                            Toast.makeText(context, "Network Loop Insermember "+i,Toast.LENGTH_SHORT).show();
                            i++;

                        } while (cursor.moveToNext());
                    }

                }

            }
        }

        private void INSERTMEMBERSYNC(final int insertagentid, final String agentbcode, final String memberno, final String aname,
                                      final String gname, final String addval, final String adoj, final String ic,
                                      final String rank,
                                      final String regamount, final String paymode, final String sbaccount, final String username, final String statusagent) {
            StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Agent_InsertAgent", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("AGENT "+response);
                    try {
                        String agentCode = null;
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("AgentReturn");
                        db.updateAGENTmemberSyncStatus(insertagentid, NewAgentJoining.INSERAGENT_SYNCED_WITH_SERVER);
                        context.sendBroadcast(new Intent(NewAgentJoining.DATA_SAVED_BROADCAST_INSERTAGENT));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            if (jsonObject.getInt("ErrorCode") == 0)
                                agentCode = jsonObject.optString("AgentCode");
                                String Status = jsonObject.optString("Status");
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("BCODE", agentbcode);
                    params.put("MemberNo", memberno);
                    params.put("ANAME", aname);
                    params.put("GNAME", gname);
                    params.put("ADD", addval);
                    params.put("ADOJ", adoj);
                    params.put("IC", ic);
                    params.put("RANK", rank);
                    params.put("REGAMOUNT", regamount);
                    params.put("PAYMODE", paymode);
                    params.put("SBACNo", sbaccount);
//                if (pay_type.getSelectedItem().toString().equalsIgnoreCase("Cash"))
//                    params.put("SBACNo", "");
//                else
//                    params.put("SBACNo", tv_sb_accNo.getText().toString());
                    params.put("USERNAME",username);

                    System.out.println("params NA "+params.toString());
                    return params;
                }
            };
            str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(str);
        }
    }
}
