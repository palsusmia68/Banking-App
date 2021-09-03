package com.cmb_collector.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.cmb_collector.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText edit_mobile;
    private AutoCompleteTextView edit_code;
    private LinearLayout llout_otp;
    private EditText edit_otp;
    private TextView tv_resendotp;
    private Button btn_register;
    private String OTP="";
    private ArrayList<String> SuggestionString;
    private WCUserClass userClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        init();
        setListener();
    }
    private void  init(){
        edit_mobile=findViewById(R.id.edit_mobile);
        edit_code=findViewById(R.id.edit_code);
        llout_otp=findViewById(R.id.llout_otp);
        edit_otp=findViewById(R.id.edit_otp);
        tv_resendotp=findViewById(R.id.tv_resendotp);
        btn_register=findViewById(R.id.btn_register);
        llout_otp.setVisibility(View.GONE);
        userClass = WCUserClass.getInstance();
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
    }

    private void setListener(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String messageText = "Your OTP for Forget Password is "+ OTP +". Thank you for Connecting with CFC Purnea Nidhi Limited.";
//                Utility.sendSMS(ForgotPassActivity.this,"8250806960", messageText);
                if (llout_otp.getVisibility()== View.GONE){

                        if (edit_code.getText().toString().trim().equals("")){
                            edit_code.setError("Enter code");
                        }else if(edit_mobile.getText().toString().trim().equals("")){
                            edit_mobile.setError("Enter Mobile");
                        }else {
                            serviceForGetOtp();
                        }
                }else {
                    if (edit_otp.getText().toString().trim().equals("")){
                        edit_otp.setError("Enter OTP");
                    }else {
                        if (edit_otp.getText().toString().trim().equals(OTP)) {
                            Intent intent =new Intent(ForgotPassActivity.this,ChangePassActivity.class);
                            intent.putExtra("checkfrom","1");
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        }
                    }
                }
            }
        });

        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String messageText = "Your OTP for Forget Password is "+ OTP +". Thank you for Connecting with CFC Purnea Nidhi Limited.";
//                Utility.sendSMS(ForgotPassActivity.this,"8250806960", messageText);
                serviceForGetOtp();
            }
        });
    }
    private void serviceForGetOtp() {
        final CustomProgressDialog dialog = new CustomProgressDialog(ForgotPassActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Config_Generate_OTP", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray=object.optJSONArray("OTPStatus");
                    String ErrorCode="";
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        ErrorCode=jsonObject.optString("ErrorCode");
                        OTP=jsonObject.getString("OTP");
                    }

                    if (ErrorCode.equals("0")){

                        String messageText = "Your OTP for Forget Password is "+ OTP +". Thank you for Connecting with CFC Purnea Nidhi Limited.";
                        Utility.sendSMS(ForgotPassActivity.this, edit_mobile.getText().toString().trim(), messageText);
                        llout_otp.setVisibility(View.VISIBLE);

                        userClass.setGlobalUserCode(edit_code.getText().toString().trim());

                    }else {
                        PopupClass.showPopUpWithTitleMessageOneButton(ForgotPassActivity.this, "OK", "Error !!!", "OTP generation failed... ", "", new PopupCallBackOneButton() {
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
                params.put("UserCode", edit_code.getText().toString().trim());
                params.put("UserPhone", edit_mobile.getText().toString().trim());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);

    }
}
