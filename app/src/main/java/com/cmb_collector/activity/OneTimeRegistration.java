package com.cmb_collector.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.HideSoftKeyboard;
import com.cmb_collector.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OneTimeRegistration extends AppCompatActivity {

    public static final String KEY_TYPE = "MemberType";
    public static final String KEY_MOBILE = "PhoneNo";

    public static final String KEY_MEMBER_CODE = "MemberCode";
    public static final String KEY_MPIN = "MPIN";

    private Intent intent;
    private CustomProgressDialog customProgressDialog;

    private LinearLayout li_mpin_layout, li_regis_layout;

    private String code, name, password;
    private Integer MPIN;

    private EditText mpin1, mpin2, mpin3, mpin4;
    private EditText rmpin1, rmpin2, rmpin3, rmpin4;

    private Button btn_register;
    private RadioButton radio_agent, radio_member, radio_button;

    private ImageView img_regis;

    private Animator animator;

    public String radio_value = "MEMBER";

    private RadioGroup radioGroup;

    private TextView edit_code, edit_name, edit_pass;
    private TextView tv_login;

    private EditText edit_mobile;

    private String CMPIN, RMPIN;
    private String user_input_pass, mobile, value;

    private HideSoftKeyboard hideSoftKeyboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize the field:

        customProgressDialog = new CustomProgressDialog(OneTimeRegistration.this);
        hideSoftKeyboard = new HideSoftKeyboard(OneTimeRegistration.this);

        li_mpin_layout = findViewById(R.id.li_mpin_layout);
        //li_regis_layout = findViewById(R.id.li_regis_layout);

        mpin1 = findViewById(R.id.edit_one_mpin);
        mpin2 = findViewById(R.id.edit_two_mpin);
        mpin3 = findViewById(R.id.edit_three_mpin);
        mpin4 = findViewById(R.id.edit_four_mpin);
        rmpin1 = findViewById(R.id.edit_one_rpin);
        rmpin2 = findViewById(R.id.edit_two_rpin);
        rmpin3 = findViewById(R.id.edit_three_rpin);
        rmpin4 = findViewById(R.id.edit_four_rpin);
        btn_register = findViewById(R.id.btn_register);
        radio_member = findViewById(R.id.radio_member);
        radio_agent = findViewById(R.id.radio_agent);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_code = findViewById(R.id.edit_code);
        edit_name = findViewById(R.id.edit_name);
        edit_pass = findViewById(R.id.edit_pass);
        radioGroup = findViewById(R.id.radio_group);

        tv_login = findViewById(R.id.tv_login);

        int selectedId = radioGroup.getCheckedRadioButtonId();

        radio_button = findViewById(selectedId);

        SpannableString due_content = new SpannableString("Sign In");
        due_content.setSpan(new UnderlineSpan(), 0, due_content.length(), 0);
        tv_login.setText(due_content);

        radio_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_value = "MEMBER";
                LogInType(radio_value);

                radio_member.setChecked(true);
                radio_agent.setChecked(false);

            }
        });

        radio_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_value = "AGENT";
                LogInType(radio_value);

                radio_agent.setChecked(true);
                radio_member.setChecked(false);
            }
        });

        //Create MPIN:-

        mpin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mpin1.getText().toString().length() == 1) {
                    mpin2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mpin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mpin2.getText().toString().length() == 1) {
                    mpin3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mpin3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mpin3.getText().toString().length() == 1) {
                    mpin4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mpin4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mpin4.getText().toString().length() == 1) {
                    rmpin1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Back delete:
        mpin4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mpin3.requestFocus();
                }
                return false;
            }
        });

        mpin3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mpin2.requestFocus();
                }
                return false;
            }
        });

        mpin2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mpin1.requestFocus();
                }
                return false;
            }
        });

        mpin1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mpin1.requestFocus();
                }
                return false;
            }
        });

        //Re-Confirm MPIN:-

        rmpin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rmpin1.getText().toString().length() == 1) {
                    rmpin2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rmpin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rmpin2.getText().toString().length() == 1) {
                    rmpin3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rmpin3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rmpin3.getText().toString().length() == 1) {
                    rmpin4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rmpin4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rmpin4.getText().toString().length() == 1) {
                    btn_register.setEnabled(true);
                    btn_register.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Back Delete:
        rmpin4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    rmpin3.requestFocus();
                }
                return false;
            }
        });

        rmpin3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    rmpin2.requestFocus();
                }
                return false;
            }
        });

        rmpin2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    rmpin1.requestFocus();
                }
                return false;
            }
        });

        rmpin1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    rmpin1.requestFocus();
                }
                return false;
            }
        });

        //Get User Details:
