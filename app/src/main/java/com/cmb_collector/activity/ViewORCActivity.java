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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyORCViewAdapter;
import com.cmb_collector.model.ViewORCModel;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.ShowMessage;
import com.cmb_collector.utility.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewORCActivity extends AppBaseClass {
    private Spinner spin_vmode;
    private EditText et_collector_code;
    private Button btn_show;
    private RecyclerView list_view_orc;
    private LinearLayout li_header;
    private ImageView ivBack;

    private ArrayList<String> vMonthList = new ArrayList();
    private WCUserClass userClass;
    ViewORCModel viewORCModel;
    List<ViewORCModel> viewORCModellist;
    MyORCViewAdapter adapter;
    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_view_orc);
        init();
        setListner();
        serviceForVMonthList();
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
                    new ShowMessage(ViewORCActivity.this, "Please enter collector code");
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

        new VolleyRequest(ViewORCActivity.this, ServiceConnector.ORC_VIEW_OWNORC, params, true, new VolleyRequest.ResponseListener() {
            @Override
            public void onResponseReceive(String response) {

                try {
                    viewORCModellist = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ViewOwnORC");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        viewORCModel = new ViewORCModel();
                        viewORCModel.setAdvanceSpot(jsonObject.optString("AdvanceSpot"));
                        viewORCModel.setChainBusiness(jsonObject.optString("ChainBusiness"));
                        viewORCModel.setCollecterCode(jsonObject.optString("CollecterCode"));
                        viewORCModel.setCollectorName(jsonObject.optString("CollectorName"));
                        viewORCModel.setDateFrom(jsonObject.optString("DateFrom"));
                        viewORCModel.setDateTo(jsonObject.optString("DateTo"));
                        viewORCModel.setMarketingEXP(jsonObject.optString("MarketingEXP"));
                        viewORCModel.setAdvanceSpot(jsonObject.optString("NetPayment"));
                        viewORCModel.setSelfBusiness(jsonObject.optString("SelfBusiness"));
                        viewORCModel.setServiceCharge(jsonObject.optString("ServiceCharge"));
                        viewORCModel.setTotalBusiness(jsonObject.optString("TotalBusiness"));
                        viewORCModel.setTotalCommission(jsonObject.optString("TotalCommission"));
                        viewORCModel.setTotalSpot(jsonObject.optString("TotalSpot"));
                        viewORCModel.setTotalTds(jsonObject.optString("TotalTds"));
                        viewORCModel.setVoucherMonth(jsonObject.optString("VoucherMonth"));
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
        adapter = new MyORCViewAdapter(this, viewORCModellist);
        list_view_orc.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_view_orc.setLayoutManager(layoutManager);
    }

    private void serviceForVMonthList() {

        HashMap<String, String> params = new HashMap<>();
        params.put("CollectorCode", userClass.getGlobalUserCode());
        /*  userClass.getGlobalUserCode()*/
        new VolleyRequest(ViewORCActivity.this, ServiceConnector.ORC_VOUCHER_MONTH, params, true, new VolleyRequest.ResponseListener() {
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
                        new ShowMessage(ViewORCActivity.this, response);
                    }
                });

    }

    private void setVMonth() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, vMonthList);
        spin_vmode.setAdapter(adapterMember);
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


}
