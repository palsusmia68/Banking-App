package com.cmb_collector.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.cmb_collector.R;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnProfile extends AppBaseClass {

    private TextView et_name, et_email, et_aadhar, et_dob, et_add, et_phone_no, et_pan;
    private ImageView img_own_profile;
    private ImageView img_update;
    private CircleImageView img_profile;

    private byte[] pics;
    private Bitmap memPic;

    private Intent intent;

    private FloatingActionButton li_update;

    private Calendar profile_date = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener profile_dob;
    private WCUserClass userClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_own_profile);

        img_own_profile = findViewById(R.id.img_own_profile);
        img_update = findViewById(R.id.img_update);
        img_profile = findViewById(R.id.img_profile);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_aadhar = findViewById(R.id.et_aadhar);
        et_dob = findViewById(R.id.et_dob);
        et_add = findViewById(R.id.et_add);
        et_phone_no = findViewById(R.id.et_phone_no);
        et_pan = findViewById(R.id.et_pan);
        li_update = findViewById(R.id.li_update);
        userClass = WCUserClass.getInstance();

        img_own_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        li_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OwnProfile.this, UpdateProfile.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        serviceForLoadOwnProfileInformation();

    }

    private void serviceForLoadOwnProfileInformation() {
        final CustomProgressDialog dialog = new CustomProgressDialog(OwnProfile.this);
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
                        et_add.setText(jsonObject.optString("Address"));
                        et_phone_no.setText(jsonObject.optString("Phone"));
                        et_pan.setText(jsonObject.optString("PAN"));
                        Glide.with(OwnProfile.this)
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

    private Bitmap LoadPic(String picPath) {
        pics = Base64.decode(picPath, Base64.DEFAULT);
        memPic = BitmapFactory.decodeByteArray(pics, 0, pics.length);
        return memPic;
    }


}
