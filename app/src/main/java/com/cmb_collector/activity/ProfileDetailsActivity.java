package com.cmb_collector.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.cmb_collector.R;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends AppBaseClass {

    private ImageView img_member_profile;
    private RelativeLayout rl_item;
    private CircleImageView item_circleImage;

    private LinearLayout ln_main;
    private String imagePic;
    Bundle bundle = null;

    private TextView et_name, et_email, et_aadhar, et_dob, et_add, et_phone_no, et_pan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_member_profile_details);

        img_member_profile = findViewById(R.id.img_member_profile);
        img_member_profile.setOnClickListener(backbutton);
        rl_item = findViewById(R.id.rl_item);
        item_circleImage = findViewById(R.id.item_circleImage);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_aadhar = findViewById(R.id.et_aadhar);
        et_dob = findViewById(R.id.et_dob);
        et_add = findViewById(R.id.et_add);
        et_phone_no = findViewById(R.id.et_phone_no);
        et_pan = findViewById(R.id.et_pan);
        ln_main = findViewById(R.id.ln_main);
        bundle = getIntent().getExtras();
        serviceForLoadOwnProfileInformation();
    }

    private Bitmap LoadPic(String picPath) {
        byte[] pics = Base64.decode(picPath, Base64.DEFAULT);
        Bitmap memPic = BitmapFactory.decodeByteArray(pics, 0, pics.length);
        return memPic;
    }

    View.OnClickListener backbutton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void serviceForLoadOwnProfileInformation() {
        final CustomProgressDialog dialog = new CustomProgressDialog(ProfileDetailsActivity.this);
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

                        Glide.with(ProfileDetailsActivity.this)
                                .load(LoadPic(jsonObject.optString("ProfilePic")))
                                .apply(new RequestOptions()
                                        .skipMemoryCache(true)
                                        .placeholder(R.drawable.own_profile))
                                .into(item_circleImage);

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
                params.put("CollectorCode", bundle.getString("getMemberNo"));
                params.put("UserType", "MEMBER");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
}
