package com.cmb_collector.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cmb_collector.BuildConfig;
import com.cmb_collector.R;
import com.cmb_collector.database.DatabaseHelper;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.GPSTracker;
import com.cmb_collector.services.MyService;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AsteriskPasswordTransformationMethod;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.HideSoftKeyboard;
import com.cmb_collector.utility.ShowSoftKeyboard;
import com.cmb_collector.utility.Utility;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class LogActivity extends AppCompatActivity implements LocationListener {
    public static final String KEY_TYPE = "MemberType";
    public static final String KEY_MEMBERCODE = "MemberCode";
    public static final String KEY_MPIN = "MPIN";
    char[] code = new char[4];
    HideSoftKeyboard hsk;
    ShowSoftKeyboard ssk;
    AsteriskPasswordTransformationMethod passtransform;
    private Button login_btn;
    private RadioButton radio_member, radio_agent;
    private AutoCompleteTextView edit_code;
    private EditText mPinHiddenEditText;
    private TextView tvShow, signup_btn;
    private String radioValue = "";
    private String LOGIN_PIN;
    private int flag = 0;
    private int whoHasFocus;
    private String user_input_type = "";
    private String user_input_code, user_input_password;
    private String GTYPE, GUSERCODE, GBRANCHCODE;
    private String gusername;
    private TextView txt_wrongpass;
    private ProgressDialog progressDialog;
    private ProgressBar login_progressBar;
    private RelativeLayout rl_login_content;
    private Animation animLoginBtn;
    private Dialog dialog;
    private ImageView pop_img_close;
    private ImageView imageView;
    private TextView tv_forgot;
    private WCUserClass userClass;
    private List<WCUserClass> userClassList = new ArrayList<>();
    private Animator animator;
    Pinview pinview;
    private CustomProgressDialog customProgressDialog;
    private ArrayList<String> SuggestionString;
    private int checkID=0;
    private TextView tv_fgpass;
    private  int textCount=0;
    Intent myIntent = null;
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
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initView();
        init();
        restoreValuesFromBundle(savedInstanceState);
        startLocationButtonClick();
        checkServiceStatus();
        //UnderLine TextView SignUp:
        SpannableString content = new SpannableString("Sign Up");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        signup_btn.setText(content);
        setListener();
        animLoginBtn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_loginbtn);
        SharedPreferences preferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        if (preferences.getAll().size() > 0) {
            SuggestionString=new ArrayList<>();
            for (int i=0;i<preferences.getAll().size();i++){
                SuggestionString.add(preferences.getString(String.valueOf(i),""));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, SuggestionString);
            edit_code.setAdapter(adapter);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(LogActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LogActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(LogActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
            }
        }
        else{
        }
//        db.updateRD();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        db = new DatabaseHelper(this);

        serviceForLoadPlanTable("RD");
     //   Relationdata();
        serviceForRegAmountList();
          }

