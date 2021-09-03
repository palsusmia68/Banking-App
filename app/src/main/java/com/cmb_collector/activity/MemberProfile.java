package com.cmb_collector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cmb_collector.R;
import com.cmb_collector.adapter.MyMemberProfileAdapter;
import com.cmb_collector.model.WCMemberProfileClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberProfile extends AppBaseClass {

    private Intent intent;
    private ImageView img_member_profile;
    private RecyclerView member_recycler;
    private String imageUrl;
    private List<WCMemberProfileClass> memberProfileClassList = new ArrayList<>();
    private MyMemberProfileAdapter myMemberProfileAdapter;
    private WCUserClass userClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_member_profile);

        initView();
        serviceForLoadMemberProfile();
    }


    private void initView() {

        img_member_profile = findViewById(R.id.img_member_profile);
        member_recycler = findViewById(R.id.member_recycler);
        img_member_profile.setOnClickListener(backbutton);
        userClass = WCUserClass.getInstance();

    }

    private void serviceForLoadMemberProfile() {
        final CustomProgressDialog dialog = new CustomProgressDialog(MemberProfile.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_LoadMemberProfile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                System.out.println("MemberProfile "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("MemberProfile");
                    memberProfileClassList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        memberProfileClassList.add(new WCMemberProfileClass(jsonObject.optInt("MemberPic"),
                                jsonObject.optString("MemberNo"), jsonObject.optString("Name"),
                                jsonObject.optString("PhoneNo"), jsonObject.optString("Address"),
                                jsonObject.optString("Email"), jsonObject.optString("Base64Pics")));
                    }
                    setUpMemberProfileAdapter();
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

    private void setUpMemberProfileAdapter() {
        myMemberProfileAdapter = new MyMemberProfileAdapter(this, memberProfileClassList);
        member_recycler.setAdapter(myMemberProfileAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        member_recycler.setLayoutManager(layoutManager);
    }

    View.OnClickListener backbutton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };
}
