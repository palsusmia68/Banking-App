package com.cmb_collector.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.cmb_collector.adapter.MyCircleAdapter;
import com.cmb_collector.adapter.MyOperatorAdapter;
import com.cmb_collector.model.WCOperatorCircleCodeClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;


public class MobileRecharge extends AppBaseClass {

    private ImageView img_mb_rechar, iv_contact;
    private TextView title;
    private EditText edit_mobile, edit_rchrg_ammount, edit_confirm_rchrg_ammount;
    private Button btn_confirm;
    private Spinner spin_operator, spin_circle;

    private List<Integer> operatorLogo = new ArrayList<>();
    private List<String> operatorName = new ArrayList<>();
    private List<String> circleName = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 200;

    private WCOperatorCircleCodeClass codeClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_mobile_recharge);

        initView();
        setDefaults();
        setListener();
        setOperatorAdapter();
        setCircleAdapter();
    }

    private void initView() {
        title = findViewById(R.id.title);
        img_mb_rechar = findViewById(R.id.img_mb_rechar);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_rchrg_ammount = findViewById(R.id.edit_rchrg_ammount);
        edit_confirm_rchrg_ammount = findViewById(R.id.edit_confirm_rchrg_ammount);
        spin_operator = findViewById(R.id.spin_operator);
        spin_circle = findViewById(R.id.spin_circle);
        iv_contact = findViewById(R.id.iv_contact);
        btn_confirm = findViewById(R.id.btn_confirm);

        codeClass = new WCOperatorCircleCodeClass();
    }

    private void setDefaults() {
        title.setText(getIntent().getStringExtra("title"));
        edit_mobile.setText(getIntent().getStringExtra("number"));

    }

    private void setListener() {
        img_mb_rechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        iv_contact.setOnClickListener(loadContactListener);

        btn_confirm.setOnClickListener(confirmListener);
    }

    private void setOperatorAdapter() {
        if (title.getText().toString().equalsIgnoreCase("Mobile Recharge") || title.getText().toString().equalsIgnoreCase("Postpaid Recharge")) {
            operatorLogo.add(R.drawable.vodafone);
            operatorName.add("Vodafone");
            operatorLogo.add(R.drawable.airtel);
            operatorName.add("Airtel");
            operatorLogo.add(R.drawable.jio);
            operatorName.add("Jio");
            operatorLogo.add(R.drawable.idea);
            operatorName.add("Idea");
            operatorLogo.add(R.drawable.bsnl);
            operatorName.add("BSNL");
            operatorLogo.add(R.drawable.docomo);
            operatorName.add("Tata Docomo");
            operatorLogo.add(R.drawable.mts);
            operatorName.add("MTS");
        } else if (title.getText().toString().equalsIgnoreCase("DTH Recharge")) {
            operatorLogo.add(R.drawable.tata_sky);
            operatorName.add("Tata Sky");
            operatorLogo.add(R.drawable.dish_tv);
            operatorName.add("Dish TV");
            operatorLogo.add(R.drawable.airtel);
            operatorName.add("Airtel Digital TV");
            operatorLogo.add(R.drawable.videocon);
            operatorName.add("Videocon D2H");
            operatorLogo.add(R.drawable.sun_direct);
            operatorName.add("Sun Direct");
        } else {
            operatorLogo.add(R.drawable.airtel);
            operatorName.add("Airtel");
            operatorLogo.add(R.drawable.bsnl);
            operatorName.add("BSNL");
            operatorLogo.add(R.drawable.idea);
            operatorName.add("Idea");
            operatorLogo.add(R.drawable.jio);
            operatorName.add("Jio");
            operatorLogo.add(R.drawable.mtnl);
            operatorName.add("MTNL");
            operatorName.add("Vodafone");
        }


        MyOperatorAdapter adapter = new MyOperatorAdapter(MobileRecharge.this, operatorLogo, operatorName);
        spin_operator.setAdapter(adapter);
    }

    private void setCircleAdapter() {
        circleName.add("Andaman And Nicobar");
        circleName.add("Andhra Pradesh");
        circleName.add("Arunachal Pradesh");
        circleName.add("Assam");
        circleName.add("Bihar");
        circleName.add("Chandigarh");
        circleName.add("Chhattisgarh");
        circleName.add("Dadra Nager Haveli");
        circleName.add("Daman And Div");
        circleName.add("Goa");
        circleName.add("Gujarat");
        circleName.add("Haryana");
        circleName.add("Himachal Pradesh");
        circleName.add("Jammu And Kashmir");
        circleName.add("Jharkhand");
        circleName.add("Karnataka");
        circleName.add("Kerala");
        circleName.add("Lakshadweep");
        circleName.add("Madhya Pradesh");
        circleName.add("Maharashtra");
        circleName.add("Manipur");
        circleName.add("Meghalaya");
        circleName.add("Mizoram");
        circleName.add("Nagaland");
        circleName.add("New Delhi");
        circleName.add("Orissa");
        circleName.add("Pondicherry");
        circleName.add("Punjab");
        circleName.add("Rajasthan");
        circleName.add("Sikkim");
        circleName.add("Tamil Nadu");
        circleName.add("Tripura");
        circleName.add("Uttar Pradesh East");
        circleName.add("Uttaranchal");
        circleName.add("West Bengal");
        circleName.add("Uttar Pradesh West");
        circleName.add("Mumbai");
        circleName.add("Delhi");
        circleName.add("CHENNAI");
        circleName.add("NORTH EAST");
        circleName.add("Kolkata");


        MyCircleAdapter adapter = new MyCircleAdapter(MobileRecharge.this, circleName);
        spin_circle.setAdapter(adapter);
    }


    private void serviceForMobileRecharge(final String mobile, final String amount) {
        final CustomProgressDialog dialog = new CustomProgressDialog(MobileRecharge.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.recharge_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optString("status").equalsIgnoreCase("Success")) {
                        Log.d("XXXXXres", response);
                        PopupClass.showPopUpWithTitleMessageOneButton(MobileRecharge.this, "OK", object.optString("status") + "\n" + object.optString("opid"), "Your Transaction Id:" + object.optString("txid"), "Your Order Id:" + object.optString("orderid"), new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                    } else {
                        PopupClass.showPopUpWithTitleMessageOneButton(MobileRecharge.this, "OK", object.optString("status"), "Your Transaction Id:" + object.optString("opid"), "Your Order Id:", new PopupCallBackOneButton() {
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
                Toast.makeText(MobileRecharge.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismissDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", "500150");
                params.put("pwd", "123");
                params.put("circlecode", codeClass.getCircleCodeList(spin_circle.getSelectedItemPosition()));
                params.put("operatorcode", codeClass.getOperatorCodeList(spin_operator.getSelectedItemPosition()));
                params.put("number", mobile);
                params.put("amount", amount);
                params.put("orderid", Utility.getRandomString(8));
                params.put("format", "json");

                Log.d("XXXXXXparam", params.toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    View.OnClickListener loadContactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EnableRuntimePermission();
        }
    };

    private void EnableRuntimePermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS}, PERMISSION_REQUEST_CODE);
    }


    View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utility.checkConnectivity(MobileRecharge.this)) {
                if (checkInput()) {
                    String mobile = edit_mobile.getText().toString().trim();
                    String amount = edit_rchrg_ammount.getText().toString().trim();

                    serviceForMobileRecharge(mobile, amount);
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(MobileRecharge.this, "OK", "Error!!!", "Sorry!!!Internet Connection neededed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        }
    };

    private boolean checkInput() {
        Pattern mobileNoPattern = Pattern.compile("^(\\+91|0?)[6-9][0-9]{9}$");
        Matcher mobileNoMatcher = mobileNoPattern.matcher(edit_mobile.getText().toString());

        if (TextUtils.isEmpty(edit_mobile.getText().toString()) || !(mobileNoMatcher.find() && mobileNoMatcher.group().equals(edit_mobile.getText().toString()))) {
            edit_mobile.setError("Enter valid Mobile Number");
            return false;
        } else if (TextUtils.isEmpty(edit_rchrg_ammount.getText().toString())) {
            edit_rchrg_ammount.setError("Enter valid Recharge Amount");
            return false;
        } else if (!edit_rchrg_ammount.getText().toString().equals(edit_confirm_rchrg_ammount.getText().toString())) {
            edit_confirm_rchrg_ammount.setError("Amount Not matched");
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean contactAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (contactAccepted) {
                        startActivity(new Intent(MobileRecharge.this, LoadContactActivity.class)
                                .putExtra("title", title.getText().toString()));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        finish();
                    } else {
                        Toast.makeText(this, "Permission Denied, You cannot access contacts.", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                                showMessageOKCancel("You need to allow access the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_CONTACTS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MobileRecharge.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
