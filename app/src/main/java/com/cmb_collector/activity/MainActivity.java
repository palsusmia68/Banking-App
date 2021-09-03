package com.cmb_collector.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.cmb_collector.adapter.MyGridAdapter;
import com.cmb_collector.database.DatabaseHelper;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.GPSTracker;
import com.cmb_collector.services.MyService;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.Sp;
import com.cmb_collector.utility.Utility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cmb_collector.database.DatabaseHelper.TABLE_INSERTAGENT;
import static com.cmb_collector.database.DatabaseHelper.TABLE_INSERTMEMBER;
import static com.cmb_collector.database.DatabaseHelper.TABLE_NEWACDATATABLE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_NEWACSTATUS;
import static com.cmb_collector.database.DatabaseHelper.TABLE_PLANTABLE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_STATUSAGENT;
import static com.cmb_collector.database.DatabaseHelper.TABLE_STATUSINSERTMEMBER;

public class MainActivity extends AppBaseClass{
    public static boolean active = false;
    private GridView first_gridView;
    private List<Integer> listImages = new ArrayList<>();
    private List<String> listTexts = new ArrayList<>();
    private MyGridAdapter gridAdapter;
    private String gtype, gusercode;
    private String gusername;
    private String getAccountNumber,getpAccountNumber;
    private TextView title,txt_aval_bal;
    private TextView txt_turn_on, txt_turn_off;
    private LinearLayout li_ava_balance;
    private LinearLayout li_view_balance;
    private LinearLayout li_footer;
    private ImageView image_view;
    private ImageView img_home;
    List<String> listMenuImgItem = new ArrayList<>();
    List<String> listMenuTitleItem = new ArrayList<>();
    private WCUserClass userClass;
    boolean doubleBackToExitPressedOnce = false;
    final static String KEY_INT_FROM_SERVICE = "KEY_INT_FROM_SERVICE";
    final static String KEY_STRING_FROM_SERVICE = "KEY_STRING_FROM_SERVICE";
    final static String ACTION_UPDATE_CNT = "UPDATE_CNT";
    final static String ACTION_UPDATE_MSG = "UPDATE_MSG";
    final static String KEY_MSG_TO_SERVICE = "KEY_MSG_TO_SERVICE";
    final static String ACTION_MSG_TO_SERVICE = "MSG_TO_SERVICE";
    MyMainReceiver myMainReceiver;
    RelativeLayout lv;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    private String mLastUpdateTime;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    String lat,lng,location;
    LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private int PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    String Sprovider = "";
    Calendar calander;
    SimpleDateFormat simpledateformat;
    Intent myIntent = null;
    private InsertNewMember.InsertMemberNetworkStateChecker insertMemberNetworkStateChecker;
    private NewAgentJoining.InsertagentNetworkStateChecker insertagentNetworkStateChecker;
    private NewAccountActivity.InsertnewaccountNetworkStateChecker insertnewaccountNetworkStateChecker;
    DatabaseHelper helper;
    String status = "1";
    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        myMainReceiver = new MyMainReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_UPDATE_CNT);
        intentFilter.addAction(MyService.ACTION_UPDATE_MSG);
        registerReceiver(myMainReceiver, intentFilter);
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(myMainReceiver);
        super.onStop();
        active = false;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_main);
        lv = (RelativeLayout)findViewById(R.id.lv);
        userClass = WCUserClass.getInstance();
        image_view = findViewById(R.id.image_view);
        img_home = findViewById(R.id.img_home);
        img_home.setImageResource(R.drawable.home_click);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backk);

        image_view.setImageBitmap(Utility.getRoundedCornerBitmap(this, ((BitmapDrawable) image_view.getDrawable()).getBitmap(), 100, bitmap.getWidth(), bitmap.getHeight(), true, true, false, false));

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_click_in, R.anim.fade_click_out);

            }
        });

        //fetch user code and type:

        SharedPreferences shared = getSharedPreferences("GLOBALVALUE", Context.MODE_PRIVATE);
        gusercode = shared.getString("LOGCODE", "");
        gusername = shared.getString("USERCODE", "");


        System.out.println("User Type is..." + gtype);
        System.out.println("UserCode is..." + gusercode);

        //Initialize the value:
        title = findViewById(R.id.title);
        txt_aval_bal = findViewById(R.id.txt_aval_bal);
