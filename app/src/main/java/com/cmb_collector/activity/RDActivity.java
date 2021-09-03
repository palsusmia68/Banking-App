package com.cmb_collector.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.cmb_collector.database.DatabaseHelper;
import com.cmb_collector.model.Fd12setget;
import com.cmb_collector.model.Fddata;
import com.cmb_collector.model.LoadCollectorSBAccount;
import com.cmb_collector.model.Mis60setget;
import com.cmb_collector.model.Misdatasetget;
import com.cmb_collector.model.Plancode_Setget;
import com.cmb_collector.model.Rd12setget;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.model.plancode_tableRd_Setget;
import com.cmb_collector.model.plancode_table_fd_Setget;
import com.cmb_collector.model.plancode_table_mis_setget;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.Sp;
import com.cmb_collector.utility.Utility;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.cmb_collector.database.DatabaseHelper.TABLE_FD12TABLE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_INVESTMENTPLANCODE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_INVESTMENTPLANCODECODE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_INVESTMENTPLANCODEE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_INVESTMENTPLANCODEFD;
import static com.cmb_collector.database.DatabaseHelper.TABLE_INVESTMENTPLANCODEMISCODE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_INVESTMENTPLANCODEMISTABLE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_MIS60TABLE;
import static com.cmb_collector.database.DatabaseHelper.TABLE_PLANCODEFD;
import static com.cmb_collector.database.DatabaseHelper.TABLE_PLANCODETABLEFD;
import static com.cmb_collector.database.DatabaseHelper.TABLE_PLANCODETABLEMIS;
import static com.cmb_collector.database.DatabaseHelper.TABLE_RD12TABLE;

public class RDActivity extends AppCompatActivity {

    private ImageView img_rd;

    private LinearLayout li_account,llout_cash_bal,mi;
    private static TextView et_cash_balance;;

    private TextView tv_bCode;
    private TextView tv_bName;
    private TextView  tv_title, hide;
    private static EditText txt_policy_date;
    private static EditText tv_member_code;
    private static EditText tv_member_name;
    private static EditText txt_father_name;
    private static EditText txt_add;
    private static EditText tv_phone;
    private static TextView tv_term;
    private static EditText txt_deposit_amount;
    private static EditText txt_mamount;
    private TextView tv_sb_accNo;
    private TextView tv_av_bal;
    private TextView txt_spcode;
    private TextView txt_spname;
    private TextView txt_aval_bal;
    private EditText txt_mInterest;

    private EditText et_amount;

    private Spinner spin_member;
    private static Spinner spin_plancode;
    private static Spinner spin_plantable;
    private static Spinner spin_mode;
    private Spinner spin_pay_mode;

    private Button btn_amount;
    private Button btn_submit;

    private static ArrayList<String> memberList = new ArrayList();
    private static List<String> planCodeList = new ArrayList<>();
    private static List<String> planTableList = new ArrayList<>();
    private List<String> policyModeList = new ArrayList<>();

    private String[] payModeList = {"Saving A/C"};

    private String title_name;
    private static String plan_code;
    private static String planCode;
    private String planTable;
    private String amount;
    private String mode;

    static int OffLineflag = 0,OnLineflag=0;

    private static WCUserClass userDetailsData;
    private static AutoCompleteTextView tvAuto_member,tvAuto_member2;
    private static ImageButton ib_search;
    private static WCUserClass userClass;
    private static Spinner spin_sbAcNo;
    private static String getsbAccount;
    private static ArrayList<String> SBAccountList = new ArrayList<>();
    static DatabaseHelper helper;
    String planTabledb = "RD";
    String planTabledb1 = "FD";
    String planTabledb2 = "FD";
    static List<Plancode_Setget> plancodesetgets;
    Plancode_Setget plancodesetgetss;
    Fddata fddata;
    static List<Fddata> fddataa;
    List<String> investmentplacecodefdidd = new ArrayList<String>();
    static List<String> investmentplacecodeee = new ArrayList<String>();
    static List<String> investmentplacecodefddd = new ArrayList<String>();

    List<String> investmentplacecodeidd = new ArrayList<String>();
    static List<String> investmentplacecodecodee = new ArrayList<String>();
    static List<String> investmentplacecoderde = new ArrayList<String>();
    List<String> investmentplacecodedrde = new ArrayList<String>();

    List<String> investmentplacecodemisidd = new ArrayList<String>();
    static List<String> investmentplacecodeevv = new ArrayList<String>();
    static List<String> investmentplacecodemiss = new ArrayList<String>();

    Misdatasetget misdatasetget;
    static List<Misdatasetget> misdatasetgets;
    plancode_tableRd_Setget plancode_tableRd_setget;
    static List<plancode_tableRd_Setget> plancode_tableRd_setgets;
    plancode_table_fd_Setget plancode_table_fd_setget;
    static List<plancode_table_fd_Setget> plancode_table_fd_setgets;
    plancode_table_mis_setget plancode_table_mis_setget;
    static List<plancode_table_mis_setget> plancode_table_mis_setgetList;
    Rd12setget rd12setget;
    static List<Rd12setget> rd12setgets;
    Fd12setget fd12setget;
    static List<Fd12setget> fd12setgets;
    List<String> fdtwelvetableidd = new ArrayList<String>();
    static List<String> modefdd = new ArrayList<String>();
    static List<String> termfdd = new ArrayList<String>();
    static List<String> roifdd = new ArrayList<String>();
    List<String> rdtwelvetableidd = new ArrayList<String>();
    static List<String> modee = new ArrayList<String>();
    static List<String> termm = new ArrayList<String>();
    static List<String> roii = new ArrayList<String>();
    Mis60setget mis60setget;
    static List<Mis60setget> mis60setgets;
    List<String> plancodemisdidd= new ArrayList<String>();
    static List<String> plancodemisss= new ArrayList<String>();
    static List<String> planmiss= new ArrayList<String>();
    List<String> plancodetablefdiddd = new ArrayList<String>();
    static List<String> plancodefddd = new ArrayList<String>();
    static List<String> plancodeff = new ArrayList<String>();
    List<String> plancodetablerdidd = new ArrayList<String>();
    static List<String> plancoderddd = new ArrayList<String>();
    static List<String> planrdd = new ArrayList<String>();

    List<String> mistwelvetableidd = new ArrayList<String>();
    static List<String> modemiss = new ArrayList<String>();
    static List<String> termmiss = new ArrayList<String>();
    static List<String> roimiss = new ArrayList<String>();

