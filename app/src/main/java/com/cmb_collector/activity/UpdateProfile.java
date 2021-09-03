package com.cmb_collector.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.cmb_collector.R;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.FileUtil;
import com.cmb_collector.utility.ImageCompressor;
import com.cmb_collector.utility.ImagePickerActivity;
import com.cmb_collector.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.cmb_collector.activity.InsertNewMember.REQUEST_IMAGE;

public class UpdateProfile extends AppBaseClass {
    private EditText et_name, et_email, et_aadhar, et_add, et_phone_no, et_pan_no;
    private TextView et_dob;
    String DOB = "";
    private ImageView img_edit_profile;
    private FloatingActionButton img_update;
    private ImageView camera_img;
    private ImageView gallery_img;
    private ImageView img_pick;
    private ImageView img_remove;

    private CircleImageView img_profile;

    private Button btn_cancel;

    private Calendar myDob = Calendar.getInstance();

    Uri cameraImageUri = null;
    private byte[] pics;
    private Bitmap memPic;
    private Intent intent;
    private File actualImage;
    private File compressedImage;


    private Dialog dialog;

    private static final int REQUEST_CAMERA_CODE = 0;
    private static final int REQUEST_STORAGE_CODE = 1;

    private WCUserClass userClass;
    private File actualProfileImage = null;
    private File compressedProfileImage = null;
    private Button btn_changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_update_profile);

        initView();
        setListener();
        serviceForLoadOwnProfileInformation();
    }

    private void initView() {

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_aadhar = findViewById(R.id.et_aadhar);
        et_dob = findViewById(R.id.et_dob);
        et_add = findViewById(R.id.et_add);
        et_phone_no = findViewById(R.id.et_phone);
        img_edit_profile = findViewById(R.id.img_edit_profile);
        img_update = findViewById(R.id.img_update);
        et_pan_no = findViewById(R.id.et_pan_no);
        img_profile = findViewById(R.id.img_profile);
        img_pick = findViewById(R.id.img_pick);
        img_remove = findViewById(R.id.img_remove);
        btn_changepass = findViewById(R.id.btn_changepass);

        userClass = WCUserClass.getInstance();

    }

    private void setListener() {
        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UpdateProfile.this, OwnProfile.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UpdateProfile.this,ChangePassActivity.class);
                intent.putExtra("checkfrom","0");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                intent = new Intent(UpdateProfile.this, OwnProfile.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);*/
                uploadProfile();

            }
        });

        et_dob.setOnClickListener(dobListener);

        img_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
            }
        });
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
        Intent intent = new Intent(UpdateProfile.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(UpdateProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
        builder.setTitle("Need to permision");
        builder.setMessage("Camera & Gallery permission need ");
        builder.setPositiveButton("Allow", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void uploadProfile() {
        final CustomProgressDialog dialog = new CustomProgressDialog(UpdateProfile.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Collector_ProfileUpdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ProfileUpdate");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String ErrorCode = jsonObject.getString("ErrorCode");
                        if (ErrorCode.equals("0")) {
                            String UpdateStatus = jsonObject.getString("UpdateStatus");
                            Toast.makeText(UpdateProfile.this, UpdateStatus, Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(UpdateProfile.this, "Something went to wrong", Toast.LENGTH_LONG).show();
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
                params.put("CollectorCode", userClass.getGlobalUserCode());
                params.put("EmailId", et_email.getText().toString().trim());
                params.put("AdharNo", et_aadhar.getText().toString().trim());
                params.put("DOB", DOB);
                params.put("Address", et_add.getText().toString().trim());
                params.put("PanNo", et_pan_no.getText().toString().trim());
                params.put("CollectorType", "COLLECTOR");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadOwnProfileInformation() {
        final CustomProgressDialog dialog = new CustomProgressDialog(UpdateProfile.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_LoadOwnProfileInformation", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("OwnProfileInformation");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        et_name.setText(jsonObject.optString("Name"));
                        et_email.setText(jsonObject.optString("Email"));
                        et_aadhar.setText(jsonObject.optString("AadharNo"));
                        et_dob.setText(jsonObject.optString("CollectorDOB"));
                        try {
                            final String inputFormat = "dd/MM/yyyy";
                            final String outputFormat = "yyyyMMdd";
                            DOB = String.valueOf(TimeStampConverter(inputFormat, jsonObject.optString("CollectorDOB"),
                                    outputFormat));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        et_add.setText(jsonObject.optString("Address"));
                        et_phone_no.setText(jsonObject.optString("Phone"));
                        et_pan_no.setText(jsonObject.optString("PAN"));
                        Glide.with(UpdateProfile.this)
                                .load(LoadPic(jsonObject.optString("ProfilePic")))
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.profile_view)
                                        .skipMemoryCache(true))
                                .into(img_profile);
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
                params.put("CollectorCode", userClass.getGlobalUserCode());
                params.put("UserType", "COLLECTOR");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private static String TimeStampConverter(final String inputFormat,
                                             String inputTimeStamp, final String outputFormat)
            throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
                inputFormat).parse(inputTimeStamp));
    }

    private Bitmap LoadPic(String picPath) {
        pics = Base64.decode(picPath, Base64.DEFAULT);
        memPic = BitmapFactory.decodeByteArray(pics, 0, pics.length);
        return memPic;
    }

    View.OnClickListener dobListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog date = new DatePickerDialog(UpdateProfile.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myDob.set(Calendar.YEAR, year);
                    myDob.set(Calendar.MONTH, monthOfYear);
                    myDob.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    et_dob.setText(Utility.changeDateFormat(myDob, "dd/MM/yyyy"));
                    DOB = Utility.changeDateFormat(myDob, "yyyyMMdd");
                }
            }, myDob.get(Calendar.YEAR), myDob.get(Calendar.MONTH), myDob.get(Calendar.DAY_OF_MONTH));

            date.getDatePicker().setMaxDate(System.currentTimeMillis());
            date.show();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    img_profile.setImageBitmap(bitmap);
                    actualProfileImage = FileUtil.from(this, uri);
                    if (actualProfileImage == null) {
                        Toast.makeText(UpdateProfile.this, "Choose an Image", Toast.LENGTH_SHORT).show();
                    } else {
                        new ImageCompressor(UpdateProfile.this)
                                .compressToFileAsFlowable(actualProfileImage)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<File>() {
                                    @Override
                                    public void accept(File file) throws Exception {
                                        compressedProfileImage = file;
                                        Bitmap profilePath = null;
                                        if (compressedProfileImage != null) {
                                            profilePath = BitmapFactory.decodeFile(compressedProfileImage.getAbsolutePath());

                                        }
                                        uploadImage(profilePath,
                                                userClass.getGlobalMemberCode());
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                        Toast.makeText(UpdateProfile.this, "Error in Compress", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImage(final Bitmap bitmapProfile, final String memberCode) {
        String bitProfileImage = "";
        if (bitmapProfile != null) {
            bitProfileImage = BitMapToString(bitmapProfile);
        }
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(UpdateProfile.this);
        customProgressDialog.showLoader();
        String finalBitProfileImage = bitProfileImage;
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_InsertBPics", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customProgressDialog.dismissDialog();
                if (compressedProfileImage != null) {
                    if (compressedProfileImage.exists()) {
                        compressedProfileImage.delete();

                    }
                }
                Toast.makeText(UpdateProfile.this, "Image Upload Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.dismissDialog();
                Toast.makeText(UpdateProfile.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("MemberCode", memberCode);
                map.put("Pics64", finalBitProfileImage);
                map.put("Sign64", "");
                map.put("MDoc64", "");
                map.put("MFingerPic64", "");
                return map;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(120000, 2, 0));
        Volley.newRequestQueue(UpdateProfile.this).add(str);
//        MySingleton.getInstance(this).addRequesteQue(str);
    }

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
