package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import com.cmb_collector.utility.CustomProgressDialog;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassActivity extends AppCompatActivity {

    private Pinview pinviewold,pinviewnew,pinviewRe;
    private TextView tvShowold,tvShownew,tvShowRE;
    private LinearLayout llout_oldpin;
    private Button btn_register;
    private int flagOld = 0;
    private int flagNew = 0;
    private int flagRe= 0;
    private String checkFrom="";
    private WCUserClass userClass;
    String Errorcode="";
    String Status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        init();
        setListener();
    }

    private void init(){

        userClass = WCUserClass.getInstance();

        pinviewold=findViewById(R.id.pinviewold);
        pinviewold.setPassword(true);
        pinviewnew=findViewById(R.id.pinviewnew);
        pinviewnew.setPassword(true);
        pinviewRe=findViewById(R.id.pinviewRe);
        pinviewRe.setPassword(true);
        tvShowold=findViewById(R.id.tvShowold);
        tvShownew=findViewById(R.id.tvShownew);
        tvShowRE=findViewById(R.id.tvShowRE);

        llout_oldpin=findViewById(R.id.llout_oldpin);
        btn_register=findViewById(R.id.btn_register);

        checkFrom=getIntent().getExtras().getString("checkfrom");

        if (checkFrom.equals("1")){
            llout_oldpin.setVisibility(View.GONE);
        }else {
            llout_oldpin.setVisibility(View.VISIBLE);

        }

    }
    private void setListener(){

        tvShowold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagOld == 0) {

                    flagOld = 1;
                    pinviewold.setPassword(false);
                    tvShowold.setText("Hide");

                } else {
                    flagOld = 0;
                    pinviewold.setPassword(true);
                    tvShowold.setText("Show");
                }
            }
        });

        tvShownew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagNew == 0) {

                    flagNew = 1;
                    pinviewnew.setPassword(false);
                    tvShownew.setText("Hide");

                } else {
                    flagNew = 0;
                    pinviewnew.setPassword(true);
                    tvShownew.setText("Show");
                }
            }
        });

        tvShowRE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagRe == 0) {

                    flagRe = 1;
                    pinviewRe.setPassword(false);
                    tvShowRE.setText("Hide");

                } else {
                    flagRe = 0;
                    pinviewRe.setPassword(true);
                    tvShowRE.setText("Show");
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFrom.equals("1")){
                    if (!pinviewnew.getValue().trim().equals("")){
                        if (pinviewnew.getValue().trim().equals(pinviewRe.getValue().trim())){
                            serviceForChangeMPIN();
                        }else {
                            PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "Error !!!", "New Pin didn't match, Please try again... ", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                }
                            });
                        }

                    }else {
                        PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "", "Please Enter PIN ", "", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {
                            }
                        });
                    }
                }else {

                    if (!pinviewold.getValue().trim().equals("")){

                        if (userClass.getMPINsave().equals(pinviewold.getValue().trim())){

                            if (!pinviewnew.getValue().trim().equals("")){
                                if (pinviewnew.getValue().trim().equals(pinviewRe.getValue().trim())){
                                    serviceForChangeMPIN();
                                }else {
                                    PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "Error !!!", "New Pin didn't match, Please try again... ", "", new PopupCallBackOneButton() {
                                        @Override
                                        public void onFirstButtonClick() {

                                        }
                                    });
                                }
                            }else {
                                PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "", "Please Enter PIN ", "", new PopupCallBackOneButton() {
                                    @Override
                                    public void onFirstButtonClick() {
                                    }
                                });
                            }
                        }
                        else
                        {
                            PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "Error !!!", "Old Pin Didn't Match", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }
                    }else {
                        PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "", "Please Enter PIN ", "", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {
                            }
                        });
                    }
                }
            }
        });
    }

    private void serviceForChangeMPIN() {

        final CustomProgressDialog dialog = new CustomProgressDialog(ChangePassActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Config_ChangeMpin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray=object.optJSONArray("MPINStatus");
                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Status=jsonObject.optString("Status");
                        Errorcode=jsonObject.optString("Errorcode");

                    }

                    if (Errorcode.equals("0")){

                        PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "Success !!!", Status, "", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {
                                if (checkFrom.equals("0")) {
                                    userClass.setMPINsave(pinviewnew.getValue().trim());
                                    Intent intent = new Intent(ChangePassActivity.this, UpdateProfile.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                }else {
                                    Intent intent = new Intent(ChangePassActivity.this, LogActivity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                }
                            }
                        });

                    }else {

                        PopupClass.showPopUpWithTitleMessageOneButton(ChangePassActivity.this, "OK", "", "Please Enter PIN ", "", new PopupCallBackOneButton() {
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
                dialog.dismissDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserType", "COLLECTOR");
                params.put("UserCode", userClass.getGlobalUserCode());
                params.put("UserNewMpin", pinviewnew.getValue().trim());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (checkFrom.equals("0")) {
            Intent intent = new Intent(ChangePassActivity.this, UpdateProfile.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }else {
            Intent intent = new Intent(ChangePassActivity.this, LogActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }
}