    static String[] RddataArray;
    static HashMap<Integer, String> rdcodeId;
    static String[] FDdataArray;
    static HashMap<Integer, String> fddataId;
    static String[] MISdataArray;
    static HashMap<Integer, String> misdataId;
    static String investmentplacecodecode;
    static String investmentplacecodee;
    static String investmentplacecodeev;
    static String[] PlanrddataArray;
    static HashMap<Integer, String> Plancoderdd;
    static String[] plancodeffArray;
    static HashMap<Integer, String> plancodefdId;
    static String[] plancodeffArraymiss;
    static HashMap<Integer, String> plancodefdIdd;
    //////////////////////////////////////////////////////
    static String[] rd12Arraymiss;
    static HashMap<Integer, String> rd12Id;
    static HashMap<Integer, String> roiiID;
    //////////////////////////////
    static String[] fd12Arraymiss;
    static HashMap<Integer, String> fd12Id;
    static HashMap<Integer, String> fdroiiID;
    static String[] miss60Arraymiss;
    static HashMap<Integer, String> miss60Id;
    static HashMap<Integer, String> missroiiID;
    static String term;
    static String[] AccountNoArray;
    static HashMap<Integer, String> balanceId;
    static List<String> accountnumber = new ArrayList<String>();
    static List<String> accountbalance = new ArrayList<String>();
    static LoadCollectorSBAccount loadCollectorSBAccount;
    static List<LoadCollectorSBAccount> loadCollectorSBAccounts;
    static String balanceID;
    static String accoountId;
    private BroadcastReceiver mNetworkReceiver3;
    private BroadcastReceiver broadcastReceiver;
    static Context context;
    static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rd);
        RDActivity.context=getApplicationContext();
        OffLineflag=0;
        OnLineflag=0;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        init();
        setDefaultValues();
        setListener();