//        edit_mobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                boolean handled = false;
//
//                 if (actionId == EditorInfo.IME_ACTION_SEND)
//                 {
//                    getCodeandName();
//                    handled = true;
//                 }
//                    return handled;
//            }
//        });

        //Get User Details:

        edit_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                count = 10;
                if (edit_mobile.length() == count) {

                    serviceForUserDetails();

//                    if (radio_member.isChecked() == true) {
//                        getDetails();
//                    } else if (radio_agent.isChecked() == true) {
//                        getDetails();
//                    } else {
//                        Toast.makeText(OneTimeRegistration.this, "Pls. select type...", Toast.LENGTH_SHORT).show();
//                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edit_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = password.length();
                user_input_pass = edit_pass.getText().toString().trim();

                System.out.println("count..." + count);

                System.out.println("password..." + password);

                System.out.println("userinput..." + user_input_pass);

                if (edit_pass.length() == count) {
                    if (user_input_pass.equals(password)) {
                        if (MPIN == 0) {
                            hideSoftKeyboard.hideSoftKeyboard((EditText) edit_pass);
                            edit_pass.setCursorVisible(false);
                            li_mpin_layout.setVisibility(View.VISIBLE);

                        } else {
                            hideSoftKeyboard.hideSoftKeyboard((EditText) edit_pass);
                            PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "You Have Already Registered", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                }
                            });
                        }
                    } else {
                        edit_pass.setText("");
                        PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "Wrong Password Entered", "", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {
                            }
                        });
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        System.out.println("cmpin...." + CMPIN);
        System.out.println("rmpin...." + RMPIN);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceForRegisterMPIN();

            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OneTimeRegistration.this, LogActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        img_regis = findViewById(R.id.img_regis);
        animator = AnimatorInflater.loadAnimator(OneTimeRegistration.this, R.animator.flip_anim);
        animator.setTarget(img_regis);
        animator.start();
    }

    private void serviceForUserDetails() {

        mobile = edit_mobile.getText().toString().trim();
        value = radio_value;

        if (mobile.isEmpty()) {
            PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "Please Enter Mobile No.", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        } else {

            if (Utility.checkConnectivity(OneTimeRegistration.this)) {

                customProgressDialog.showLoader();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Regn_SearchNameByPhoneNo",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                customProgressDialog.dismissDialog();

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if (jsonObject.getInt("Error_Code") == 0) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("Regn_Details");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            code = object.getString("Code");
                                            name = object.getString("Name");
                                            password = object.getString("AppPassword");
                                            MPIN = object.getInt("MPin");

                                            edit_code.setText(code);
                                            edit_name.setText(name);
                                            edit_pass.setEnabled(true);
                                            LoginUserCode(code);
                                            UserName(name);
                                            System.out.println("name..." + name);
                                            System.out.println("value..." + value);
                                        }
                                    } else {
                                        PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "Mobile No. Not Registered", "", new PopupCallBackOneButton() {
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
                        params.put("MemberType", "AGENT");
                        params.put("PhoneNo", mobile);

                        System.out.println("params..." + params);

                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "Sorry, No Internet connection found, Please check your network connection", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {
                    }
                });
            }
        }

    }

    private void serviceForRegisterMPIN() {
        //Checking MPIN field empty or not:
        if (mpin1.getText().toString().isEmpty() || mpin2.getText().toString().isEmpty() || mpin3.getText().toString().isEmpty() || mpin4.getText().toString().isEmpty()) {
            PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "Field Cannot be Blank", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        } else if (rmpin1.getText().toString().isEmpty() || rmpin2.getText().toString().isEmpty() || rmpin3.getText().toString().isEmpty() || rmpin4.getText().toString().isEmpty()) {
            PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "Please Confirm the MPIN", "", new PopupCallBackOneButton() {
                @Override
                public void onFirstButtonClick() {
                }
            });
        } else {
            CMPIN = mpin1.getText().toString().trim() + mpin2.getText().toString().trim() + mpin3.getText().toString().trim() + mpin4.getText().toString().trim();
            RMPIN = rmpin1.getText().toString().trim() + rmpin2.getText().toString().trim() + rmpin3.getText().toString().trim() + rmpin4.getText().toString().trim();
            System.out.println("1st field..." + CMPIN);
            System.out.println("2nd field..." + RMPIN);

            if (Utility.checkConnectivity(OneTimeRegistration.this)) {

                customProgressDialog.showLoader();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Regn_InsertRegn",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                customProgressDialog.dismissDialog();

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if (CMPIN.equals(RMPIN)) {
                                        System.out.println("final mpin..." + CMPIN);

                                        if (jsonObject.getInt("Error_Code") == 0) {
                                            Toast.makeText(getApplicationContext(), "MPIN created sucessfully", Toast.LENGTH_SHORT).show();
                                            Intent rgs_intent = new Intent(OneTimeRegistration.this, LogActivity.class);
                                            rgs_intent.putExtra("USERNAME", name);
                                            startActivity(rgs_intent);
                                        }

                                    } else {
                                        PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "MPIN Not Match", "", new PopupCallBackOneButton() {
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
                        Map<String, String> map = new HashMap<>();
                        map.put("MemberType", "AGENT");
                        map.put("MemberCode", code);
                        map.put("MPIN", CMPIN);

                        System.out.println("map..." + map);

                        return map;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(OneTimeRegistration.this, "Ok", "", "Sorry, No Internet connection found, Please check your network connection", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {
                    }
                });
            }
        }
    }

    public void UserName(String name) {
        SharedPreferences.Editor edit = getSharedPreferences("GLOBALVALUE", MODE_PRIVATE).edit();
        edit.putString("KEY_USER_NAME", name);
        edit.apply();
    }

    public void LogInType(String type) {
        SharedPreferences.Editor editor = getSharedPreferences("LOGTYPE", MODE_PRIVATE).edit();
        editor.putString("LOGTYPE", type);
        System.out.println("LOGTYPE for registration Time------->>" + type);
        editor.apply();
    }

    public void LoginUserCode(String code) {
        SharedPreferences.Editor editor = getSharedPreferences("LOGTYPE", MODE_PRIVATE).edit();
        editor.putString("LOGCODE", code);
        System.out.println("LOGCODE for registration Time------->>" + code);
        editor.apply();
    }

    public void StoreMPin() {
        SharedPreferences sharedPref = getSharedPreferences("userMpinDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (CMPIN.equals(RMPIN)) {
            editor.putString("cmpin", CMPIN);
            editor.putString("rmpin", RMPIN);
            System.out.println("CMPIN is.." + CMPIN);
            System.out.println("RMPIN is.." + RMPIN);
            editor.commit();
            Toast.makeText(getApplicationContext(), "MPIN create Successfully...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "MPIN not match...", Toast.LENGTH_LONG).show();
        }

        String check = String.valueOf(sharedPref.contains("cmpin"));

        File f = new File("/data/data/com.wcreditcooperative/shared_prefs/userMpinDetails.xml");


//        if (CMPIN.isEmpty()){
//            Toast.makeText(getApplicationContext(),"Field cannot be blank",Toast.LENGTH_LONG).show();
//        }else if (sharedPref.contains("cmpin")){
//            Toast.makeText(getApplicationContext(),"MPIN already exists...",Toast.LENGTH_LONG).show();
//        }else if (CMPIN.equals(RMPIN)){
//            Toast.makeText(getApplicationContext(),"MPIN saved successfully...",Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(getApplicationContext(),"MPIN not match...",Toast.LENGTH_LONG).show();
//        }


//        if (f.exists())
//            Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
//        else
//            Log.d("TAG", "Setup default preferences");


//         Toast.makeText(getApplicationContext(),"MPIN saved successfully...",Toast.LENGTH_LONG).show();

        mpin1.setText("");
        mpin2.setText("");
        mpin3.setText("");
        mpin4.setText("");

        rmpin1.setText("");
        rmpin2.setText("");
        rmpin3.setText("");
        rmpin4.setText("");

    }


}