//        txt_turn_on = findViewById(R.id.txt_turn_on);
//        txt_turn_off = findViewById(R.id.txt_turn_off);

        li_ava_balance = findViewById(R.id.li_ava_balance);
        li_footer = findViewById(R.id.li_footer);
        li_view_balance = findViewById(R.id.li_view_balance);

        title.setText(userClass.getGlobalUserName());
        getpAccountNumber = Sp.getInstance(this).getAccountName();
        if (getpAccountNumber!=null){
            //    Toast.makeText(getApplicationContext(),"not null "+getpAccountNumber,Toast.LENGTH_LONG).show();
            getAccountNumber = getpAccountNumber;
            serviceforbalance(getAccountNumber);
        }else {
            serviceForSbAccountList();
            //   Toast.makeText(getApplicationContext(),"null "+getpAccountNumber,Toast.LENGTH_LONG).show();
        }

//        txt_turn_on.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                li_ava_balance.setVisibility(View.VISIBLE);
//                txt_turn_on.setVisibility(View.GONE);
//                txt_turn_off.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        txt_turn_off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                txt_turn_off.setVisibility(View.GONE);
//                li_ava_balance.setVisibility(View.GONE);
//                txt_turn_on.setVisibility(View.VISIBLE);
//
//
//            }
//        });

        li_view_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                li_ava_balance.setVisibility(View.VISIBLE);
                li_view_balance.setVisibility(View.GONE);
            }
        });
        serviceForGetGridList();
        // serviceforbalance();
        //  serviceForSbAccountList();
