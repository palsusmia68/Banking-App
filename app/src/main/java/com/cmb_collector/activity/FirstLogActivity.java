package com.cmb_collector.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmb_collector.R;

public class FirstLogActivity extends AppCompatActivity {
    EditText et1, et2, et3, et4, edit_code;
    TextView tvShow;
    Button  logBtn;
    TextView regBtn;
    private int flag = 0;
    private String radioValue = "";

    private Button signup_btn, login_btn;
    private RadioButton radio_member, radio_agent;
    public static final String KEY_TYPE = "MemberType";
    public static final String KEY_MEMBERCODE = "MemberCode";
    public static final String KEY_MPIN = "MPIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        et1 = findViewById(R.id.edit_one);
        et2 = findViewById(R.id.edit_two);
        et3 = findViewById(R.id.edit_three);
        et4 = findViewById(R.id.edit_four);
        edit_code = findViewById(R.id.edit_code);
        radio_member = findViewById(R.id.radio_member);
        radio_agent = findViewById(R.id.radio_agent);
        regBtn = findViewById(R.id.signup_btn);
        logBtn = findViewById(R.id.login_btn);
        tvShow = findViewById(R.id.tvShow);

        passwordTextChange();

        et1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        et2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        et3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        et4.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    flag = 1;
                    et1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et4.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    flag = 0;
                    et1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    et2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    et3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    et4.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                }
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstLogActivity.this, "Login Click", Toast.LENGTH_SHORT).show();

            }
        });

        radio_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioValue = "MEMBER";
                //LogInType(radioValue);
                radio_member.setChecked(true);
                radio_agent.setChecked(false);

            }
        });

        radio_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioValue = "AGENT";
                //LogInType(radioValue);
                radio_agent.setChecked(true);
                radio_member.setChecked(false);
            }
        });


    }


    private void passwordTextChange() {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et1.getText().toString().length() == 1)
                    et2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et2.getText().toString().length() == 1)
                    et3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et3.getText().toString().length() == 1)
                    et4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et4.getText().toString().length() == 1) {
                    changeLoginButton();
                } else {
                    logBtn.setEnabled(false);
                    logBtn.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void changeLoginButton() {
        logBtn.setEnabled(true);
        logBtn.setTextColor(getResources().getColor(R.color.colorWhite));
        logBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }


    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }

    }


}
