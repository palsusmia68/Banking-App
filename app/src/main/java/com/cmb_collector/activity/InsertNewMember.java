package com.cmb_collector.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
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

import androidx.annotation.Nullable;

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
import com.cmb_collector.database.DatabaseHelper;
import com.cmb_collector.model.LoadCollectorSBAccount;
import com.cmb_collector.model.Registeramountbalance;
import com.cmb_collector.model.RelationSetGet;
import com.cmb_collector.model.WCRegisAmount;
import com.cmb_collector.model.WCRelationData;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.FileUtil;
import com.cmb_collector.utility.ImageCompressor;
import com.cmb_collector.utility.ImagePickerActivity;
import com.cmb_collector.utility.Sp;
import com.cmb_collector.utility.Utility;
import com.goodiebag.pinview.Pinview;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InsertNewMember extends AppBaseClass {
    public static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_FOR_SIGNATURE = 101;
    private static final int REQUEST_IMAGE_FINGERPRINT = 102;
    private Spinner spin_prefix;
    private Spinner spin_sex;
    private Spinner spin_nominee_rel;
    private Spinner spin_pay_type;
    private Spinner spin_rgs_amount;
    private Spinner spin_kyc;
    private Spinner spin_sbAcNo;
    private CircleImageView profile_image, img_sign, img_fingerprint;
    private ImageView img_mem_back;
    private EditText et_application_no;
    private EditText et_member_name;
    private EditText et_father_name;
    private EditText et_address;
    private EditText et_pin_code;
    private EditText et_phone_no;
    private EditText et_email;
    private EditText et_pan_no;
    private EditText et_aadhar_no;
    private EditText et_id_proof_doc;
    private EditText et_nominee;
    private EditText et_nominee_age;
    private EditText et_bank_name;
    private EditText et_bank_acno;
    private EditText et_ifsc_code;
    private EditText et_sponsor_code;
    //    private EditText sb_account;
    private TextView tv_doj;
    private TextView tv_dob;
    private TextView tv_age;
    private TextView tv_branch_code;
    private TextView tv_branch_name;
    private Button chng_dp;
    private Button chng_sign;
    private Button btn_fingerprint;
    private Button btn_save_member;
    private File actualProfileImage;
    private File compressedProfileImage = null;
    final int PIC_CROP = 1;
    private File actualSignImage;
    private File compressedSignImage = null;
    private String[] prefix = {"Mr", "Mrs", "Miss", "Dr"};
    private String[] kyc = {"PAN NO.", "VALID PASSPORT", "ELECTION CARD", "PHOTO IDENTITY ISSUED BY GOVERMENT",
            "DRIVING LICENSE WITH PHOTO", "PHOTO CREDIT CARD", "EMPLOYEE ID CARD", "AADHAR CARD"};
    private String[] sex_type = {"Male", "Female"};
    private String[] pay_type = {"Saving A/C"};
    private Calendar myDob = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dobDate;
    private WCUserClass userClass;
    private WCRegisAmount regisAmount;
    private WCRelationData relationData;
    private ArrayList<WCRelationData> relationDataList = new ArrayList<>();
    private ArrayList<String> listRelation = new ArrayList<>();
    private ArrayList<WCRegisAmount> regisAmountList = new ArrayList<>();
    private ArrayList<String> listRegisAmount = new ArrayList<>();
    private ArrayList<String> SBAccountList = new ArrayList<>();
    private String  sbAccount;
    private String formNo = "NA";
    private String GlobalBCode = "NA";
    private String GlobalUserCode = "NA";
    private String name = "NA";
    private String pref = "NA";
    private String father = "NA";
    private String address = "NA";
    private String pin = "NA";
    private String dob = "NA";
    private String age = "NA";
    private String nomineeName = "NA";
    private String nomineeAge = "NA";
    private String nomineeRelation = "NA";
    private String gender = "NA";
    private String phone = "NA";
    private String email = "NA";
    private String pan = "NA";
    private String aadhar = "NA";
    private String bankAcNo = "NA";
    private String bankName = "NA";
    private String bankIfsc = "NA";
    private String idProof = "NA";
    private String idProofDocNo = "NA";
    private String sponsorCode = "NA";
    private String doj = "NA";
    private String rgsAmount = "NA";
    private String paytype = "NA";
    private String getsbAccount = "NA";
    private String error_code, memberCode, Status = "";
    private String stringFinger = "";
    private CustomProgressDialog customProgressDialog;
    private Bitmap fingerprintBitmapData;
    private Dialog dialog;
    private ImageView camera_img;
    private ImageView gallery_img;
    private ImageView img_remove;
    private Button btn_cancel;
    Uri cameraImageUri = null;
    private static final int REQUEST_PROFILE_CAMERA_CODE = 11;
    private static final int REQUEST_PROFILE_GALLERY_CODE = 22;
    private static final int REQUEST_SIGN_CAMERA_CODE = 33;
    private static final int REQUEST_SIGN_GALLERY_CODE = 44;
    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout llout_cash_bal;
    private TextView et_cash_balance;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private DatabaseHelper db;
    RelationSetGet relationSetGet;
    Registeramountbalance registeramountbalance;
    LoadCollectorSBAccount loadCollectorSBAccount;
    List<LoadCollectorSBAccount> loadCollectorSBAccounts;
    List<RelationSetGet> relationSetGets;
    List<Registeramountbalance> registeramountbalances;
    List<String> relation = new ArrayList<String>();
    List<String> relationId = new ArrayList<String>();
    List<String> accountnumber = new ArrayList<String>();
    List<String> accountbalance = new ArrayList<String>();
    List<String> registeramount = new ArrayList<String>();
    List<String> registeramountid = new ArrayList<String>();
    String[] RelationArray;
    HashMap<Integer, String> relationMapId;
    String[] AccountNoArray;
    String[] RegistationArray;
    HashMap<Integer, String> registrationMapId;
    HashMap<Integer, String> balanceId;
    String balanceID, accoountId;
    public static final int INSERMEMBER_SYNCED_WITH_SERVER = 1;
    public static final int INSERMEMBER_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST_INSERTMEMBER = "com.cmb_collector";
    private BroadcastReceiver broadcastReceiver;
    private InsertMemberNetworkStateChecker insertMemberNetworkStateChecker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_insert_new_member);
        customProgressDialog = new CustomProgressDialog(InsertNewMember.this);
        initView();
        setDefaults();
        setListener();
        serviceForRelationList();
        serviceForRegisAmount();
        serviceForSbAccountList();
        ImagePickerActivity.clearCache(this);
        db = new DatabaseHelper(getApplicationContext());
        registeramountbalances = new ArrayList<>();
        relationSetGets = new ArrayList<>();
        relation = new ArrayList<>();
        relationId = new ArrayList<>();
        loadCollectorSBAccounts = new ArrayList<>();
        accountnumber = new ArrayList<>();
        accountbalance = new ArrayList<>();
        registeramountid = new ArrayList<>();
        registeramount = new ArrayList<>();
        OfflineRelationList();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST_INSERTMEMBER));
        insertMemberNetworkStateChecker = new InsertMemberNetworkStateChecker();
        registerReceiver(insertMemberNetworkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void OfflineRelationList() {
        if (Utility.checkConnectivity(InsertNewMember.this)) {

        } else {
            relationlist();
            relationDataSpOffline();
            loadCollectorSBAccount();
            accountnumberbalance();
            registeramount();
            registeramountoffline();
            spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    balanceID = balanceId.get(spin_sbAcNo.getSelectedItemPosition());
                    accoountId = spin_sbAcNo.getSelectedItem().toString();
                    getsbAccount = spin_sbAcNo.getSelectedItem().toString();
                    // Toast.makeText(parent.getContext(),"OFF"+balanceID+" "+" "+accoountId, Toast.LENGTH_LONG).show();
                    et_cash_balance.setText(balanceID);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        }
    }

    private void relationlist() {
        db = new DatabaseHelper(getApplicationContext());
        Cursor cursor = db.getRelationData();
        if (cursor.moveToFirst()) {
            do {
                relationSetGet = new RelationSetGet(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLEID_CMBRELATIONID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_CMBRELATIONLIST))
                );
                String aa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLEID_CMBRELATIONID));
                String aaa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_CMBRELATIONLIST));
                relationSetGets.add(relationSetGet);
                relationId.add(relationSetGet.getRelationid());
                relation.add(relationSetGet.getRelationlist());
                System.out.println("DATAVALA" + relationSetGet.getRelationid() + " " + relationSetGet.getRelationlist());
            }
            while (cursor.moveToNext());
        }
    }

    private void relationDataSpOffline() {
        RelationArray = new String[relationSetGets.size()];
        relationMapId = new HashMap<Integer, String>();
        for (int i = 0; i < relationSetGets.size(); i++) {
            relationMapId.put(i, relationId.get(i));
            RelationArray[i] = relation.get(i);
            System.out.println("datavalue" + " " + RelationArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, RelationArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_nominee_rel.setAdapter(dataAdapter);
    }

    private void loadCollectorSBAccount() {
        db = new DatabaseHelper(getApplicationContext());
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
        }
    }

    private void accountnumberbalance() {
        AccountNoArray = new String[loadCollectorSBAccounts.size()];
        balanceId = new HashMap<Integer, String>();
        for (int i = 0; i < loadCollectorSBAccounts.size(); i++) {
            balanceId.put(i, accountbalance.get(i));
            AccountNoArray[i] = accountnumber.get(i);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, AccountNoArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sbAcNo.setAdapter(dataAdapter);
    }

    private void registeramount() {
        db = new DatabaseHelper(getApplicationContext());
        Cursor cursor = db.getregistervalue();
        if (cursor.moveToFirst()) {
            do {
                registeramountbalance = new Registeramountbalance(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_REGISTERAMOUNTID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_REGISTERAMOUNTBALANCE))

                );
                registeramountbalances.add(registeramountbalance);
                registeramountid.add(registeramountbalance.getRegisteramountid());
                registeramount.add(registeramountbalance.getRegisteramountbalnce());
            }
            while (cursor.moveToNext());
        }

    }

    private void registeramountoffline() {
        RegistationArray = new String[registeramountbalances.size()];
        registrationMapId = new HashMap<Integer, String>();
        for (int i = 0; i < registeramountbalances.size(); i++) {
            registrationMapId.put(i, registeramountid.get(i));
            RegistationArray[i] = registeramount.get(i);
            System.out.println("datavalue" + " " + RegistationArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, RegistationArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_rgs_amount.setAdapter(dataAdapter);
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
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(InsertNewMember.this, R.layout.spinner_item, SBAccountList);
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

    private void initView() {
        tv_doj = findViewById(R.id.tv_doj);
        tv_dob = findViewById(R.id.tv_dob_member);
        tv_age = findViewById(R.id.tv_age);
        tv_branch_code = findViewById(R.id.tv_branch_code);
        tv_branch_name = findViewById(R.id.tv_branch_name);
        img_mem_back = findViewById(R.id.img_mem_back);
        img_sign = findViewById(R.id.img_sign);
        profile_image = findViewById(R.id.profile_image);
        chng_sign = findViewById(R.id.chng_sign);
        img_fingerprint = findViewById(R.id.img_fingerprint);
        spin_prefix = findViewById(R.id.spin_prefix);
        spin_sex = findViewById(R.id.spin_sex);
        spin_nominee_rel = findViewById(R.id.spin_nominee_rel);
        spin_pay_type = findViewById(R.id.spin_pay_type);
        spin_rgs_amount = findViewById(R.id.spin_rgs_amount);
        spin_kyc = findViewById(R.id.spin_kyc);
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);
        et_application_no = findViewById(R.id.et_application_no);
        et_member_name = findViewById(R.id.et_member_name);
        et_father_name = findViewById(R.id.et_father_name);
        et_address = findViewById(R.id.et_address);
        et_pin_code = findViewById(R.id.et_pin_code);
        et_phone_no = findViewById(R.id.et_phone_no);
        et_email = findViewById(R.id.et_email);
        et_pan_no = findViewById(R.id.et_pan_no);
        et_aadhar_no = findViewById(R.id.et_aadhar_no);
        et_id_proof_doc = findViewById(R.id.et_id_proof_doc);
        et_nominee = findViewById(R.id.et_nominee);
        et_nominee_age = findViewById(R.id.et_nominee_age);
        et_bank_name = findViewById(R.id.et_bank_name);
        et_bank_acno = findViewById(R.id.et_bank_acno);
        et_ifsc_code = findViewById(R.id.et_ifsc_code);
        et_sponsor_code = findViewById(R.id.et_sponsor_code);
        //  sb_account = findViewById(R.id.sb_account);
        chng_dp = findViewById(R.id.chng_dp);
        btn_fingerprint = findViewById(R.id.btn_fingerprint);
        btn_save_member = findViewById(R.id.btn_save_member);
        llout_cash_bal = findViewById(R.id.llout_cash_bal);
        et_cash_balance = findViewById(R.id.et_cash_balance);
        userClass = WCUserClass.getInstance();
    }

    private void setDefaults() {
        tv_branch_code.setText(userClass.getGlobalBCode());
        tv_branch_name.setText(userClass.getGlobalBranchName());
        tv_doj.setText(Utility.setCurrentDate());
        et_sponsor_code.setText(userClass.getGlobalUserCode());
        ArrayAdapter pre = new ArrayAdapter(this, R.layout.spinner_item, prefix);
        spin_prefix.setAdapter(pre);
        ArrayAdapter kyc_details = new ArrayAdapter(this, R.layout.spinner_item, kyc);
        spin_kyc.setAdapter(kyc_details);
        ArrayAdapter sex = new ArrayAdapter(this, R.layout.spinner_item, sex_type);
        spin_sex.setAdapter(sex);
        ArrayAdapter payType = new ArrayAdapter(this, R.layout.spinner_item, pay_type);
        spin_pay_type.setAdapter(payType);
    }

    private void setListener() {
        btn_save_member.setOnClickListener(MemberInsertActionButton);
        img_mem_back.setOnClickListener(BackAction);
        chng_dp.setOnClickListener(changDpListener);
        chng_sign.setOnClickListener(signListener);
        btn_fingerprint.setOnClickListener(scanFingerListener);

        dobDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myDob.set(Calendar.YEAR, year);
                myDob.set(Calendar.MONTH, monthOfYear);
                myDob.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateFormat();
            }
        };

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(InsertNewMember.this, dobDate,
                        myDob.get(Calendar.YEAR),
                        myDob.get(Calendar.MONTH),
                        myDob.get(Calendar.DAY_OF_MONTH));

                date.getDatePicker().setMaxDate(System.currentTimeMillis());
                date.show();

            }
        });

        spin_pay_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String payMode;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payMode = parent.getItemAtPosition(position).toString();

                if (payMode.equalsIgnoreCase("Saving A/C")) {

                    llout_cash_bal.setVisibility(View.VISIBLE);
//                    getsbAccount = SBAccountList.get(position);
                    //     serviceforbalance(getsbAccount);
//                    Utility.serviceforbalance(InsertNewMember.this,userClass.getGlobalUserCode());
//                  //  et_cash_balance.setText("20879");
//                    et_cash_balance.setText(Utility.Savings_Balance);
                    // serviceForCashBalanceByMember();
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
                //   Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void serviceforbalance(String getsbAccount) {
        final CustomProgressDialog dialog = new CustomProgressDialog(InsertNewMember.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailSavingsBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("savings Balance " + response);
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

//    private void serviceForCashBalanceByMember() {
//        final CustomProgressDialog dialog = new CustomProgressDialog(InsertNewMember.this);
//        dialog.showLoader();
//        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailWalletBalance", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                dialog.dismissDialog();
//                try {
//                    JSONObject object = new JSONObject(response);
//                    String Error_Code = object.optString("Error_Code");
//                    if (Error_Code.equals("0")) {
//                        JSONArray jsonArray = object.getJSONArray("AvailBalance");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            et_cash_balance.setText(jsonObject.optString("OpenBal"));
//                        }
//                    } else {
//                        et_cash_balance.setText("0");
//                    }
//
//                } catch (JSONException e) {
//                    PopupClass.showPopUpWithTitleMessageOneButton(InsertNewMember.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismissDialog();
//                PopupClass.showPopUpWithTitleMessageOneButton(InsertNewMember.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                    @Override
//                    public void onFirstButtonClick() {
//
//                    }
//                });
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("UserId", userClass.getGlobalUserCode());
//                return params;
//            }
//        };
//
//        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(str);
//
//    }

    View.OnClickListener BackAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(InsertNewMember.this, NewRequest.class));
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    View.OnClickListener scanFingerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(InsertNewMember.this, FingerPrintActivity.class);
            intent.putExtra("scanner_action", Utility.ScannerAction.Capture);
            startActivityForResult(intent, REQUEST_IMAGE_FINGERPRINT);
        }
    };

    View.OnClickListener changDpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // chooseProfileImage();
            onProfileImageClick();
        }
    };

    View.OnClickListener signListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //  chooseSignImage();
            onSignatureImageClick();
        }
    };

    private void onSignatureImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptionsForSignature();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptionsForSignature() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntentForSignature();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntentForSignature();
            }
        });
    }

    private void launchGalleryIntentForSignature() {
        Intent intent = new Intent(InsertNewMember.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE_FOR_SIGNATURE);
    }

    private void launchCameraIntentForSignature() {
        Intent intent = new Intent(InsertNewMember.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE_FOR_SIGNATURE);
    }

    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(InsertNewMember.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(InsertNewMember.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    profile_image.setImageBitmap(bitmap);

                    actualProfileImage = FileUtil.from(this, uri);
                    if (actualProfileImage == null) {
                        Toast.makeText(InsertNewMember.this, "Choose an Image", Toast.LENGTH_SHORT).show();
                    } else {

                        new ImageCompressor(InsertNewMember.this)
                                .compressToFileAsFlowable(actualProfileImage)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<File>() {
                                    @Override
                                    public void accept(File file) throws Exception {
                                        compressedProfileImage = file;
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                        Toast.makeText(InsertNewMember.this, "Error in Compress", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_FOR_SIGNATURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    img_sign.setImageBitmap(bitmap);

                    actualSignImage = FileUtil.from(this, uri);
                    if (actualSignImage == null) {
                        Toast.makeText(InsertNewMember.this, "Choose an Image", Toast.LENGTH_SHORT).show();
                    } else {
                        new ImageCompressor(InsertNewMember.this)
                                .compressToFileAsFlowable(actualSignImage)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<File>() {
                                    @Override
                                    public void accept(File file) throws Exception {
                                        compressedSignImage = file;
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                        Toast.makeText(InsertNewMember.this, "Error in Compress", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_FINGERPRINT) {
            if (resultCode == Activity.RESULT_OK) {
                byte[] fingerData = data.getByteArrayExtra("scan_finger");
                byte[] fingerISOData = data.getByteArrayExtra("scan_ISO");
                fingerprintBitmapData = convertBytearrayToBitmap(fingerData);
                img_fingerprint.setImageBitmap(fingerprintBitmapData);
                stringFinger = Base64.encodeToString(fingerISOData, Base64.DEFAULT);
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertNewMember.this);
        builder.setTitle("Need to permision");
        builder.setMessage("Camera & Gallery permission need ");
        builder.setPositiveButton("Allow", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    View.OnClickListener MemberInsertActionButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            formNo = et_application_no.getText().toString().trim();
            pref = spin_prefix.getSelectedItem().toString().trim();
            name = et_member_name.getText().toString().trim();
            gender = spin_sex.getSelectedItem().toString().trim();
            father = et_father_name.getText().toString().trim();
            address = et_address.getText().toString().trim();
            pin = et_pin_code.getText().toString().trim();
            doj = tv_doj.getText().toString();
            dob = tv_dob.getText().toString().trim();
            age = tv_age.getText().toString();
            //  sbAccount =  sb_account.getText().toString();
            phone = et_phone_no.getText().toString().trim();
            email = et_email.getText().toString().trim();
            pan = et_pan_no.getText().toString().trim();
            aadhar = et_aadhar_no.getText().toString().trim();
            idProof = spin_kyc.getSelectedItem().toString();
            idProofDocNo = et_id_proof_doc.getText().toString().trim();
            nomineeName = et_nominee.getText().toString().trim();
            nomineeAge = et_nominee_age.getText().toString().trim();
            nomineeRelation = spin_nominee_rel.getSelectedItem().toString();
            bankName = et_bank_name.getText().toString().trim();
            bankAcNo = et_bank_acno.getText().toString().trim();
            bankIfsc = et_ifsc_code.getText().toString().trim();
            sponsorCode = et_sponsor_code.getText().toString().trim();
            paytype = spin_pay_type.getSelectedItem().toString();
            rgsAmount = spin_rgs_amount.getSelectedItem().toString();
            // serviceForInsertMember();
            if (formNo.isEmpty()) {
                et_application_no.setError("Enter Application No.");

            } else if (name.isEmpty()) {
                et_member_name.setError("Enter Member Name");

            } else if (father.isEmpty()) {
                et_father_name.setError("Enter Father Name");

            } else if (address.isEmpty()) {
                et_address.setError("Enter Address");

            } else if (pin.isEmpty()) {
                et_pin_code.setError("Enter PIN");

            } else if (dob.isEmpty()) {
                tv_dob.setError("Please select DOB");

            } else if (phone.isEmpty()) {
                et_phone_no.setError("Enter Phone No.");

            } else if (email.isEmpty()) {
                et_email.setError("Enter Email");

            }

//            else{ if (email.matches(emailPattern)){
//
//            }
            else if (!nomineeName.equals("") && nomineeAge.equals("")) {
                et_nominee_age.setError("Enter nominee age");

            } else {
                if (email.matches(emailPattern)) {
                    et_email.setError("valid Email Address");
                    Random rnd = new Random();
                    int number = rnd.nextInt(9999);
                    Sp.getInstance(getApplicationContext()).storeOtpValue(number);
                    // SendSms(number);
                    //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();

                } else {
                    et_email.setError("Invalid Email Address");
                    //  Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }
//                Random rnd = new Random();
//                int number = rnd.nextInt(9999);
//                Sp.getInstance(getApplicationContext()).storeOtpValue(number);
//                SendSms(number);
                //  Utility.sendSMS(InsertNewMember.this,"8250806960", String.valueOf(number));
                //serviceForInsertMember();
                /*if (spin_pay_type.getSelectedItem().toString().equalsIgnoreCase("Saving A/C")) {

                    if (Double.parseDouble(et_cash_balance.getText().toString()) < Double.parseDouble(spin_rgs_amount.getSelectedItem().toString())) {
                        PopupClass.showPopUpWithTitleMessageOneButton(InsertNewMember.this, "OK", "Error!!!", "", "Not sufficient Wallet Balance to carry out Transac", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),""+spin_rgs_amount.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                        serviceForInsertMember();
                    }
                } else {
                   // serviceForInsertMember();
                }*/

                serviceForInsertMember();
            }
        }
    };

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(InsertNewMember.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.sms_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("SMS response " + response);
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

    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
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
                int getpOtpNumber = Sp.getInstance(getApplicationContext()).getOtpValue();
                String SgetpOtpNumber = String.valueOf(getpOtpNumber);
                String user_input_password = pinview.getValue().toString().trim();
                if (SgetpOtpNumber.equals(user_input_password)) {
                    dialog.dismiss();
                    serviceForInsertMember();
                    // Toast.makeText(getApplicationContext(), "" + getpOtpNumber + "  " + user_input_password, Toast.LENGTH_LONG).show();
                } else {
                    wrongOtp.setVisibility(View.VISIBLE);

                    //  Toast.makeText(getApplicationContext(), "Wrong OTP"+userClass.getPhoneNumber(), Toast.LENGTH_LONG).show();

                }
            }
        });
        dialog.show();
        return dialog;
    }

    private void serviceForInsertMember() {
        if (Utility.checkConnectivity(InsertNewMember.this)) {
            customProgressDialog.showLoader();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Member_InsertMEMBER",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("PUT_Member_InsertMEMBER" + response);
                            Log.d("response new", response);
                            customProgressDialog.dismissDialog();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("SuccessMsg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    error_code = object.optString("ErrorCode");
                                    memberCode = object.optString("MemberCode");
                                    Status = object.optString("Status");
                                }
                                if (error_code.equals("0")) {
                                    //  Log.d("getAbsolutePath",compressedProfileImage.getAbsolutePath());
                                    Bitmap profilePath = null;
                                    if (compressedProfileImage != null) {
                                        profilePath = BitmapFactory.decodeFile(compressedProfileImage.getAbsolutePath());
                                    }
                                    Bitmap signPath = null;
                                    if (compressedSignImage != null) {
                                        signPath = BitmapFactory.decodeFile(compressedSignImage.getAbsolutePath());

                                    }
                                    uploadImage(profilePath,
                                            signPath, memberCode);
                                    PopupClass.showPopUpWithTitleMessageOneButton(InsertNewMember.this, "Ok", Status, "Member Code is : ", memberCode, new PopupCallBackOneButton() {
                                        @Override
                                        public void onFirstButtonClick() {
                                            finish();
                                        }
                                    });
                                } else {
                                    PopupClass.showPopUpWithTitleMessageOneButton(InsertNewMember.this, "Ok", Status, "", "", new PopupCallBackOneButton() {
                                        @Override
                                        public void onFirstButtonClick() {

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

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("BCode", userClass.getGlobalBCode());
                    params.put("FormNo", formNo);
                    params.put("MemberName", name);
                    params.put("FPrefix", pref);
                    params.put("Father", father);
                    params.put("Address", address);
                    params.put("PIN", pin);
                    params.put("MemberDOB", dob);
                    params.put("Age", age);
                    params.put("Nominee", nomineeName);
                    if (nomineeName.equals("")) {
                        params.put("NAge", "0");
                    } else {
                        params.put("NAge", nomineeAge);
                    }
                    params.put("NRelation", nomineeRelation);
                    params.put("Sex", gender);
                    params.put("Phone", phone);
                    params.put("email", email);
                    params.put("PAN", pan);
                    params.put("AADHARNO", aadhar);
                    params.put("AccNo", bankAcNo);
                    params.put("BankName", bankName);
                    params.put("IFSC", bankIfsc);
                    params.put("Idproof", idProof);
                    params.put("IdproofDocNo", idProofDocNo);
                    params.put("SponsorCode", sponsorCode);
                    params.put("DateOfJoin", doj);
                    params.put("RegAmt", rgsAmount);
                    params.put("ShareAmount", "0");
                    params.put("NoOfShare", "0");
                    params.put("PayType", paytype);
                    params.put("UserId", userClass.getGlobalUserCode());
                    params.put("SBAccount", getsbAccount);
                    System.out.println("PUT_Member_InsertMEMBER" + params.toString());
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
//            PopupClass.showPopUpWithTitleMessageOneButton(InsertNewMember.this, "Ok", "", String.valueOf(R.string.Error_Internet), "", new PopupCallBackOneButton() {
//                @Override
//                public void onFirstButtonClick() {
//                }
//            });
            if (nomineeName.equals("")) {
                nomineeAge = "0";
                GlobalBCode = userClass.getGlobalBCode();
                GlobalUserCode = userClass.getGlobalUserCode();
                db.memberInsert(GlobalBCode, formNo, name, pref, father, address, pin, dob, age, nomineeName, nomineeAge, nomineeRelation, gender, phone, email, pan, aadhar, bankAcNo, bankName, bankIfsc, idProof, idProofDocNo, sponsorCode, doj, rgsAmount, "0", "0", paytype, GlobalUserCode, getsbAccount, INSERMEMBER_NOT_SYNCED_WITH_SERVER);
            } else {
                GlobalBCode = userClass.getGlobalBCode();
                db.memberInsert(GlobalBCode, formNo, name, pref, father, address, pin, dob, age, nomineeName, nomineeAge, nomineeRelation, gender, phone, email, pan, aadhar, bankAcNo, bankName, bankIfsc, idProof, idProofDocNo, sponsorCode, doj, rgsAmount, "0", "0", paytype, GlobalUserCode, getsbAccount, INSERMEMBER_NOT_SYNCED_WITH_SERVER);
            }
        }

    }

    private void serviceForRelationList() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceConnector.base_URL + "GET_Config_RelationList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("GET_Config_RelationList" + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("Error_Code") == 0) {
                                JSONArray jsonArray = jsonObject.getJSONArray("RelationList");

                                relationDataList.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    relationData = new WCRelationData();
                                    relationData.setRelation(object.optString("Relation"));

                                    relationDataList.add(relationData);
                                }

                                listRelation.clear();
                                for (int i = 0; i < relationDataList.size(); i++) {
                                    listRelation.add(relationDataList.get(i).getRelation());
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(InsertNewMember.this, R.layout.spinner_item, listRelation);
                                spin_nominee_rel.setAdapter(spinnerArrayAdapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void serviceForRegisAmount() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceConnector.base_URL + "GET_Config_MemberRegAmountList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("Error_Code") == 0) {
                                JSONArray jsonArray = jsonObject.getJSONArray("RegAmount");

                                regisAmountList.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    regisAmount = new WCRegisAmount();
                                    regisAmount.setValue(object.optString("Value"));

                                    regisAmountList.add(regisAmount);
                                }

                                listRegisAmount.clear();
                                for (int i = 0; i < regisAmountList.size(); i++) {
                                    listRegisAmount.add(regisAmountList.get(i).getValue());
                                }
                                //  listRegisAmount.add("10000000");
                                ArrayAdapter<String> spinnerRgsAmount = new ArrayAdapter<String>(InsertNewMember.this, R.layout.spinner_item, listRegisAmount);
                                spin_rgs_amount.setAdapter(spinnerRgsAmount);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateDateFormat() {
        String myformat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tv_dob.setText(sdf.format(myDob.getTime()));
        tv_age.setText(Integer.toString(calculateAge(myDob.getTimeInMillis())));
    }

    private int calculateAge(long date) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }

    private void uploadImage(final Bitmap bitmapProfile, final Bitmap bitmapSign, final String memberCode) {
        String bitProfileImage = "";
        String bitSignImage = "";
        if (bitmapProfile != null) {
            bitProfileImage = BitMapToString(bitmapProfile);
        }
        if (bitmapSign != null) {
            bitSignImage = BitMapToString(bitmapSign);
        }

        customProgressDialog.showLoader();
        String finalBitProfileImage = bitProfileImage;
        String finalBitSignImage = bitSignImage;
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_InsertBPics", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customProgressDialog.dismissDialog();
                if (compressedProfileImage != null && compressedSignImage != null) {
                    if (compressedProfileImage.exists()) {
                        compressedProfileImage.delete();

                    }
                    if (compressedSignImage.exists()) {
                        compressedSignImage.delete();
                    }
                }
                Toast.makeText(InsertNewMember.this, "Image Upload Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.dismissDialog();
                Toast.makeText(InsertNewMember.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("MemberCode", memberCode);
                map.put("Pics64", finalBitProfileImage);
                map.put("Sign64", finalBitSignImage);
                map.put("MDoc64", "");
                map.put("MFingerPic64", stringFinger);
                System.out.println("PUT_InsertBPics"+map.toString());
                return map;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(120000, 2, 0));
        Volley.newRequestQueue(InsertNewMember.this).add(str);
    }

    private Bitmap convertBytearrayToBitmap(byte[] fingerData) {
        return BitmapFactory.decodeByteArray(fingerData, 0, fingerData.length);
    }

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    public void onDestroy() {
        try{
            if(insertMemberNetworkStateChecker!=null)
                unregisterReceiver(insertMemberNetworkStateChecker);
            if (broadcastReceiver!=null)
                unregisterReceiver(broadcastReceiver);

        }catch(Exception e){}
        super.onDestroy();
    }
    public static class InsertMemberNetworkStateChecker extends BroadcastReceiver {
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
                    Cursor cursor = db.getUnsyncedInsert();
                    int i = 1;
                    if (cursor.moveToFirst()) {
                        do {
                            INSERTMEMBERSYNC(
                                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_INSERTMEMBERID)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_BCODE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_FROMNO)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_MEMBERNO)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_FPREFIX)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_FATHER)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ADDRESS)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PIN)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_MEMBERDOB)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_AGE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NOMINEE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAGE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_MRELATION)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_SEX)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PHONE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_EMAIL)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PAN)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ADDHER)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ACCNO)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_BANKNAME)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_IFSC)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_IDPROOF)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_IDPROOFDOCNO)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_SPONSERCODE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DATEOFJOING)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_REGEMT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_SHAREAMOUNT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_MOOFSHARE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PAYTYPE)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_USERID)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_SBACCOUNT)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_STATUSINSERTMEMBER))

                            );
                             Toast.makeText(context, "Network Loop Insermember "+i,Toast.LENGTH_SHORT).show();
                            i++;

                        } while (cursor.moveToNext());
                    }

                }

            }
        }

        private void INSERTMEMBERSYNC(final int insertmemberid, final String bcode, final String formno, final String memberno,
                                  final String fprefix, final String father, final String address, final String pin, final String memberdob,
                                  final String age, final String nominee, final String nage, final String nrelation, final String sex, final String phone,
                                  final String email, final String pan, final String adherno, final String accno, final String bankname, final String ifsc,
                                  final String idproof, final String idproofdocno, final String sponsercode, final String dateofjoining, final String regemt,
                                  final String shareamount, final String noofshare, final String paytype, final String userid, final String sbaccount, final String statusinsermember) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Member_InsertMEMBER",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("PUT_Member_InsertMEMBERsyncresponse" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                db.updateInsertmemberSyncStatus(insertmemberid, InsertNewMember.INSERMEMBER_SYNCED_WITH_SERVER);
                                context.sendBroadcast(new Intent(InsertNewMember.DATA_SAVED_BROADCAST_INSERTMEMBER));
                                JSONArray jsonArray = jsonObject.getJSONArray("SuccessMsg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String  error_code = object.optString("ErrorCode");
                                    String  memberCode = object.optString("MemberCode");
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
                    params.put("BCode", bcode);
                    params.put("FormNo", formno);
                    params.put("MemberName", memberno);
                    params.put("FPrefix", fprefix);
                    params.put("Father", father);
                    params.put("Address", address);
                    params.put("PIN", pin);
                    params.put("MemberDOB", memberdob);
                    params.put("Age", age);
                    params.put("Nominee", nominee);
                    if (nominee.equals("")) {
                        params.put("NAge", "0");
                    } else {
                        params.put("NAge", nage);
                    }
                    params.put("NRelation", nrelation);
                    params.put("Sex", sex);
                    params.put("Phone", phone);
                    params.put("email", email);
                    params.put("PAN", pan);
                    params.put("AADHARNO", adherno);
                    params.put("AccNo", accno);
                    params.put("BankName", bankname);
                    params.put("IFSC", ifsc);
                    params.put("Idproof", idproof);
                    params.put("IdproofDocNo", idproofdocno);
                    params.put("SponsorCode", sponsercode);
                    params.put("DateOfJoin", dateofjoining);
                    params.put("RegAmt", regemt);
                    params.put("ShareAmount", shareamount);
                    params.put("NoOfShare", noofshare);
                    params.put("PayType", paytype);
                    params.put("UserId", userid);
                    params.put("SBAccount", sbaccount);
                    System.out.println("PUT_Member_InsertMEMBERsync" + params.toString());
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }



    }
}