//        serviceForMemberNameList();
//        serviceForLoadPlanCode();
//        serviceForSbAccountList();
        mNetworkReceiver3 = new NetworkChange3();
        registerNetworkBroadcastForNougat();
        loadCollectorSBAccounts = new ArrayList<>();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
    }


        public static void dialog(boolean value){
        if(value){
            ib_search.setVisibility(View.VISIBLE);
            tvAuto_member.setVisibility(View.VISIBLE);
            tvAuto_member2.setVisibility(View.GONE);
            tv_member_code.setEnabled(false);
            tv_member_code.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_member_name.setEnabled(false);
            tv_member_name.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            txt_father_name.setEnabled(false);
            txt_father_name.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            txt_add.setEnabled(false);
            txt_add.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_phone.setEnabled(false);
            tv_phone.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_phone.setEnabled(false);
            tv_phone.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            tv_term.setEnabled(false);
            tv_term.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            txt_deposit_amount.setEnabled(false);
            txt_deposit_amount.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            txt_mamount.setEnabled(false);
            txt_mamount.setBackgroundResource(R.drawable.xml_et_bg_border_gray);
            System.out.println("status"+"we are back");
            OnLineflag++;
            if (OnLineflag==1){
                serviceForMemberNameList();
                serviceForLoadPlanCode();
                serviceForSbAccountList();
                System.out.println("status_Internet"+"if block"+OnLineflag);
                spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    String payMode;

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getsbAccount = SBAccountList.get(position);
                        Sp.getInstance(RDActivity.context).storeAccountName(getsbAccount);
                        serviceforbalance(getsbAccount);
                        // Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spin_plancode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        planCode = parent.getItemAtPosition(position).toString();
                        System.out.println("RddataArray "+position);
                        if (Utility.checkConnectivity(RDActivity.context)) {
                            serviceForLoadPlanTable(planCode);
                        }
//                else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //   customProgressDialog = new CustomProgressDialog(NewAgentJoining.context);
            }
        }else {
            ib_search.setVisibility(View.GONE);
            tvAuto_member.setVisibility(View.GONE);
            tvAuto_member2.setVisibility(View.VISIBLE);
            OffLineflag++;
            System.out.println("status"+"could not connect to the internet");
            //  Toast.makeText(getApplicationContext(),"Net off",Toast.LENGTH_LONG).show();
            tv_member_code.setEnabled(true);
            tv_member_code.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_member_name.setEnabled(true);
            tv_member_name.setBackgroundResource(R.drawable.xml_et_bg_border);
            txt_father_name.setEnabled(true);
            txt_father_name.setBackgroundResource(R.drawable.xml_et_bg_border);
            txt_add.setEnabled(true);
            txt_add.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_phone.setEnabled(true);
            tv_phone.setBackgroundResource(R.drawable.xml_et_bg_border);
            tv_term.setEnabled(true);
            tv_term.setBackgroundResource(R.drawable.xml_et_bg_border);
            txt_deposit_amount.setEnabled(true);
            txt_deposit_amount.setBackgroundResource(R.drawable.xml_et_bg_border);
            txt_mamount.setEnabled(true);
            txt_mamount.setBackgroundResource(R.drawable.xml_et_bg_border);
            if (OffLineflag==1){

                if (plan_code.equals("1003")){
                    RDDATA();
                  //  plancode_table_rd();
                 //   RD12();
                }
                if (plan_code.equals("1002")){
                    FDDATA();
                    plancode_table_fd();
                    FD12();
                }
                if (plan_code.equals("1005")){
                    MISATA();
                    plancode_table_mis();
                    MIS60();
                }
//                if (plan_code.equals(investmentplacecodecode)){
//                    spin_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            term = rd12Id.get(spin_mode.getSelectedItemPosition());
//                            tv_term.setText(term);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//
//                    });
//                }
//                if (plan_code.equals(investmentplacecodee)){
//                    spin_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            term = fd12Id.get(spin_mode.getSelectedItemPosition());
//                            tv_term.setText(term);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//
//                    });
//
//
//                }
//                if (plan_code.equals(investmentplacecodeev)){
//                    spin_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            term = miss60Id.get(spin_mode.getSelectedItemPosition());
//                            tv_term.setText(term);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//
//                    });
//                }
                spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        balanceID = balanceId.get(spin_sbAcNo.getSelectedItemPosition());
                        accoountId = spin_sbAcNo.getSelectedItem().toString();
                        getsbAccount = spin_sbAcNo.getSelectedItem().toString();
                        // Toast.makeText(parent.getContext(),"OFF"+balanceID+" "+" "+accoountId, Toast.LENGTH_LONG).show();
                        et_cash_balance.setText(balanceID);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
                loadCollectorSBAccount();
                accountnumberbalance();
            }
            // habijabi();
            spin_plancode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println("RddataArray "+RddataArray[i]);
                    plancode_table_rd();
//                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
//                            android.R.layout.simple_spinner_item, PlanrddataArray);
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spin_plantable.setAdapter(dataAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }
    private void init() {
        img_rd = findViewById(R.id.img_rd);
        li_account = findViewById(R.id.li_account);
        llout_cash_bal = findViewById(R.id.llout_cash_bal);
        et_cash_balance = findViewById(R.id.et_cash_balance);

        spin_member = findViewById(R.id.spin_member);
        spin_plancode = findViewById(R.id.spin_plancode);
        spin_plantable = findViewById(R.id.spin_plantable);
        spin_mode = findViewById(R.id.spin_mode);
        spin_pay_mode = findViewById(R.id.spin_pay_mode);

        tv_bCode = findViewById(R.id.tv_bCode);
        tv_bName = findViewById(R.id.tv_bName);
        tv_member_code = findViewById(R.id.tv_member_code);
        tv_member_name = findViewById(R.id.tv_member_name);
        tvAuto_member2 = findViewById(R.id.tvAuto_member2);
        txt_father_name = findViewById(R.id.txt_father_name);
        txt_add = findViewById(R.id.txt_add);
        tv_phone = findViewById(R.id.tv_phone);
        tv_term = findViewById(R.id.tv_term);
        txt_deposit_amount = findViewById(R.id.txt_deposit_amount);
        txt_mamount = findViewById(R.id.txt_mamount);
        tv_sb_accNo = findViewById(R.id.tv_sb_accNo);
        tv_av_bal = findViewById(R.id.tv_av_bal);
        txt_spcode = findViewById(R.id.txt_spcode);
        txt_spname = findViewById(R.id.txt_spname);
        txt_aval_bal = findViewById(R.id.txt_aval_bal);
        txt_mInterest = findViewById(R.id.txt_mInterest);
        mi = findViewById(R.id.mi);

        et_amount = findViewById(R.id.et_amount);

        btn_amount = findViewById(R.id.btn_amount);
        btn_submit = findViewById(R.id.btn_submit);

        ib_search = findViewById(R.id.ib_search);
        tvAuto_member = findViewById(R.id.tvAuto_member);

        userDetailsData = WCUserClass.getInstance();

        txt_policy_date = findViewById(R.id.txt_policy_date);
        tv_title = findViewById(R.id.tv_title);
        hide = findViewById(R.id.hide);
        userClass = WCUserClass.getInstance();

       // Utility.serviceforbalance(RDActivity.this,userClass.getGlobalUserCode());
    //    et_cash_balance.setText(Utility.Savings_Balance);

        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);

    }

    private void setDefaultValues() {
        plancodesetgets = new ArrayList<>();
        investmentplacecodeidd = new ArrayList<>();
        investmentplacecodecodee = new ArrayList<>();
        investmentplacecoderde = new ArrayList<>();
        investmentplacecodedrde = new ArrayList<>();
        //////////////////////////////////////////////
        investmentplacecodefdidd = new ArrayList<>();
        investmentplacecodeee = new ArrayList<>();
        investmentplacecodefddd = new ArrayList<>();
        fddataa = new ArrayList<>();
        ////////////////////////////////////////////
        investmentplacecodemisidd = new ArrayList<String>();
        investmentplacecodeevv = new ArrayList<String>();
        investmentplacecodemiss = new ArrayList<String>();
        misdatasetgets = new ArrayList<>();
        ///////////////////////plancode_table_rd////////////////////////////////////
        plancode_tableRd_setgets = new ArrayList<>();
        plancodetablerdidd = new ArrayList<String>();
        plancoderddd = new ArrayList<String>();
        planrdd = new ArrayList<String>();
        ////////////////////////
        plancodemisdidd= new ArrayList<String>();
        plancodemisss= new ArrayList<String>();
        planmiss= new ArrayList<String>();
        plancode_table_mis_setgetList = new ArrayList<>();
        /////////////////plancode_table_fd//////
        plancode_table_fd_setgets = new ArrayList<>();
        plancodetablefdiddd= new ArrayList<String>();
        plancodefddd= new ArrayList<String>();
        plancodeff= new ArrayList<String>();
        /////////////////////////////////////////////plancode table mis/////////////////////////////////////////////
        plancode_table_mis_setgetList = new ArrayList<>();
        plancodemisdidd = new ArrayList<>();
        plancodemisss = new ArrayList<>();
        planmiss = new ArrayList<>();
        ///////////////////////////////////////RD12///////////////////////////////////////////////////////////
        rd12setgets = new ArrayList<>();
        rdtwelvetableidd = new ArrayList<>();
        modee = new ArrayList<>();
        termm = new ArrayList<>();
        roii = new ArrayList<>();
        ////////////////////////////////////FD12/////////////////////////////////
        fd12setgets = new ArrayList<>();
        fdtwelvetableidd = new ArrayList<>();
        modefdd = new ArrayList<>();
        termfdd = new ArrayList<>();
        roifdd = new ArrayList<>();
        ////////////////////////////////MIS60////////////////////////////////////////
        mis60setgets = new ArrayList<>();
        mistwelvetableidd = new ArrayList<>();
        modemiss = new ArrayList<>();
        termmiss = new ArrayList<>();
        roimiss = new ArrayList<>();
        ////////////////////////////////////////////////////////////////
        loadCollectorSBAccounts = new ArrayList<>();
        accountbalance = new ArrayList<>();
        title_name = getIntent().getExtras().getString("title");
        if (title_name.equals("New RD")) {
            hide.setText("New RD");
        } else if (title_name.equals("New FD")) {
            hide.setText("New FD");
        } else if (title_name.equals("New MIS")) {
            hide.setText("New MIS");
            mi.setVisibility(View.VISIBLE);
        }
        plan_code = getIntent().getExtras().getString("plancode");
        Log.d("XXXXXXXXcode", plan_code);
        tv_title.setText(hide.getText());
        txt_policy_date.setText(Utility.setCurrentDate());
        tv_bCode.setText(userDetailsData.getGlobalBCode());
        tv_bName.setText(userDetailsData.getGlobalBranchName());
        setPayMode();
        txt_spcode.setText(userDetailsData.getGlobalUserCode());
        txt_spname.setText(userDetailsData.getGlobalUserName());
        helper = new DatabaseHelper(getApplicationContext());
        String query = "SELECT * FROM " + TABLE_INVESTMENTPLANCODE + " where " + TABLE_INVESTMENTPLANCODECODE + "=" +plan_code;
        helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {
                String investmentplacecodeid =  cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODEID));
                investmentplacecodecode =  cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODECODE));
                String investmentplacecoderd =  cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODERD));
                String investmentplacecodedrd =  cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODdRD));
                System.out.println("RD_DATA"+investmentplacecodeid+" "+investmentplacecodecode+" "+investmentplacecoderd+" "+investmentplacecodedrd);
                plancodesetgetss = new Plancode_Setget(
                        investmentplacecodeid,investmentplacecodecode,investmentplacecoderd,investmentplacecodedrd);
                plancodesetgets.add(plancodesetgetss);
                investmentplacecodeidd.add(plancodesetgetss.getInvestmentplacecodeid());
                investmentplacecodecodee.add(plancodesetgetss.getInvestmentplacecodecode());
                investmentplacecoderde.add(plancodesetgetss.getInvestmentplacecoderd());
                investmentplacecodedrde.add(plancodesetgetss.getInvestmentplacecodedrd());
            }
            while (cursor.moveToNext());
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String fd = "SELECT * FROM " + TABLE_INVESTMENTPLANCODEFD  + " where " + TABLE_INVESTMENTPLANCODEE  + "=" +plan_code;
        SQLiteDatabase db1 = helper.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(fd,null);
        if (cursor1.moveToFirst()){
            do {
                String investmentplacecodefdid =  cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODEFDID));
                investmentplacecodee =  cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODEE));
                String investmentplacecodefdd =  cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODEFDD));
                System.out.println("FD_DATA"+investmentplacecodefdid+" "+investmentplacecodee+" "+investmentplacecodefdd);
                fddata = new Fddata(investmentplacecodefdid,investmentplacecodee,investmentplacecodefdd);
                fddataa.add(fddata);
                investmentplacecodefdidd.add(fddata.getInvestmentplacecodefdid());
                investmentplacecodeee.add(fddata.getInvestmentplacecodee());
                investmentplacecodefddd.add(fddata.getInvestmentplacecodefdd());
            }
            while (cursor1.moveToNext());
        }
       ////////////////////////////////////////////////////////////////miss//////////////////////////////////////////////
        String miss = "SELECT * FROM " + TABLE_INVESTMENTPLANCODEMISTABLE  + " where " + TABLE_INVESTMENTPLANCODEMISCODE  + "=" +plan_code;
        SQLiteDatabase db2 = helper.getReadableDatabase();
        Cursor cursor2 = db2.rawQuery(miss,null);
        if (cursor2.moveToFirst()){
            do {
                String investmentplacecodemisid =  cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODEMISID));
                investmentplacecodeev =  cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODEMISCODE));
                String investmentplacecodemis =  cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.TABLE_INVESTMENTPLANCODEMIS));
                misdatasetget = new Misdatasetget(investmentplacecodemisid,investmentplacecodeev,investmentplacecodemis);
                misdatasetgets.add(misdatasetget);
                investmentplacecodemisidd.add(misdatasetget.getInvestmentplacecodemisid());
                investmentplacecodeevv.add(misdatasetget.getInvestmentplacecodee());
                investmentplacecodemiss.add(misdatasetget.getInvestmentplacecodemis());
                System.out.println("MIS_DATA"+investmentplacecodemisid+" "+investmentplacecodeev+" "+investmentplacecodemis);
            }
            while (cursor2.moveToNext());
        }
        /////////////////////////////////////////////plancode table rd/////////////////////////////////////////////