public void Relationdata() {
    Cursor res = db.getRelationData();
    if(res.getCount() == 0) {
        System.out.println("GET_Policy_LoadPTable"+"in Relationdata");
        Log.e("Error","Nothing DIS found");
        serviceForRelationList();
        serviceForRegisAmount();
        //db.INSDRTFD("1003","RD","DRD");
        serviceForLoadPlanCode();
        serviceForLoadPlanCode1();
        serviceForLoadPlanCode2();
        serviceForLoadPlanTable("RD");
       // serviceForLoadPlanTable("DRD");
        serviceForLoadPlanTable1("FD");
        serviceForLoadPlanTable2("MIS");
        serviceForLoadPolicyMode("RD-12");
        serviceForLoadPolicyMode1("FD-12");
        serviceForLoadPolicyMode2("MIS-60");
        return;
    }
    StringBuffer buffer = new StringBuffer();
    while (res.moveToNext()) {
        buffer.append("relationid :"+ res.getString(0)+"\n");
        buffer.append("relationlist :"+ res.getString(1)+"\n");
    }
    Log.e("Datasp",buffer.toString());
}
    private void serviceForRelationList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceConnector.base_URL + "GET_Config_RelationList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("GET_Config_RelationList"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("Error_Code") == 0) {
                                JSONArray jsonArray = jsonObject.getJSONArray("RelationList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                     String relationcmblist = object.getString("Relation");
                                     db.relationInsert(relationcmblist);
                                     System.out.println("GET_Config_RelationList"+relationcmblist);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    String errorMsg = "Server Error may be time out or there is no connection. Please try after some time";
                    showAlert(errorMsg);
                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    String errorMsg = "Authentication Failure while performing the request. Please try after some time";
                    showAlert(errorMsg);
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    String errorMsg = "server responded with a error response. Please try after some time";
                    showAlert(errorMsg);
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    String errorMsg = "there was network error while performing the request. Please try after some time";
                    showAlert(errorMsg);
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    String errorMsg = "the server response could not be parsed. Please try after some time";
                    showAlert(errorMsg);
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showAlert(String errorMsg) {
        //  AlertDialog.Builder builder = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder .setTitle("Error");

        //Setting message manually and performing action on button click
        builder.setMessage(errorMsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        // Toast.makeText(getApplicationContext(),"you choose yes action for alertbox", Toast.LENGTH_SHORT).show();
                    }
                });
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Action for 'NO' Button
//                        dialog.cancel();
//                       // Toast.makeText(getApplicationContext(),"you choose no action for alertbox", Toast.LENGTH_SHORT).show();
//                    }
//                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error");
        alert.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        imageView = findViewById(R.id.imageView);
        animator = AnimatorInflater.loadAnimator(LogActivity.this, R.animator.flip_anim);
        animator.setTarget(imageView);
        animator.start();
    }
    private void initView() {
        tv_fgpass = findViewById(R.id.tv_fgpass);
        signup_btn = findViewById(R.id.signup_btn);
        login_btn = findViewById(R.id.login_btn);
        radio_member = findViewById(R.id.radio_member);
        radio_agent = findViewById(R.id.radio_agent);
        edit_code = findViewById(R.id.edit_code);
        mPinHiddenEditText = findViewById(R.id.pin_hidden_edittext);
        tvShow = findViewById(R.id.tvShow);
        rl_login_content = findViewById(R.id.rl_login_content);
        txt_wrongpass = findViewById(R.id.txt_wrongpass);
        tv_forgot = findViewById(R.id.tv_forgot);
        pinview = findViewById(R.id.pinview);
        pinview.setPassword(true);
        customProgressDialog = new CustomProgressDialog(LogActivity.this);
        userClass = WCUserClass.getInstance();
    }
    private void checkingLogin(Context context) {
        //user_input_type = radioValue;
        user_input_code = edit_code.getText().toString().trim();
        //  user_input_password = et1.getText().toString().trim() + et2.getText().toString().trim() + et3.getText().toString().trim() + et4.getText().toString().trim();
        user_input_password = pinview.getValue().toString().trim();
        if (user_input_code.isEmpty()) {
            Toast.makeText(this, "Pls. put the User Code", Toast.LENGTH_LONG).show();
            // edit_code.setError("Enter User Code");
        } else if (user_input_password.isEmpty()) {
            Toast.makeText(this, "Pls. put the password", Toast.LENGTH_LONG).show();
        } else {
            customProgressDialog.showLoader();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Login_LoginUser",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("response"+response);
                            customProgressDialog.dismissDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("UserDetails");
                                userClassList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    if (object.getInt("IsSuccess") == 1) {
                                        userClass.setGlobalBCode(object.optString("GlobalBCode"));
                                        userClass.setGlobalBranchName(object.optString("GlobalBranchName"));
                                        userClass.setGlobalUserCode(object.optString("GlobalUserCode"));
                                        userClass.setGlobalUserName(object.optString("GlobalUserName"));
                                        userClass.setUserType(object.optString("UserType"));
                                        userClass.setCashLimit(object.optString("CashLimit"));
                                        userClass.setChequeLimit(object.optString("ChequeLimit"));
                                        userClass.setGlobalMemberCode(object.optString("GlobalMemberCode"));
                                        userClass.setMPINsave(user_input_password);
                                        userClass.setFlag(object.optString("Flag"));
                                        userClassList.add(userClass);
                                        //todo
                                        SharedPreferences pppreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                        if (pppreferences.getAll().size() > 0) {
                                            for (int j=0;j<pppreferences.getAll().size();j++) {
                                                if (user_input_code.equals(pppreferences.getString(String.valueOf(j), null))){
                                                  /*  SharedPreferences.Editor myEdit = pppreferences.edit();
                                                    myEdit.putString(String.valueOf(pppreferences.getAll().size()), user_input_code);
                                                    myEdit.apply();*/
                                                    checkID=1;
                                                    break;
                                                }else {
                                                    checkID=0;
                                                }
                                            }
                                            if(checkID==0){
                                                SharedPreferences.Editor myEdit = pppreferences.edit();
                                                myEdit.putString(String.valueOf(pppreferences.getAll().size()), user_input_code);
                                                myEdit.apply();
                                            }
                                        }else {
                                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                            myEdit.putString(String.valueOf(0),user_input_code);
                                            myEdit.apply();
                                        }
                                        LocationData(object.optString("GlobalUserCode"));
                                        serviceForSbAccountList(object.optString("GlobalUserCode"));
                                        Intent login_intent = new Intent(LogActivity.this, MainActivity.class);
                                        startActivity(login_intent);
                                        finish();
                                        overridePendingTransition(R.anim.fade_click_in, R.anim.fade_click_out);
                                    }
                                    else
                                    {
                                        ///    login_btn.setVisibility(View.VISIBLE);
                                        mPinHiddenEditText.setText("");
                                       /* login_btn.setBackgroundResource(R.drawable.xml_btn_back);
                                        login_btn.setEnabled(false);*/
                                        txt_wrongpass.setVisibility(View.VISIBLE);
                                        //tvShow.setEnabled(false);
                                        //Toast.makeText(LogActivity.this, "Please enter Valid Usercode and Mpin", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // login_progressBar.setVisibility(View.GONE);
                    customProgressDialog.dismissDialog();
                    //   login_btn.setVisibility(View.VISIBLE);
                    mPinHiddenEditText.setText("");
                    // login_btn.setBackgroundResource(R.drawable.xml_btn_back);
                    //  login_btn.setEnabled(false);
                    Toast.makeText(LogActivity.this, "Internet not connected", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_TYPE, "Agent");
                    params.put(KEY_MEMBERCODE, user_input_code);
                    params.put(KEY_MPIN, user_input_password);
                    System.out.println("params..." + params);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 1, 0));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    public void LocationData(String GlobalUserCode){
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("HH:mm");
        String time = simpledateformat.format(calander.getTime());
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
                                    myIntent = new Intent(LogActivity.this, MyService.class);
                                    startService(myIntent);
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
                params.put("ArrangerCode", GlobalUserCode);
                params.put("Edate", Utility.setCurrentDate());
                params.put("ETime", time);
                params.put("EType", "I");
                params.put("Lat", lat);
                params.put("Lang", lng);
                params.put("Adrs", location);
                System.out.println("PUT_InsertLocation" + params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 1, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void setListener() {
        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textCount++;
                String ed = edit_code.getText().toString();
                System.out.println("text Count "+ed.length());
                if (ed.length()==10){
                    edit_code.setFocusable(false);
                    edit_code.setEnabled(false);
                    edit_code.setCursorVisible(false);
                    edit_code.setKeyListener(null);
                    //edit_code.setBackgroundColor(Color.TRANSPARENT);
                    //  Toast.makeText(getApplicationContext(),"can not enter ",Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_fgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LogActivity.this,ForgotPassActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogActivity.this, OneTimeRegistration.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ddddd"+Sprovider);
                if (Sprovider.equals("network")){
                    checkingLogin(LogActivity.this);
                    System.out.println("ddddd"+Sprovider+"0");
                }
                else {
                    init();
                    startLocationButtonClick();
                    checkServiceStatus();
                }
                if (mCurrentLocation != null) {
                    checkingLogin(LogActivity.this);
                }

            }
        });
        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    flag = 1;
                    pinview.setPassword(false);
                    tvShow.setText("Hide");
                } else {
                    flag = 0;
                    pinview.setPassword(true);
                    tvShow.setText("Show");
                }
            }
        });
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForgotPasswordPopUp();
            }
        });
     /* pinview.setOnKeyListener(new View.OnKeyListener() {
          @Override
          public boolean onKey(View v, int keyCode, KeyEvent event) {
              if (pinview.getValue().length()>0){
                  tvShow.setEnabled(true);
              }
              else{
                  tvShow.setEnabled(false);
              }
              return false;
          }
      });*/
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                hideKeyboard(LogActivity.this);
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void getForgotPasswordPopUp() {
        dialog = new Dialog(LogActivity.this);
        dialog.setContentView(R.layout.xml_forgot_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_img_close = dialog.findViewById(R.id.pop_img_close);
        pop_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
    private void checkServiceStatus() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(LogActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {
            } else {
                ActivityCompat.requestPermissions(LogActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please enable services to get gps", Toast.LENGTH_LONG).show();
                }
            }


        }
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            Log.e("ODINLOCATIONOFFLINEE"," "+addresses);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                location = strReturnedAddress.toString();
                System.out.println("ODINLOCATIONOFFLINEE"+location);

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                System.out.println("LOCATIONCURRENT"+mCurrentLocation+" "+" "+mLastUpdateTime);
                updateLocationUI();
            }
        };
        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
                System.out.println("is_requesting_updates"+mRequestingLocationUpdates);
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
                System.out.println("is_requesting_updates1"+mCurrentLocation);
            }
            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
                System.out.println("is_requesting_updates2"+mLastUpdateTime);
            }
        }
        updateLocationUI();
    }
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            //Toast.makeText(getApplicationContext(),mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
            Log.e("Last Update" ,mLastUpdateTime);
            lat = String.valueOf(mCurrentLocation.getLatitude());
            lng = String.valueOf(mCurrentLocation.getLongitude());
            getCompleteAddressString(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LogActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(LogActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                        System.out.println("DATALOCATION000"+response);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            System.out.println("DATALOCATION00"+response.isPermanentlyDenied());
                            openSettings();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //   Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//                        Log.e(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
//                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;

    }
    @Override
    public void onBackPressed() {

    }
    @Override
    public void onResume(){
        super.onResume();
        System.out.println("Inside onResume");
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }
        updateLocationUI();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        System.out.println("Inside onReStart");

    }
    @Override
    public void onPause(){
        super.onPause();
        System.out.println("Inside onPause");
        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        lat = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());
        Log.e("ODINLOCATIONOFFLINE",location.getLatitude()+" "+location.getLongitude());
        getCompleteAddressString(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("DATALOCATION"+provider+" "+status+" "+extras);
    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("DATALOCATION1"+provider);
        Sprovider  = provider;
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("DATALOCATION2"+provider+"NetWorkok");
    }
