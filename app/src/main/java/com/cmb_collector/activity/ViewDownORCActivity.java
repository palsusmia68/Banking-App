package com.cmb_collector.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyORCDownViewAdapter;
import com.cmb_collector.model.ViewDownORCModel;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewDownORCActivity extends AppCompatActivity {
    private Spinner spin_vmode;
    private EditText et_collector_code;
    private Button btn_show;
    private RecyclerView list_view_orc;
    private LinearLayout li_header;
    private ImageView ivBack;
    private ArrayList<String> vMonthList = new ArrayList();
    private WCUserClass userClass;
    ViewDownORCModel viewORCModel;
    List<ViewDownORCModel> viewORCModellist;
    MyORCDownViewAdapter adapter;
    String selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_down_orc);
        init();
        setListner();
        serviceForVMonthList();
    }
    private void init() {
        spin_vmode = findViewById(R.id.spin_vmode);
        list_view_orc = findViewById(R.id.list_view_orc);
        btn_show = findViewById(R.id.btn_show);
        et_collector_code = findViewById(R.id.et_collector_code);
        li_header = findViewById(R.id.li_header);
        ivBack = findViewById(R.id.ivBack);
        userClass = WCUserClass.getInstance();
        et_collector_code.setText(userClass.getGlobalUserCode());
    }
    private void setListner() {
        spin_vmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                li_header.setVisibility(View.GONE);
                list_view_orc.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_collector_code.getText().toString().trim().equals("")) {
                    Toast.makeText(ViewDownORCActivity.this, "Please enter collector code", Toast.LENGTH_SHORT).show();
                } else {
                    li_header.setVisibility(View.VISIBLE);
                    list_view_orc.setVisibility(View.VISIBLE);
                    serviceForORCVIew(selectedItem);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }
    private void serviceForORCVIew(String selectedItem) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Vmonth", selectedItem);
        params.put("CollectorCode", userClass.getGlobalUserCode());
        /*  userClass.getGlobalUserCode()*/
        new VolleyRequest(ViewDownORCActivity.this, ServiceConnector.base_URL + "GET_ORC_ViewDownORC", params, true, new VolleyRequest.ResponseListener() {
            @Override
            public void onResponseReceive(String response) {

                try {
                    viewORCModellist = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ViewDownORC");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        viewORCModel = new ViewDownORCModel();
                        viewORCModel.setCollecterCode(jsonObject.optString("CollecterCode"));
                        viewORCModel.setCollectorName(jsonObject.optString("CollectorName"));
                        viewORCModel.setDateFrom(jsonObject.optString("DateFrom"));
                        viewORCModel.setDateTo(jsonObject.optString("DateTo"));
                        viewORCModel.setTotalBusiness(jsonObject.optString("TotalBusiness"));
                        viewORCModel.setTotalCommission(jsonObject.optString("TotalCommission"));
                        viewORCModel.setNetPayment(jsonObject.optString("NetPayment"));
                        viewORCModellist.add(viewORCModel);

                    }
                    setUpORCView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new VolleyRequest.ErrorListener() {
                    @Override
                    public void onErrorReceive(String response) {
                        //   new ShowMessage(ViewORCActivity.this, response);
                    }
                });
    }

    private void setUpORCView() {
        adapter = new MyORCDownViewAdapter(this, viewORCModellist);
        list_view_orc.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_view_orc.setLayoutManager(layoutManager);
    }

    private void serviceForVMonthList() {

        HashMap<String, String> params = new HashMap<>();
        params.put("CollectorCode", userClass.getGlobalUserCode());
        /*  userClass.getGlobalUserCode()*/
        new VolleyRequest(ViewDownORCActivity.this, ServiceConnector.base_URL + "GET_ORC_VoucherMonth", params, true, new VolleyRequest.ResponseListener() {
            @Override
            public void onResponseReceive(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("VoucherMonth");
                    for (int i = 0; i < array.length(); i++) {
                        String string = array.getString(i);
                        vMonthList.add(string);
                    }
                    setVMonth();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new VolleyRequest.ErrorListener() {
                    @Override
                    public void onErrorReceive(String response) {
                        Toast.makeText(ViewDownORCActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setVMonth() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, vMonthList);
        spin_vmode.setAdapter(adapterMember);
    }


}
