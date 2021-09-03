package com.cmb_collector.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.Utility;
import com.cmb_collector.utility.VolleyRequest;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FingerPrintActivity extends AppCompatActivity implements MFS100Event {
    private ImageView iv_finger;
    private EditText et_user_code;
    private Button btn_capture, btn_stop_capture, btn_match;

    long mLastDttTime = 0l;
    private static long mLastClkTime = 0;
    private static long Threshold = 1400;
    private long mLastAttTime = 0l;

    byte[] Enroll_Template;
    byte[] Verify_Template;
    byte[] Enroll_Verify_Template;
    private FingerData lastCapFingerData = null;
    Utility.ScannerAction scannerAction = Utility.ScannerAction.Capture;

    int timeout = 10000;
    MFS100 mfs100 = null;

    private boolean isCaptureRunning = false;

    private String MPIN = "";
    private WCUserClass userClass;
    private List<WCUserClass> userClassList = new ArrayList<>();

    @Override
    protected void onStart() {
        try {
            if (mfs100 == null) {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(this);
            } else {
                InitScanner();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        initView();
        setListener();

        try {
            mfs100 = new MFS100(this);
            mfs100.SetApplicationContext(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        iv_finger = findViewById(R.id.iv_finger);
        et_user_code = findViewById(R.id.et_user_code);
        btn_capture = findViewById(R.id.btn_capture);
        btn_stop_capture = findViewById(R.id.btn_stop_capture);
        btn_match = findViewById(R.id.btn_match);

        userClass = WCUserClass.getInstance();

        scannerAction = (Utility.ScannerAction) getIntent().getExtras().get("scanner_action");
        if (scannerAction.equals(Utility.ScannerAction.Verify)) {
            btn_capture.setVisibility(View.GONE);
            btn_match.setVisibility(View.VISIBLE);
            et_user_code.setVisibility(View.VISIBLE);
        }
    }

    private void serviceForGetFingerImage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("UserType", getString(R.string.member_type));
        params.put("UserCode", et_user_code.getText().toString());

        new VolleyRequest(this, ServiceConnector.base_URL + "GET_Login_GetFingerprintImage", params, true, new VolleyRequest.ResponseListener() {
            @Override
            public void onResponseReceive(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("FingerDetails");
                    JSONObject jsonObject = array.getJSONObject(0);
                    if (jsonObject.optString("IsSuccess").equalsIgnoreCase("1")) {
                        if (jsonObject.optString("FingerImage").equalsIgnoreCase("") || jsonObject.optString("FingerImage").equalsIgnoreCase("null")) {
                            PopupClass.showPopUpWithTitleMessageOneButton(FingerPrintActivity.this, "OK", "Sorry!!!", "", "You are not Registered with fingerprint", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    setResultCancelled();
                                }
                            });
                        } else {
                            MPIN = jsonObject.optString("MPIN");
                            Enroll_Verify_Template = Base64.decode(jsonObject.optString("FingerImage"), Base64.DEFAULT);
                            scannerAction = Utility.ScannerAction.Verify;
                            if (!isCaptureRunning) {
                                StartSyncCapture();
                            }
                        }
                    } else {
                        PopupClass.showPopUpWithTitleMessageOneButton(FingerPrintActivity.this, "OK", "Sorry!!!", "Invalid User Code or You are not Approve as a App User.", "Contact to Admin...", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new VolleyRequest.ErrorListener() {
            @Override
            public void onErrorReceive(String response) {
                PopupClass.showPopUpWithTitleMessageOneButton(FingerPrintActivity.this, "OK", "Error!!!", response, "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        });
    }

    private void setListener() {
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClkTime < Threshold) {
                    return;
                }
                mLastClkTime = SystemClock.elapsedRealtime();
                scannerAction = Utility.ScannerAction.Capture;
                if (!isCaptureRunning) {
                    StartSyncCapture();
                }
            }
        });

        btn_stop_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopCapture();
            }
        });

        btn_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCaptureRunning) {
                    StartSyncCapture();
                }
//                if (!TextUtils.isEmpty(et_user_code.getText().toString()))
//                    serviceForGetFingerImage();
//                else {
//                    et_user_code.setError("Please Enter Member Code");
//                    et_user_code.requestFocus();
//                }
            }
        });
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                displayTextUIThread("Device Initialization Success");
                SetTextOnUIThread("Init success");
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
                SetLogOnUIThread(info);
            }
        } catch (Exception ex) {
            displayTextUIThread("Init failed, unhandled exception");
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }

    private void SetTextOnUIThread(final String str) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void SetLogOnUIThread(final String str) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void StartSyncCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, true);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        displayTextUIThread(mfs100.GetErrorMsg(ret));
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;

                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                     //   Toast.makeText(getApplicationContext(),""+bitmap,Toast.LENGTH_SHORT).show();
                      //  System.out.println(" bitmap "+bitmap);
                        FingerPrintActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_finger.setImageBitmap(bitmap);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream .toByteArray();
                                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                et_user_code.setText(encoded);
                                Toast.makeText(getApplicationContext(),""+encoded,Toast.LENGTH_SHORT).show();
                            }
                        });