//        Utility.serviceforbalance(MainActivity.this,userClass.getGlobalUserCode());
//        txt_aval_bal.setText(Utility.Savings_Balance);


        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                turnGPSOn();
                return false;
            }
        });
        startService();
        insertMemberNetworkStateChecker = new InsertNewMember.InsertMemberNetworkStateChecker();
        registerReceiver(insertMemberNetworkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        insertagentNetworkStateChecker = new NewAgentJoining.InsertagentNetworkStateChecker();
        registerReceiver(insertagentNetworkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        insertnewaccountNetworkStateChecker = new NewAccountActivity.InsertnewaccountNetworkStateChecker();
        registerReceiver(insertagentNetworkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        helper = new DatabaseHelper(getApplicationContext());
        String query = "SELECT * FROM " + TABLE_INSERTMEMBER + " where " + TABLE_STATUSINSERTMEMBER + "=" +status;
        String query1 = "SELECT * FROM " + TABLE_NEWACDATATABLE + " where " + TABLE_NEWACSTATUS + "=" +status;
        System.out.println("TABLE_INSERTMEMBER "+query1);
        helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {
              String statuss =  cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_STATUSINSERTMEMBER));
              System.out.println("statuss"+statuss);
              Deletemember(statuss);
            }
            while (cursor.moveToNext());
        }
        ////////////////////////////////////////////////////////////
        helper = new DatabaseHelper(getApplicationContext());
        String query_agent = "SELECT * FROM " + TABLE_INSERTAGENT + " where " + TABLE_STATUSAGENT + "=" +status;
        helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db_agent = helper.getReadableDatabase();
        Cursor cursor_agent = db_agent.rawQuery(query_agent,null);
        if (cursor_agent.moveToFirst()){
            do {
                String statuss =  cursor_agent.getString(cursor_agent.getColumnIndex(DatabaseHelper.TABLE_STATUSAGENT));
                System.out.println("statuss"+statuss);
                Deleteagent(statuss);
            }
            while (cursor_agent.moveToNext());
        }
        ////////////////////////////////////////////////////////////////////////////
        helper = new DatabaseHelper(getApplicationContext());
        String query_newaccount = "SELECT * FROM " + TABLE_NEWACDATATABLE + " where " + TABLE_NEWACSTATUS + "=" +status;
        helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db_newaccount = helper.getReadableDatabase();
        Cursor cursor_newaccount = db_newaccount.rawQuery(query_newaccount,null);
        if (cursor_newaccount.moveToFirst()){
            do {
                String statuss =  cursor_newaccount.getString(cursor_newaccount.getColumnIndex(DatabaseHelper.TABLE_NEWACSTATUS));
                System.out.println("statuss"+statuss);
                Deletenewaccount(statuss);
            }
            while (cursor_newaccount.moveToNext());
        }
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
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
                                getAccountNumber = object.optString("AccountNo");
                                // SBAccountList.add(object.optString("AccountNo"));
                            }
                            serviceforbalance(getAccountNumber);
                            //  ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(RDActivity.this, R.layout.spinner_item, SBAccountList);
                            //  spin_sbAcNo.setAdapter(spinnerSBAmount);
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

    private void serviceforbalance(String getAccountNumber) {
        final CustomProgressDialog dialog = new CustomProgressDialog(MainActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailSavingsBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("savings Balance "+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("Balance");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    /// Savings_Balance= jsonObject.optString("SavngsBalance");
                    txt_aval_bal.setText(jsonObject.optString("SavngsBalance"));

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
                params.put("collectorcode", getAccountNumber);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(str);

    }

    private void serviceForGetGridList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(MainActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Login_MenuList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Login_MenuList"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("MenuList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listMenuImgItem.add(jsonObject.optString("ImgName"));
                        listMenuTitleItem.add(jsonObject.optString("MenuName"));
                        getGridData(listMenuImgItem, listMenuTitleItem, i);
                    }
                } catch (JSONException e) {
                    PopupClass.showPopUpWithTitleMessageOneButton(MainActivity.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {
                            finish();
                            Logout();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
                PopupClass.showPopUpWithTitleMessageOneButton(MainActivity.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {
                        finish();
                        Logout();
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("USerType", getString(R.string.member_type));
                params.put("UserID", userClass.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
    private void Logout(){
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("HH:mm");
        String time = simpledateformat.format(calander.getTime());
        GPSTracker gpsTracker = new GPSTracker(this);
        String stringLatitude = String.valueOf(gpsTracker.latitude);
        String stringLongitude = String.valueOf(gpsTracker.longitude);
        String country = gpsTracker.getCountryName(this);
        String city = gpsTracker.getLocality(this);
        String postalCode = gpsTracker.getPostalCode(this);
        String addressLine = gpsTracker.getAddressLine(this);
        String location = country+" "+city+" "+postalCode+" "+addressLine;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_InsertLocation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("PUT_InsertLocation"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String jsonObject1 = jsonObject.getString("Error_Code");
                            if (jsonObject1.equals("1")){
                                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                                if (gpsTracker.getIsGPSTrackingEnabled()){
//                                    myIntent = new Intent(LogActivity.this, MyService.class);
//                                    startService(myIntent);
                                    userClass.setFlag("0");
                                }
                                else
                                {
                                    gpsTracker.showSettingsAlert();
                                }
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("ArrangerCode", userClass.getGlobalUserCode());
                params.put("Edate", Utility.setCurrentDate());
                params.put("ETime", time);
                params.put("EType", "O");
                params.put("Lat", stringLatitude);
                params.put("Lang", stringLongitude);
                params.put("Adrs", addressLine);
                System.out.println("PUT_InsertLocation" + params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 1, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



//    private void serviceforbalance() {
//        final CustomProgressDialog dialog = new CustomProgressDialog(MainActivity.this);
//        dialog.showLoader();
//        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailSavingsBalance", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println("savings Balance "+response);
//                dialog.dismissDialog();
//                try {
//                    JSONObject object = new JSONObject(response);
//                    JSONArray jsonArray = object.getJSONArray("Balance");
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    String Savings_Balance= jsonObject.optString("SavngsBalance");
//                    txt_aval_bal.setText(Savings_Balance);
//
//                } catch (JSONException e) {
//                    PopupClass.showPopUpWithTitleMessageOneButton(MainActivity.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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
//                PopupClass.showPopUpWithTitleMessageOneButton(MainActivity.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                    @Override
//                    public void onFirstButtonClick() {
//                    }
//                });
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                System.out.println("cllector code "+userClass.getGlobalUserCode());
//                params.put("collectorcode", userClass.getGlobalUserCode());
//                return params;
//            }
//        };
//
//        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(str);
//    }


//    private void serviceforbalance() {
//        final CustomProgressDialog dialog = new CustomProgressDialog(MainActivity.this);
//        dialog.showLoader();
//        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailWalletBalance", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println("wallet Balance "+response);
//                dialog.dismissDialog();
//                try {
//                    JSONObject object = new JSONObject(response);
//                    String Error_Code=object.optString("Error_Code");
//                    if (Error_Code.equals("0")){
//
//                        JSONArray jsonArray = object.getJSONArray("AvailBalance");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            txt_aval_bal.setText(jsonObject.optString("OpenBal"));
//                        }
//
//                    }else {
//
//                        txt_aval_bal.setText("0");
//                    }
//
//                } catch (JSONException e) {
//                    PopupClass.showPopUpWithTitleMessageOneButton(MainActivity.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
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
//                PopupClass.showPopUpWithTitleMessageOneButton(MainActivity.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                    @Override
//                    public void onFirstButtonClick() {
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
//    }

    private void getGridData(List<String> listImg, List<String> listTitle, int i) {
        listImages.add(Utility.getGridImage(listImg.get(i)));
        listTexts.add(listTitle.get(i));

        initSetUpAdapter();
    }

    private void initSetUpAdapter() {

        first_gridView = findViewById(R.id.first_gridView);
        gridAdapter = new MyGridAdapter(this, listImages, listTexts);
        first_gridView.setAdapter(gridAdapter);

        first_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    default:
                        Intent intent = new Intent(getApplicationContext(), Utility.getClassName(listMenuImgItem.get(position)).getClass());
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }
    private class MyMainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MyService.ACTION_UPDATE_CNT)){
                int int_from_service = intent.getIntExtra(MyService.KEY_INT_FROM_SERVICE, 0);
            }else if(action.equals(MyService.ACTION_UPDATE_MSG)){
                String string_from_service = intent.getStringExtra(MyService.KEY_STRING_FROM_SERVICE);
            }
        }
    }
    @Override
    public void onDestroy() {
        myIntent = new Intent(MainActivity.this, MyService.class);
        stopService(myIntent);
        Logout();
        try{
            if (insertMemberNetworkStateChecker!=null)
                unregisterReceiver(insertMemberNetworkStateChecker);
        }catch(Exception e){}
        super.onDestroy();
    }

    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(MainActivity.this, MyService.class);
            startService(myIntent);
            // ContextCompat.startForegroundService(Profile_Activity.this, new Intent(getApplicationContext(), MyService.class));
        }
        else
        {
            gpsTracker.showSettingsAlert();
        }

    }
    private void checkServiceStatus() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

        }
    }
    public void  Deletemember(String statuss){
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PLANTABLE);
        String query = "DELETE  FROM " + TABLE_INSERTMEMBER + " where " + TABLE_STATUSINSERTMEMBER + "=" +statuss;
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor  c = sqlDB.rawQuery(query,null);
        String insertmemberid = "";
        String bcode = "";
        String formno = "";
        String memberno = "";
        String fprefix = "";
        String father = "";
        String address = "";
        String pin = "";
        String memberdob = "";
        String age = "";
        String nominee = "";
        String nage = "";
        String nrelation = "";
        String sex = "";
        String phone = "";
        String email = "";
        String pan = "";
        String adherno = "";
        String accno = "";
        String bankname = "";
        String ifsc = "";
        String idproof = "";
        String idproofdocno = "";
        String sponsercode = "";
        String dateofjoining = "";
        String regemt = "";
        String shareamount = "";
        String noofshare = "";
        String paytype = "";
        String userid = "";
        String sbaccount = "";
        String statusinsermember = "";

        int status = 0;
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                insertmemberid = c.getString(c.getColumnIndex("insertmemberid"));
                bcode = c.getString(c.getColumnIndex("bcode"));
                formno = c.getString(c.getColumnIndex("formno"));
                memberno = c.getString(c.getColumnIndex("memberno"));
                fprefix = c.getString(c.getColumnIndex("fprefix"));
                father = c.getString(c.getColumnIndex("father"));
                address = c.getString(c.getColumnIndex("address"));

                pin = c.getString(c.getColumnIndex("pin"));
                memberdob = c.getString(c.getColumnIndex("memberdob"));
                age = c.getString(c.getColumnIndex("age"));
                nominee = c.getString(c.getColumnIndex("nominee"));
                nage = c.getString(c.getColumnIndex("nage"));
                nrelation = c.getString(c.getColumnIndex("nrelation"));
                sex = c.getString(c.getColumnIndex("sex"));
                phone = c.getString(c.getColumnIndex("phone"));
                email = c.getString(c.getColumnIndex("email"));
                pan = c.getString(c.getColumnIndex("pan"));
                adherno = c.getString(c.getColumnIndex("adherno"));
                accno = c.getString(c.getColumnIndex("accno"));
                bankname = c.getString(c.getColumnIndex("bankname"));
                ifsc = c.getString(c.getColumnIndex("ifsc"));
                idproof = c.getString(c.getColumnIndex("idproof"));
                idproofdocno = c.getString(c.getColumnIndex("idproofdocno"));
                sponsercode = c.getString(c.getColumnIndex("sponsercode"));
                dateofjoining = c.getString(c.getColumnIndex("dateofjoining"));
                regemt = c.getString(c.getColumnIndex("regemt"));
                shareamount = c.getString(c.getColumnIndex("shareamount"));
                noofshare = c.getString(c.getColumnIndex("noofshare"));
                paytype = c.getString(c.getColumnIndex("paytype"));
                userid = c.getString(c.getColumnIndex("userid"));
                sbaccount = c.getString(c.getColumnIndex("sbaccount"));
                statusinsermember = c.getString(c.getColumnIndex("statusinsermember"));
                if (!insertmemberid.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + insertmemberid + "'");
                }
                if (!bcode.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + bcode + "'");
                }
                if (!formno.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + formno + "'");
                }
                if (!memberno.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + memberno + "'");
                }
                if (!fprefix.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + fprefix + "'");
                }
                if (!father.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + father + "'");
                }
                if (!address.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + address + "'");
                }

                if (!pin.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + pin + "'");
                }
                if (!memberdob.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + memberdob + "'");
                }
                if (!age.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + age + "'");
                }
                if (!nominee.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + nominee + "'");
                }
                if (!nage.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + nage + "'");
                }
                if (!nrelation.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + nrelation + "'");
                }
                if (!sex.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + sex + "'");
                }
                if (!phone.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + phone + "'");
                }
                if (!email.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + email + "'");
                }
                if (!pan.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + pan + "'");
                }
                if (!adherno.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + adherno + "'");
                }
                if (!accno.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + accno + "'");
                }
                if (!bankname.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + bankname + "'");
                }

                if (!ifsc.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + ifsc + "'");
                }

                if (!idproof.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + idproof + "'");
                }

                if (!idproofdocno.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + idproofdocno + "'");
                }

                if (!sponsercode.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + sponsercode + "'");
                }

                if (!dateofjoining.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + dateofjoining + "'");
                }

                if (!regemt.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + regemt + "'");
                }

                if (!shareamount.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + shareamount + "'");
                }

                if (!noofshare.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + noofshare + "'");
                }

                if (!paytype.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + paytype + "'");
                }

                if (!userid.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + userid + "'");
                }

                if (!sbaccount.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + sbaccount + "'");
                }

                if (!statusinsermember.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + statusinsermember + "'");
                }
                c.moveToNext();

            }
            c.close();

        }
    }
    public void Deleteagent(String statuss){
        String query = "DELETE  FROM " + TABLE_INSERTAGENT + " where " + TABLE_STATUSAGENT + "=" +statuss;
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor  c = sqlDB.rawQuery(query,null);
        String insertagentid = "";
        String agentbcode = "";
        String memberno = "";
        String aname = "";
        String gname = "";
        String addval = "";
        String adoj = "";
        String ic = "";
        String rank = "";
        String regamount = "";
        String paymode = "";
        String sbaccount = "";
        String username = "";
        String statusagent = "";
        int status = 0;
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                insertagentid = c.getString(c.getColumnIndex("insertagentid"));
                agentbcode = c.getString(c.getColumnIndex("agentbcode"));
                memberno = c.getString(c.getColumnIndex("formno"));
                aname = c.getString(c.getColumnIndex("aname"));
                gname = c.getString(c.getColumnIndex("gname"));
                addval = c.getString(c.getColumnIndex("addval"));
                adoj = c.getString(c.getColumnIndex("adoj"));
                ic = c.getString(c.getColumnIndex("ic"));
                rank = c.getString(c.getColumnIndex("rank"));
                regamount = c.getString(c.getColumnIndex("regamount"));
                paymode = c.getString(c.getColumnIndex("paymode"));
                sbaccount = c.getString(c.getColumnIndex("sbaccount"));
                username = c.getString(c.getColumnIndex("username"));
                statusagent = c.getString(c.getColumnIndex("statusagent"));
                if (!insertagentid.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + insertagentid + "'");
                }
                if (!agentbcode.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + agentbcode + "'");
                }
                if (!memberno.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + memberno + "'");
                }
                if (!aname.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + aname + "'");
                }
                if (!gname.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + gname + "'");
                }
                if (!addval.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + addval + "'");
                }
                if (!adoj.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + adoj + "'");
                }

                if (!ic.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + ic + "'");
                }
                if (!rank.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + rank + "'");
                }
                if (!regamount.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + regamount + "'");
                }
                if (!paymode.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + paymode + "'");
                }
                if (!sbaccount.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + sbaccount + "'");
                }
                if (!username.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + username + "'");
                }
                if (!statusagent.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + statusagent + "'");
                }
                c.moveToNext();
            }
            c.close();
        }
    }

    public void Deletenewaccount(String statuss){
        String query = "DELETE  FROM " + TABLE_NEWACDATATABLE + " where " + TABLE_NEWACSTATUS + "=" +statuss;
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor  c = sqlDB.rawQuery(query,null);
        String newacid = "";
        String newacbranch = "";
        String newacdateofent = "";
        String newacmembercode = "";
        String newacapplicantname = "";
        String newacfather = "";
        String newacaddress = "";
        String newacphoneno = "";
        String newacaccounttype = "";
        String newacjointapplicantname = "";
        String newacjointfather = "";
        String newacjointaddress = "";
        String newacadvisorcode = "";
        String newacamount = "";
        String newacpaymode = "";
        String newacuserid = "";
        String newacsbaccount = "";
        String newacstatus = "";
        int status = 0;
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                newacid = c.getString(c.getColumnIndex("newacid"));
                newacbranch = c.getString(c.getColumnIndex("newacbranch"));
                newacdateofent = c.getString(c.getColumnIndex("newacdateofent"));
                newacmembercode = c.getString(c.getColumnIndex("newacmembercode"));
                newacapplicantname = c.getString(c.getColumnIndex("newacapplicantname"));
                newacfather = c.getString(c.getColumnIndex("newacfather"));
                newacaddress = c.getString(c.getColumnIndex("newacaddress"));
                newacphoneno = c.getString(c.getColumnIndex("newacphoneno"));
                newacaccounttype = c.getString(c.getColumnIndex("newacaccounttype"));
                newacjointapplicantname = c.getString(c.getColumnIndex("newacjointapplicantname"));
                newacjointfather = c.getString(c.getColumnIndex("newacjointfather"));
                newacjointaddress = c.getString(c.getColumnIndex("newacjointaddress"));
                newacadvisorcode = c.getString(c.getColumnIndex("newacadvisorcode"));
                newacamount = c.getString(c.getColumnIndex("newacamount"));
                newacpaymode = c.getString(c.getColumnIndex("newacpaymode"));
                newacuserid = c.getString(c.getColumnIndex("newacuserid"));
                newacsbaccount = c.getString(c.getColumnIndex("newacsbaccount"));
                newacstatus = c.getString(c.getColumnIndex("newacstatus"));
                if (!newacid.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacid + "'");
                }
                if (!newacbranch.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacbranch + "'");
                }
                if (!newacdateofent.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacdateofent + "'");
                }
                if (!newacmembercode.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacmembercode + "'");
                }
                if (!newacapplicantname.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacapplicantname + "'");
                }
                if (!newacfather.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacfather + "'");
                }
                if (!newacaddress.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacaddress + "'");
                }

                if (!newacphoneno.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacphoneno + "'");
                }
                if (!newacaccounttype.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacaccounttype + "'");
                }
                if (!newacjointapplicantname.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacjointapplicantname + "'");
                }
                if (!newacjointfather.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacjointfather + "'");
                }
                if (!newacjointaddress.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacjointaddress + "'");
                }
                if (!newacadvisorcode.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacadvisorcode + "'");
                }
                if (!newacamount.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacamount + "'");
                }
                if (!newacpaymode.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacpaymode + "'");
                }
                if (!newacuserid.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacuserid + "'");
                }
                if (!newacsbaccount.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacsbaccount + "'");
                }
                if (!newacstatus.equals("android_metadata")) {
                    sqlDB.execSQL("DROP TABLE '" + newacstatus + "'");
                }
                c.moveToNext();
            }
            c.close();
        }
    }
}
