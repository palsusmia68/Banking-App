package com.cmb_collector.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.adapter.MyRVDownlineViewAdapter;
import com.cmb_collector.model.DownlineViewClass;
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

public class DownlineViewActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_code;
    private RelativeLayout rl_downline_view;
    private Button btn_show;
    private RecyclerView rv_downline_view;

    private ArrayList<DownlineViewClass> downlineViewClassList = new ArrayList<>();
    private MyRVDownlineViewAdapter adapter;
    private WCUserClass userClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downline_view);

        initView();
        setDefaults();
        setListener();

    }

    private void initView() {

        iv_back = findViewById(R.id.iv_back);
        tv_code = findViewById(R.id.tv_code);
        rl_downline_view = findViewById(R.id.rl_downline_view);
        btn_show = findViewById(R.id.btn_show);
        rv_downline_view = findViewById(R.id.rv_downline_view);

        userClass = WCUserClass.getInstance();
    }

    private void setDefaults() {
        tv_code.setText(userClass.getGlobalUserCode());
    }

    private void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(DownlineViewActivity.this)) {
                    serviceForDownlineView();
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(DownlineViewActivity.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });
    }

    private void serviceForDownlineView() {
        final CustomProgressDialog dialog = new CustomProgressDialog(DownlineViewActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_DownLineView", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("DownLineView");
                    JSONObject jsonObject = null;
                    downlineViewClassList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        downlineViewClassList.add(new DownlineViewClass(
                                jsonObject.getString("ArrangerName"),
                                jsonObject.getString("Rank"),
                                jsonObject.getString("DateOfJOin")

                        ));
                    }
                    setAdapter();
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
                params.put("CollectorCode", tv_code.getText().toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setAdapter() {

        rl_downline_view.setVisibility(View.VISIBLE);
        adapter = new MyRVDownlineViewAdapter(this, downlineViewClassList);
        rv_downline_view.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_downline_view.setLayoutManager(layoutManager);

    }

}