//                        Log.e("RawImage", Base64.encodeToString(fingerData.RawData(), Base64.DEFAULT));
//                        Log.e("FingerISOTemplate", Base64.encodeToString(fingerData.ISOTemplate(), Base64.DEFAULT));
                        displayTextUIThread("Capture Success");
                        SetTextOnUIThread("Capture Success");
                        String log = "\nQuality: " + fingerData.Quality()
                                + "\nNFIQ: " + fingerData.Nfiq()
                                + "\nWSQ Compress Ratio: "
                                + fingerData.WSQCompressRatio()
                                + "\nImage Dimensions (inch): "
                                + fingerData.InWidth() + "\" X "
                                + fingerData.InHeight() + "\""
                                + "\nImage Area (inch): " + fingerData.InArea()
                                + "\"" + "\nResolution (dpi/ppi): "
                                + fingerData.Resolution() + "\nGray Scale: "
                                + fingerData.GrayScale() + "\nBits Per Pixal: "
                                + fingerData.Bpp() + "\nWSQ Info: "
                                + fingerData.WSQInfo();
                        SetLogOnUIThread(log);
                        SetData2(fingerData);
                    }
                } catch (Exception ex) {
                    displayTextUIThread("Error");
                    SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    private void StopCapture() {
        try {
            mfs100.StopAutoCapture();
        } catch (Exception e) {
            displayTextUIThread("Error");
            SetTextOnUIThread("Error");
        }
    }

    public void SetData2(FingerData fingerData) {
        Toast.makeText(getApplicationContext(),""+fingerData,Toast.LENGTH_SHORT).show();

        try {
            if (scannerAction.equals(Utility.ScannerAction.Capture)) {
                Enroll_Template = new byte[fingerData.ISOTemplate().length];
                System.arraycopy(fingerData.ISOTemplate(), 0, Enroll_Template, 0,
                        fingerData.ISOTemplate().length);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResultOK(fingerData.FingerImage(), fingerData.ISOTemplate());
                    }
                });

            } else if (scannerAction.equals(Utility.ScannerAction.Verify)) {
                if (Enroll_Verify_Template == null) {
                    return;
                }
                Verify_Template = new byte[fingerData.ISOTemplate().length];
                System.arraycopy(fingerData.ISOTemplate(), 0, Verify_Template, 0,
                        fingerData.ISOTemplate().length);
                int ret = mfs100.MatchISO(Enroll_Verify_Template, Verify_Template);
                if (ret < 0) {
                    displayTextUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
                    SetTextOnUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
                } else {
                    if (ret >= 96) {
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setResultOK();
                            }
                        });
                        SetTextOnUIThread("Finger matched with score: " + ret);
                    } else {
                        setResultCancelled();
                        PopupClass.showPopUpWithTitleMessageOneButton(FingerPrintActivity.this, "OK", "Sorry!!!", "Finger not matched...", "", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                        SetTextOnUIThread("Finger not matched, score: " + ret);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            WriteFile("Raw.raw", fingerData.RawData());
            WriteFile("Bitmap.bmp", fingerData.FingerImage());
            WriteFile("ISOTemplate.iso", fingerData.ISOTemplate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResultOK() {
        Intent intent = new Intent();
        intent.putExtra("is_match", true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setResultOK(byte[] fingerImage, byte[] fingerISO) {
        Intent intent = new Intent();
        intent.putExtra("scan_finger", fingerImage);
        intent.putExtra("scan_ISO", fingerISO);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private void WriteFile(String filename, byte[] bytes) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "//FingerData";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = path + "//" + filename;
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(path);
            stream.write(bytes);
            stream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void showSuccessLog(String key) {
        try {
            displayTextUIThread("Device Initialize success");
            SetTextOnUIThread("Init success");
            String info = "\nKey: " + key + "\nSerial: "
                    + mfs100.GetDeviceInfo().SerialNo() + " Make: "
                    + mfs100.GetDeviceInfo().Make() + " Model: "
                    + mfs100.GetDeviceInfo().Model()
                    + "\nCertificate: " + mfs100.GetCertification();
            SetLogOnUIThread(info);
        } catch (Exception e) {
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                displayTextUIThread(mfs100.GetErrorMsg(ret));
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                displayTextUIThread("Uninitialization Success");
                SetLogOnUIThread("Uninit Success");
                SetTextOnUIThread("Uninit Success");
                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }

    private void displayTextUIThread(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FingerPrintActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        if (SystemClock.elapsedRealtime() - mLastAttTime < Threshold) {
            return;
        }
        mLastAttTime = SystemClock.elapsedRealtime();
        int ret;
        if (!hasPermission) {
            SetTextOnUIThread("Permission denied");
            return;
        }
        try {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    ret = mfs100.LoadFirmware();
                    if (ret != 0) {
                        displayTextUIThread(mfs100.GetErrorMsg(ret));
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        SetTextOnUIThread("Load firmware success");
                    }
                } else if (pid == 4101) {
                    String key = "Without Key";
                    ret = mfs100.Init();
                    if (ret == 0) {
                        showSuccessLog(key);
                    } else {
                        displayTextUIThread(mfs100.GetErrorMsg(ret));
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnDeviceDetached() {
        try {

            if (SystemClock.elapsedRealtime() - mLastDttTime < Threshold) {
                return;
            }
            mLastDttTime = SystemClock.elapsedRealtime();
            UnInitScanner();
            displayTextUIThread("Device removed");
            SetTextOnUIThread("Device removed");
        } catch (Exception e) {
        }
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            SetLogOnUIThread(err);
            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onStop() {
        try {
            if (isCaptureRunning) {
                int ret = mfs100.StopAutoCapture();
            }
            Thread.sleep(500);
            //            UnInitScanner();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            if (mfs100 != null) {
                mfs100.Dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
