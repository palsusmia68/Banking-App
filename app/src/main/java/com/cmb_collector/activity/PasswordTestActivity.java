package com.cmb_collector.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cmb_collector.R;

public class PasswordTestActivity extends AppCompatActivity implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {
    EditText et1, et2, et3, et4;
    private int whoHasFocus;
    char[] code = new char[4];//Store the digits in charArray.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_test);

        init();
    }

    private void init() {
        et1 = findViewById(R.id.edit_one);
        et2 = findViewById(R.id.edit_two);
        et3 = findViewById(R.id.edit_three);
        et4 = findViewById(R.id.edit_four);
        setListener();
    }

    private void setListener() {
        et1.addTextChangedListener(this);
        et2.addTextChangedListener(this);
        et3.addTextChangedListener(this);
        et4.addTextChangedListener(this);

        et1.setOnKeyListener(this);
        et2.setOnKeyListener(this);
        et3.setOnKeyListener(this);
        et4.setOnKeyListener(this);

        et1.setOnFocusChangeListener(this);
        et2.setOnFocusChangeListener(this);
        et3.setOnFocusChangeListener(this);
        et4.setOnFocusChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (whoHasFocus) {
            case 1:
                if (!et1.getText().toString().isEmpty()) {
                    code[0] = et1.getText().toString().charAt(0);
                    et2.requestFocus();
                }
                break;

            case 2:
                if (!et2.getText().toString().isEmpty()) {
                    code[1] = et2.getText().toString().charAt(0);
                    et3.requestFocus();
                }
                break;
            case 3:
                if (!et3.getText().toString().isEmpty()) {
                    code[2] = et3.getText().toString().charAt(0);
                    et4.requestFocus();
                }
                break;

            case 4:
                if (!et4.getText().toString().isEmpty()) {
                    code[3] = et4.getText().toString().charAt(0);
                    et4.requestFocus();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edit_one:
                whoHasFocus = 1;
                break;

            case R.id.edit_two:
                whoHasFocus = 2;
                break;

            case R.id.edit_three:
                whoHasFocus = 3;
                break;

            case R.id.edit_four:
                whoHasFocus = 4;
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                switch (v.getId()) {
                    case R.id.edit_two:
                        if (et2.getText().toString().isEmpty())
                            et1.requestFocus();
                        break;

                    case R.id.edit_three:
                        if (et3.getText().toString().isEmpty())
                            et2.requestFocus();
                        break;

                    case R.id.edit_four:
                        if (et4.getText().toString().isEmpty())
                            et3.requestFocus();
                        break;

                    default:
                        break;
                }
            }
        }
        return false;
    }
}