///////////////////////////////////////////////////////////////////////////////////
private void serviceForSbAccountList(String UserCode) {
    StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_GetCollectorSbAccountBalence",
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
                            System.out.println("Object" + object.optString("AccountNo"));
//                            String accountno = object.optString("AccountNo").replace("","NA");
//                            String BALENCE = object.getString("BALENCE").replace("","NA");
                            String accountno = object.optString("AccountNo");
                            String BALENCE = object.getString("BALENCE");
                            System.out.println("AccountNo"+accountno);
                                Cursor res = db.getAccountnumber();
                                if(res.getCount() == 0) {
                                    Log.e("Error","Nothing DIS found");
                                    db.accountInsert(accountno,BALENCE);
                                    return;
                                }
                                StringBuffer buffer = new StringBuffer();
                                while (res.moveToNext()) {
                                    buffer.append("accountnumberid :"+ res.getString(0)+"\n");
                                    buffer.append("accountnumberlist :"+ res.getString(1)+"\n");
                                }
                                Log.e("Object",buffer.toString());
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
            params.put("CollectorCode", UserCode);
            // System.out.println("APP_SB_AccountNo" + params.toString());
            return params;
        }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
  }

    private void serviceForRegisAmount() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServiceConnector.base_URL + "GET_Config_MemberRegAmountList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("GET_Config_MemberRegAmountList"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("Error_Code") == 0) {
                                JSONArray jsonArray = jsonObject.getJSONArray("RegAmount");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String registeramount = object.getString("Value");
                                    System.out.println("GET_Config_MemberRegAmountList"+object);
                                    db.RegisteramountInsert(registeramount);
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
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void serviceForRegAmountList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Config_AgentRegAmountList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        JSONArray array = object.getJSONArray("RegAmount");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String rgistrationamount = jsonObject.getString("Value");
                            db.AgentRegAmountListInsert(rgistrationamount);
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
                //            params.put("SponsorCode", userDetailsData.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);

    }

    private void serviceForLoadPlanCode() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPlanCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPlanCode1"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PlanCode");
                    for (int i = 0; i < array.length(); i++) {
                        // JSONObject jsonObject = array.getJSONObject(i);
                        // String RD = jsonObject.getString("RD");
                       //  String DRD = jsonObject.getString("DRD");
                         System.out.println("DDD"+array.optString(i));
                        db.INSDRTFD("1003",array.optString(i),"DRD");
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
                params.put("LCode", "1003");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadPlanCode1() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPlanCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PlanCode");
                    for (int i = 0; i < array.length(); i++) {
                    //    JSONObject jsonObject = array.getJSONObject(i);
                       // String FD = jsonObject.getString("FD");
                       // System.out.println("DDD1"+FD);
                       // db.INSDRRD("1002",array.optString(i));

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
                params.put("LCode", "1002");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
    private void serviceForLoadPlanCode2() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPlanCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPlanCode"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PlanCode");
                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject jsonObject = array.getJSONObject(i);
//                        String MIS = jsonObject.getString("MIS");
                       System.out.println("DDD2"+array.optString(i));
                        db.INSDRMIS("1005",array.optString(i));

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
                params.put("LCode", "1005");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadPlanTable(final String planCode) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPTable", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPTable"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PlanCodeDetails");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        String STable = object1.optString("STable");
                        String MinAmount = object1.optString("MinAmount");
                        String ModeFlag = object1.optString("ModeFlag");
                        db.INSDRRD("RD",STable,MinAmount,ModeFlag);
                       // db.updateRD();

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
                params.put("PlanCode", planCode);
                System.out.println("GET_Policy_LoadPTable"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
    private void serviceForLoadPlanTable1(final String planCode) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPTable", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPTable"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PTable");
                    for (int i = 0; i < array.length(); i++) {
                        db.InsertFD("FD",array.optString(i));
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
                params.put("PlanCode", planCode);
                System.out.println("GET_Policy_LoadPTable"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
    private void serviceForLoadPlanTable2(final String planCode) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPTable", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPTable"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PTable");
                    for (int i = 0; i < array.length(); i++) {
                        db.InsertMIS("MIS",array.optString(i));
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
                params.put("PlanCode", planCode);
                System.out.println("GET_Policy_LoadPTable"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    private void serviceForLoadPolicyMode(final String planTable) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPolicyMode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPolicyMode"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ModeDetails");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String Mode = jsonObject.optString("Mode");
                        String Term = jsonObject.optString("Term");
                        String ROI = jsonObject.getString("ROI");
                        db.ModeRD12(Mode,Term,ROI);
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
                params.put("PTable", planTable);
                System.out.println("GET_Policy_LoadPolicyMode"+planTable);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
    private void serviceForLoadPolicyMode1(final String planTable) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPolicyMode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPolicyMode"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ModeDetails");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String Mode = jsonObject.optString("Mode");
                        String Term = jsonObject.optString("Term");
                        String ROI = jsonObject.getString("ROI");
                        db.ModeFD12(Mode,Term,ROI);
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
                params.put("PTable", planTable);
                System.out.println("GET_Policy_LoadPolicyMode"+planTable);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
    private void serviceForLoadPolicyMode2(final String planTable) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LogActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPolicyMode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPolicyMode"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ModeDetails");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String Mode = jsonObject.optString("Mode");
                        String Term = jsonObject.optString("Term");
                        String ROI = jsonObject.getString("ROI");
                        db.MIS60(Mode,Term,ROI);
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
                params.put("PTable", planTable);
                System.out.println("GET_Policy_LoadPolicyMode"+planTable);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
}