//        String plancoderd = "SELECT * FROM " + TABLE_PLANCODETABLERD  + " where " + TABLE_PLANCODERD  + "=" +planTabledb;
//        String plancoderd = "SELECT * FROM " + TABLE_PLANCODETABLERDUPDATE;
//        helper = new DatabaseHelper(getApplicationContext());
//        SQLiteDatabase db3 = helper.getReadableDatabase();
//        Cursor cursor3 = db3.rawQuery(plancoderd,null);
//        if (cursor3.moveToFirst()){
//            do {
//                String plancodetablerdid =  cursor3.getString(cursor3.getColumnIndex(DatabaseHelper.TABLE_PLANCODETABLERDIDUPDATE));
//                String plancoderdd =  cursor3.getString(cursor3.getColumnIndex(DatabaseHelper.TABLE_PLANSTABLE));
//                String planrd =  cursor3.getString(cursor3.getColumnIndex(DatabaseHelper.TABLE_PLANMINAMOUNT));
//                plancode_tableRd_setget = new plancode_tableRd_Setget(plancodetablerdid,plancoderdd,planrd);
//
//           //     plancode_tableRd_setget = new plancode_tableRd_Setget(plancodetablerdid,plancoderdd,planrd);
//                plancode_tableRd_setgets.add(plancode_tableRd_setget);
//                plancodetablerdidd.add(plancode_tableRd_setget.getPlancodetablerdid());
//                plancoderddd.add(plancode_tableRd_setget.getPlancoderdd());
//                planrdd.add(plancode_tableRd_setget.getPlanrd());
//                System.out.println("plancode_table_rd"+plancodetablerdid+" "+"plancoderdd"+" "+planrd);
//            }
//            while (cursor3.moveToNext());
//        }
        ///////////////////////////////////////////plancode table fd/////////////////////////////////////////////
      //  String plancodefd = "SELECT * FROM " + TABLE_PLANCODETABLEFD  + " where " + TABLE_PLANCODEFD  + "=" +planTabledb1;
        String plancodefd = "SELECT * FROM " + TABLE_PLANCODETABLEFD;
        SQLiteDatabase db4 = helper.getReadableDatabase();
        Cursor cursor4 = db4.rawQuery(plancodefd,null);
        if (cursor4.moveToFirst()){
            do {
                String plancodetablefdidd =  cursor4.getString(cursor4.getColumnIndex(DatabaseHelper.TABLE_PLANCODETABLEFDIDD));
                String plancodefdd =  cursor4.getString(cursor4.getColumnIndex(TABLE_PLANCODEFD));
                String plancodef =  cursor4.getString(cursor4.getColumnIndex(DatabaseHelper.TABLE_PLNCODEFD));
                plancode_table_fd_setget = new plancode_table_fd_Setget(plancodetablefdidd,plancodefdd,plancodef);
                plancode_table_fd_setgets.add(plancode_table_fd_setget);
                plancodetablefdiddd.add(plancode_table_fd_setget.getPlancodetablefdidd());
                plancodefddd.add(plancode_table_fd_setget.getPlancodefdd());
                plancodeff.add(plancode_table_fd_setget.getPlancodef());
                System.out.println("plancode_table_fd"+plancodetablefdidd+" "+plancodefdd+" "+plancodef);
            }
            while (cursor4.moveToNext());
        }
        /////////////////////////////////////////////plancode table mis/////////////////////////////////////////////
      //  String plancodemis = "SELECT * FROM " + TABLE_PLANCODETABLEMIS  + " where " + TABLE_PLANCODEMIS  + "=" +planTabledb2;
        String plancodemis = "SELECT * FROM " + TABLE_PLANCODETABLEMIS;
        SQLiteDatabase db5 = helper.getReadableDatabase();
        Cursor cursor5 = db5.rawQuery(plancodemis,null);
        if (cursor5.moveToFirst()){
            do {
                String plancodemisdid =  cursor5.getString(cursor5.getColumnIndex(DatabaseHelper.TABLE_PLANCODMIS));
                String plancodemiss =  cursor5.getString(cursor5.getColumnIndex(DatabaseHelper.TABLE_PLANCODEMIS));
                String planmis =  cursor5.getString(cursor5.getColumnIndex(DatabaseHelper.TABLE_PLNCODEMIS));
                System.out.println("plancode_table_mis"+plancodemisdid+" "+plancodemiss+" "+planmis);
                plancode_table_mis_setget =new plancode_table_mis_setget(plancodemisdid,plancodemiss,planmis);
                plancode_table_mis_setgetList.add(plancode_table_mis_setget);
                plancodemisdidd.add(plancode_table_mis_setget.getPlancodemisdid());
                plancodemisss.add(plancode_table_mis_setget.getPlancodemiss());
                planmiss.add(plancode_table_mis_setget.getPlanmis());
            }
            while (cursor5.moveToNext());
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        String RD12 = "SELECT * FROM " + TABLE_RD12TABLE;
        SQLiteDatabase db6 = helper.getReadableDatabase();
        Cursor cursor6 = db6.rawQuery(RD12,null);
        if (cursor6.moveToFirst()){
            do {
                String rdtwelvetableid =  cursor6.getString(cursor6.getColumnIndex(DatabaseHelper.TABLE_RD12TABLEID));
                String mode =  cursor6.getString(cursor6.getColumnIndex(DatabaseHelper.TABLE_MODE));
                String term =  cursor6.getString(cursor6.getColumnIndex(DatabaseHelper.TABLE_TERM));
                String roi =  cursor6.getString(cursor6.getColumnIndex(DatabaseHelper.TABLE_ROI));
                System.out.println("RD12"+rdtwelvetableid+" "+mode+" "+term+" "+roi);
                rd12setget = new Rd12setget(rdtwelvetableid,mode,term,roi);
                rd12setgets.add(rd12setget);
                rdtwelvetableidd .add(rd12setget.getRdtwelvetableid());
                modee.add(rd12setget.getMode());
                termm .add(rd12setget.getTerm());
                roii.add(rd12setget.getRoi());
            }
            while (cursor6.moveToNext());
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        String FD12 = "SELECT * FROM " + TABLE_FD12TABLE;
        SQLiteDatabase db7 = helper.getReadableDatabase();
        Cursor cursor7 = db7.rawQuery(FD12,null);
        if (cursor7.moveToFirst()){
            do {
                String fdtwelvetableid =  cursor7.getString(cursor7.getColumnIndex(DatabaseHelper.TABLE_FD12TABLEID));
                String modefd =  cursor7.getString(cursor7.getColumnIndex(DatabaseHelper.TABLE_MODEFD));
                String termfd =  cursor7.getString(cursor7.getColumnIndex(DatabaseHelper.TABLE_TERMFD));
                String roifd =  cursor7.getString(cursor7.getColumnIndex(DatabaseHelper.TABLE_ROIFD));
                System.out.println("FD12"+fdtwelvetableid+" "+modefd+" "+termfd+" "+roifd);
                fd12setget = new Fd12setget(fdtwelvetableid,modefd,termfd,roifd);
                fd12setgets.add(fd12setget);
                fdtwelvetableidd.add(fd12setget.getFdtwelvetableid());
                modefdd.add(fd12setget.getModefd());
                termfdd.add(fd12setget.getTermfd());
                roifdd.add(fd12setget.getRoifd());
            }
            while (cursor7.moveToNext());
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
        String MIS60 = "SELECT * FROM " + TABLE_MIS60TABLE;
        SQLiteDatabase db8 = helper.getReadableDatabase();
        Cursor cursor8 = db8.rawQuery(MIS60,null);
        if (cursor8.moveToFirst()){
            do {
                String mistwelvetableid =  cursor8.getString(cursor8.getColumnIndex(DatabaseHelper.TABLE_MIS60TABLEID));
                String modemis =  cursor8.getString(cursor8.getColumnIndex(DatabaseHelper.TABLE_MODEFDMIS));
                String termmis =  cursor8.getString(cursor8.getColumnIndex(DatabaseHelper.TABLE_TERMMIS));
                String roimis =  cursor8.getString(cursor8.getColumnIndex(DatabaseHelper.TABLE_ROIMIS));
                System.out.println("MIS60"+mistwelvetableid+" "+modemis+" "+termmis+" "+roimis);
                mis60setget =new Mis60setget(mistwelvetableid,modemis,termmis,roimis);
                mis60setgets.add(mis60setget);
                mistwelvetableidd.add(mis60setget.getMistwelvetableid());
                modemiss.add(mis60setget.getModemis());
                termmiss.add(mis60setget.getTermmis());
                roimiss.add(mis60setget.getRoimis());
            }
            while (cursor8.moveToNext());
        }

    }
    private static void RDDATA(){
        RddataArray = new String[plancodesetgets.size()];
        rdcodeId = new HashMap<Integer, String>();
        for (int i = 0; i < plancodesetgets.size(); i++) {
            rdcodeId.put(i, investmentplacecodecodee.get(i));
            RddataArray[i] = investmentplacecoderde.get(i);
            System.out.println("RDVALU" + " " + RddataArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, RddataArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_plancode.setAdapter(dataAdapter);
    }
    private static void FDDATA(){
        FDdataArray = new String[fddataa.size()];
        fddataId = new HashMap<Integer, String>();
        for (int i = 0; i < fddataa.size(); i++) {
            fddataId.put(i, investmentplacecodeee.get(i));
            FDdataArray[i] = investmentplacecodefddd.get(i);
            System.out.println("FDVALU" + " " + FDdataArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, FDdataArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_plancode.setAdapter(dataAdapter);

    }
    private static void MISATA(){
        MISdataArray = new String[misdatasetgets.size()];
        misdataId = new HashMap<Integer, String>();
        for (int i = 0; i < misdatasetgets.size(); i++) {
            misdataId.put(i, investmentplacecodeevv.get(i));
            MISdataArray[i] = investmentplacecodemiss.get(i);
            System.out.println("MISVALU" + " " + MISdataArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, MISdataArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_plancode.setAdapter(dataAdapter);
    }
    private static void plancode_table_rd(){
        PlanrddataArray = new String[plancode_tableRd_setgets.size()];
        Plancoderdd = new HashMap<Integer, String>();
        for (int i = 0; i < plancode_tableRd_setgets.size(); i++) {
            Plancoderdd.put(i, plancoderddd.get(i));
            PlanrddataArray[i] = planrdd.get(i);
            System.out.println("MISVALU" + " " + PlanrddataArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, PlanrddataArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_plantable.setAdapter(dataAdapter);
    }
    private static void plancode_table_fd(){
        plancodeffArray = new String[plancode_table_fd_setgets.size()];
        plancodefdId = new HashMap<Integer, String>();
        for (int i = 0; i < plancode_table_fd_setgets.size(); i++) {
            plancodefdId.put(i, plancodefddd.get(i));
            plancodeffArray[i] = plancodeff.get(i);
            System.out.println("MISVALU" + " " + plancodeffArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, plancodeffArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_plantable.setAdapter(dataAdapter);

    }
    private static void plancode_table_mis(){
        plancodeffArraymiss = new String[plancode_table_mis_setgetList.size()];
        plancodefdIdd = new HashMap<Integer, String>();
        for (int i = 0; i < plancode_table_mis_setgetList.size(); i++) {
            plancodefdIdd.put(i, plancodemisss.get(i));
            plancodeffArraymiss[i] =  planmiss.get(i);
            System.out.println("MISVALU" + " " + plancodeffArraymiss[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, plancodeffArraymiss);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_plantable.setAdapter(dataAdapter);
    }
    ////////////////////////////////////////////////
    private static void RD12(){
        rd12Arraymiss = new String[rd12setgets.size()];
        rd12Id = new HashMap<Integer, String>();
        roiiID = new HashMap<Integer, String>();
        for (int i = 0; i < rd12setgets.size(); i++) {
            rd12Id.put(i, termm.get(i));
            roiiID.put(i, roii.get(i));
            rd12Arraymiss[i] = modee.get(i);
            System.out.println("MISVALU" + " " + rd12Arraymiss[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, rd12Arraymiss);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_mode.setAdapter(dataAdapter);
    }
    private static void FD12(){
        fd12Arraymiss = new String[fd12setgets.size()];
        fd12Id = new HashMap<Integer, String>();
        fdroiiID = new HashMap<Integer, String>();
        for (int i = 0; i < fd12setgets.size(); i++) {
            fd12Id.put(i, termfdd.get(i));
            fdroiiID.put(i, roifdd.get(i));
            fd12Arraymiss[i] = modefdd.get(i);
            System.out.println("MISVALU1" + " " + fd12Arraymiss[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, fd12Arraymiss);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_mode.setAdapter(dataAdapter);
    }
    private static void MIS60(){
        miss60Arraymiss = new String[mis60setgets.size()];
        miss60Id = new HashMap<Integer, String>();
        missroiiID = new HashMap<Integer, String>();
        for (int i = 0; i < mis60setgets.size(); i++) {
            miss60Id.put(i, termmiss.get(i));
            missroiiID.put(i, roimiss.get(i));
            miss60Arraymiss[i] =  modemiss.get(i);
            System.out.println("MISVALU1" + " " + miss60Arraymiss[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, miss60Arraymiss);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_mode.setAdapter(dataAdapter);

    }
    private static void loadCollectorSBAccount() {
        helper = new DatabaseHelper(RDActivity.context);
        Cursor cursor = helper.getAccountnumber();
        if (cursor.moveToFirst()) {
            do {
                loadCollectorSBAccount = new LoadCollectorSBAccount(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_accountnumberlist)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_BALENCE))
                );
                String aa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_accountnumberlist));
                String aaa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_BALENCE));
                System.out.println("accountnumberlist" + " " + aa + " " + aaa);
                loadCollectorSBAccounts.add(loadCollectorSBAccount);
                accountnumber.add(loadCollectorSBAccount.getAccountno());
                accountbalance.add(loadCollectorSBAccount.getBalance());
            }
            while (cursor.moveToNext());
        }
    }

    private static void accountnumberbalance() {
        AccountNoArray = new String[loadCollectorSBAccounts.size()];
        balanceId = new HashMap<Integer, String>();
        for (int i = 0; i < loadCollectorSBAccounts.size(); i++) {
            balanceId.put(i, accountbalance.get(i));
            AccountNoArray[i] = accountnumber.get(i);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RDActivity.context,
                android.R.layout.simple_spinner_item, AccountNoArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sbAcNo.setAdapter(dataAdapter);
    }
    private void setPayMode() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, payModeList);
        spin_pay_mode.setAdapter(adapterMember);
    }

    private void setListener() {

        img_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        btn_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = et_amount.getText().toString();
                mode = spin_mode.getSelectedItem().toString();
                if (!amount.equals(""))
                    serviceForLoadAmountDetails(planTable, amount, mode);
                else
                    et_amount.setError("Please Enter Amount");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(RDActivity.this)) {
                    if (!et_amount.getText().toString().equals("") && !txt_deposit_amount.getText().toString().equals("") && !txt_mamount.getText().toString().equals("") && li_account.getVisibility() == View.GONE) {
                      //  serviceForInsertPolicy();
                        Random rnd = new Random();
                        int number = rnd.nextInt(9999);
                        Sp.getInstance(getApplicationContext()).storeOtpValue(number);
                        //SendSms(number);
                        serviceForInsertPolicy();
                    } else {
                        if (et_amount.getText().toString().equals(""))
                            et_amount.setError("Please Enter Amount");
                        else if (txt_deposit_amount.getText().toString().equals("") || txt_mamount.getText().toString().equals("")) {
                            PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "", "Click on Amount Button", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        } else if (tv_av_bal.getText().toString().equals("") || tv_av_bal.getText().toString().contains("-") || (Double.parseDouble(tv_av_bal.getText().toString()) < Double.parseDouble(et_amount.getText().toString()))) {
                            PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "", "", "Does not have sufficient balance", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "", "", "Sorry!!Internet Connection needed", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });

        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAuto_member.equals("")){
                    tvAuto_member.setError("");
                }else {
                    if (Utility.checkConnectivity(RDActivity.this)) {
                        serviceForMemberSplitDetails(tvAuto_member.getText().toString());
                    } else {
                        PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                    }
                }
            }
        });
      /*  spin_member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String member;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                member = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        spin_plantable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                planTable = parent.getItemAtPosition(position).toString();

                if (Utility.checkConnectivity(RDActivity.this)) {
                    serviceForLoadPolicyMode(planTable);
                }
//                else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_pay_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String payMode;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payMode = parent.getItemAtPosition(position).toString();

                if (payMode.equals("Saving A/C")) {
                    li_account.setVisibility(View.GONE);
                    llout_cash_bal.setVisibility(View.VISIBLE);
                  //  serviceforbalance();
//                    Utility.serviceforbalance(RDActivity.this,userClass.getGlobalUserCode());
//                    tv_av_bal.setText(Utility.Savings_Balance);
                  //  serviceForCashBalanceByMember();
                } else {
                    li_account.setVisibility(View.VISIBLE);
                    llout_cash_bal.setVisibility(View.GONE);
                  //  serviceForSB_AccountBalanceByMember();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void SendSms(int number) {
        final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.sms_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("SMS response "+response);
                dialog.dismissDialog();


                createDialog();


                //  Toast.makeText(getApplicationContext(), "Message send Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Failed to Send Message", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", "cfcnidhi");
                map.put("password", "cfcnidhi@918");
                map.put("to", userClass.getPhoneNumber());
                map.put("senderid", "CFCLTD");
                map.put("text", String.valueOf(number));
                map.put("route", "Informative");
                map.put("type", "text");
                return map;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 0));
        Volley.newRequestQueue(getApplicationContext()).add(str);
    }

    public AlertDialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_otpview, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        Button button = dialogView.findViewById(R.id.No);
        Pinview pinview = dialogView.findViewById(R.id.pinview);
        TextView wrongOtp = dialogView.findViewById(R.id.wrongOtp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int  getpOtpNumber = Sp.getInstance(getApplicationContext()).getOtpValue();
                String SgetpOtpNumber = String.valueOf(getpOtpNumber);
                String user_input_password = pinview.getValue().toString().trim();
                if (SgetpOtpNumber.equals(user_input_password)) {
                    dialog.dismiss();
                    serviceForInsertPolicy();
                    // Toast.makeText(getApplicationContext(), "" + getpOtpNumber + "  " + user_input_password, Toast.LENGTH_LONG).show();
                }else{
                    wrongOtp.setVisibility(View.VISIBLE);

                    //  Toast.makeText(getApplicationContext(), "Wrong OTP"+userClass.getPhoneNumber(), Toast.LENGTH_LONG).show();

                }
            }
        });
        dialog.show();
        return dialog;
    }

    private static void serviceforbalance(String getsbAccount) {
       // final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.context);
     //   dialog.showLoader();
        progressDialog.show();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailSavingsBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("savings Balance "+response);
                progressDialog.dismiss();
         //       dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("Balance");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    /// Savings_Balance= jsonObject.optString("SavngsBalance");
                    et_cash_balance.setText(jsonObject.optString("SavngsBalance"));

                } catch (JSONException e) {
//                    PopupClass.showPopUpWithTitleMessageOneButton(context, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
          //      dialog.dismissDialog();
//                PopupClass.showPopUpWithTitleMessageOneButton(context, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
//                    @Override
//                    public void onFirstButtonClick() {
//                    }
//                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //  System.out.println("cllector code "+userClass.getGlobalUserCode());
                params.put("collectorcode", getsbAccount);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(RDActivity.context);
        requestQueue.add(str);

    }

    private void serviceForCashBalanceByMember() {

        final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Agent_AvailWalletBalance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    String Error_Code=object.optString("Error_Code");
                    if (Error_Code.equals("0")){

                        JSONArray jsonArray = object.getJSONArray("AvailBalance");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            et_cash_balance.setText(jsonObject.optString("OpenBal"));
                        }

                    }else {

                        et_cash_balance.setText("0");
                    }

                } catch (JSONException e) {
                    PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "Error for getting balance!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismissDialog();
                PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "Error!!!", "", "Something Went Wrong.. Please Exit the app..", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", userDetailsData.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);

    }
    private void serviceForInsertPolicy() {
        final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_Policy_InsertPolicy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);

                    JSONArray array = object.getJSONArray("PolicyReturn");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject.optString("ErrorCode").equals("0")) {
                            PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "Success!!", "Policy No:", jsonObject.optString("PolicyNo"), new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    finish();
                                }
                            });
                        } else {
                            PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "Error!!", jsonObject.optString("Status"), "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    Log.d("XXXXXXXXexc", e.getMessage());
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
                params.put("ApplicantNAme", tv_member_name.getText().toString());
                params.put("AgentCode", userDetailsData.getGlobalUserCode());
                params.put("BCODE", userDetailsData.getGlobalBCode());
                params.put("PolicyDate", txt_policy_date.getText().toString());
                params.put("Plancode", spin_plancode.getSelectedItem().toString());
                params.put("Ptable", spin_plantable.getSelectedItem().toString());
                params.put("Term", tv_term.getText().toString());
                params.put("Mode", spin_mode.getSelectedItem().toString());
                params.put("Amount", et_amount.getText().toString());
                params.put("DepositAmount", txt_deposit_amount.getText().toString());
                params.put("MatureAmount", txt_mamount.getText().toString());
                params.put("PAYMODE", spin_pay_mode.getSelectedItem().toString());
                params.put("userid", userDetailsData.getGlobalUserCode());
                params.put("MemberCode", tv_member_code.getText().toString());
                params.put("SBAccount", getsbAccount);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForSB_AccountBalanceByMember() {
        final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_SB_AccountBalanceByMember", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optString("Error_Code").equals("0")) {
                        JSONArray array = object.getJSONArray("AccountNo");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            tv_sb_accNo.setText(jsonObject.optString("AccountNo"));
                            tv_av_bal.setText(jsonObject.optString("Balance"));
                        }
                    }
                } catch (JSONException e) {
                    Log.d("XXXXXXXXexc", e.getMessage());
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
                params.put("MemberCode", tv_member_code.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadAmountDetails(final String planTable, final String amount, final String mode) {
        final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadAmountDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("AmountDetails");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject.optString("ErrorCode").equals("0")) {
                            txt_deposit_amount.setText(jsonObject.optString("DepositAmount"));
                            txt_mamount.setText(jsonObject.optString("MaturityAmount"));
                            txt_mInterest.setText(jsonObject.optString("MisReturn"));
                        } else {
                            PopupClass.showPopUpWithTitleMessageOneButton(RDActivity.this, "OK", "", jsonObject.optString("ErrorMsg"), "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    txt_deposit_amount.setText("");
                                    txt_mamount.setText("");
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    Log.d("XXXXXXXXexc", e.getMessage());
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
                params.put("PTable", planTable);
                params.put("Amount", amount);
                params.put("Mode", mode);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForLoadPolicyMode(final String planTable) {
        final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPolicyMode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPolicyMode"+response);
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("ModeDetails");
                    policyModeList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        policyModeList.add(jsonObject.optString("Mode"));
                        tv_term.setText(jsonObject.optString("Term"));
                    }
                    setPolicyMode();
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
                params.put("PTable", planTable);
                System.out.println("GET_Policy_LoadPolicyMode"+planTable);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setPolicyMode() {
        ArrayAdapter adapterPolicyMode = new ArrayAdapter(this, R.layout.spinner_item, policyModeList);
        adapterPolicyMode.notifyDataSetChanged();
        spin_mode.setAdapter(adapterPolicyMode);


    }

    private static void serviceForLoadPlanTable(final String planCode) {
     //   final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.context);
      //  dialog.showLoader();
        progressDialog.show();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPTable", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GET_Policy_LoadPTable"+response);
                progressDialog.dismiss();
               // dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PlanCodeDetails");
                    planTableList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        String STable = object1.optString("STable");
                        planTableList.add(STable);
                    }
                    setPlanTable();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
           //     dialog.dismissDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanCode", planCode);
                System.out.println("GET_Policy_LoadPTable"+params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RDActivity.context);
        requestQueue.add(str);
    }

    private static void setPlanTable() {
        ArrayAdapter adapterPlanTable = new ArrayAdapter(RDActivity.context, R.layout.spinner_item, planTableList);
        adapterPlanTable.notifyDataSetChanged();
        spin_plantable.setAdapter(adapterPlanTable);
    }

    private static void serviceForLoadPlanCode() {
        progressDialog.show();
   //     final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.context);
   //     dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPlanCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
        //        dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PlanCode");
                    planCodeList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        planCodeList.add(array.optString(i));
                    }

                    setPlanCode();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
          //      dialog.dismissDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("LCode", plan_code);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RDActivity.context);
        requestQueue.add(str);
    }
    private static void serviceForSbAccountList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Collector_LoadSBAccount",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("APP_SB_AccountNo" + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("LoadCollectorSBAccount");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String ac = String.valueOf(jsonArray.get(i));
                                System.out.println("Object " + object.optString("AccountNo"));
                                SBAccountList.add(object.optString("AccountNo"));
                            }
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(RDActivity.context, R.layout.spinner_item, SBAccountList);
                            spin_sbAcNo.setAdapter(spinnerSBAmount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", userClass.getGlobalUserCode());
                // System.out.println("APP_SB_AccountNo" + params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));

        RequestQueue requestQueue = Volley.newRequestQueue(RDActivity.context);
        requestQueue.add(stringRequest);
    }


    private static void setPlanCode() {
        ArrayAdapter adapterPlanCode = new ArrayAdapter(RDActivity.context, R.layout.spinner_item, planCodeList);
        adapterPlanCode.notifyDataSetChanged();
        spin_plancode.setAdapter(adapterPlanCode);
    }

    private void serviceForMemberSplitDetails(final String member) {
        final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Member_SplitDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        JSONArray array = object.getJSONArray("MemberDetails");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            tv_member_code.setText(jsonObject.optString("MemberCode"));
                            tv_member_name.setText(jsonObject.optString("MemberName"));
                            txt_father_name.setText(jsonObject.optString("Father"));
                            txt_add.setText(jsonObject.optString("Address"));
                            tv_phone.setText(jsonObject.optString("Phone"));

                            setAllFieldEmpty();
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
                Log.d("NameText RD ",member);
                params.put("NameText", member);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    private void setAllFieldEmpty() {
        spin_mode.setSelection(0);
        spin_plancode.setSelection(0);
        spin_plantable.setSelection(0);
        spin_pay_mode.setSelection(0);
        et_amount.setText("");
        txt_deposit_amount.setText("");
        txt_mamount.setText("");
    }

    private static void serviceForMemberNameList() {
        progressDialog.show();
     //   final CustomProgressDialog dialog = new CustomProgressDialog(RDActivity.context);
     //   dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Member_MemberNameList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
         //       dialog.dismissDialog();
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("Error_Code") == 0) {
                        memberList.clear();
                        JSONArray array = object.getJSONArray("MemberList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            memberList.add(jsonObject.optString("MemberName"));
                        }
                        setMember();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
          //      dialog.dismissDialog();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserType", "COLLECTOR");
                params.put("CollectorCode", userDetailsData.getGlobalUserCode());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RDActivity.context);
        requestQueue.add(str);
    }

    private static void setMember() {
        ArrayAdapter adapterMember = new ArrayAdapter(RDActivity.context, R.layout.spinner_item, memberList);
        adapterMember.notifyDataSetChanged();
        tvAuto_member.setAdapter(adapterMember);
    }
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver3, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver3, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver3);
            if (broadcastReceiver!=null)
                unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterNetworkChanges();
    }
    public class NetworkChange3 extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {
                if (isOnline(context)) {
                    dialog(true);
                    Log.e("keshav", "Online Connect Intenet ");
                } else {
                    dialog(false);
                    Log.e("keshav", "Conectivity Failure !!! ");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
