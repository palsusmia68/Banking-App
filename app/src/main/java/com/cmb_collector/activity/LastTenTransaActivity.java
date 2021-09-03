package com.cmb_collector.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
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
import com.cmb_collector.BuildConfig;
import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.adapter.TenTransactionAdapter;
import com.cmb_collector.model.WCAccountSummaryClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.NotificationHelper;
import com.cmb_collector.utility.PDFExportClass;
import com.cmb_collector.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LastTenTransaActivity extends AppBaseClass {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    final private int REQUEST_CODE_DOWNLOAD_PERMISSIONS = 222;
    private ImageView img_last_ten;
    LinearLayout li_trans_header;
  //  private Spinner acc_no;
    private Spinner spin_sbAcNo;
    private  String getsbAccount;
    private ArrayList<String> SBAccountList = new ArrayList<>();
   // private TextView tv_acc_no;
    private Button btn_show,btn_download,btn_share;
    private RelativeLayout rl_last_10_trans;
    private RecyclerView recy_last_10;
    private ArrayList<String> accountNameList = new ArrayList();
  //  private AutoCompleteTextView et_acc_name;
   // private ImageButton ib_search;

    private TenTransactionAdapter adapter;
    private ArrayList<WCAccountSummaryClass> summaryClassArrayList = new ArrayList<>();

    private WCUserClass userClass;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_last_ten_transa);

        init();

        setListener();

        serviceForAccountNameList();
        serviceForSbAccountList();

    }

    private void serviceForSbAccountList() {
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
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(LastTenTransaActivity.this, R.layout.spinner_item, SBAccountList);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void serviceForAccountNameList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LastTenTransaActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_AccountNameList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("AccountList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        accountNameList.add(jsonObject.optString("MEMBERNAME"));
                    }
                //    setAccountName();
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
                params.put("UserType", "COLLECTOR");
                params.put("CollectorCode", userClass.getGlobalUserCode());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

//    private void setAccountName() {
//        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, accountNameList);
////        acc_no.setAdapter(adapterMember);
//        et_acc_name.setAdapter(adapterMember);
//    }

    private void serviceForLoadLastTenTrans() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LastTenTransaActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_LastTenTran", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                System.out.println("GET_ACC_LastTenTran "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LastTenTransaction");
                    summaryClassArrayList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        WCAccountSummaryClass summaryClass = new WCAccountSummaryClass();

                        summaryClass.setAccNo(jsonObject.optString("RowNo"));
                        summaryClass.setDate(jsonObject.optString("Tdate"));
                  //      Toast.makeText(getApplicationContext(),jsonObject.optString("Tdate"),Toast.LENGTH_SHORT).show();
                        summaryClass.setTransactionNo(jsonObject.optString("Trnno"));
                        summaryClass.setNarration(jsonObject.optString("Type"));
                        summaryClass.setDebit(jsonObject.optString("Deposit"));
                        summaryClass.setCredit(jsonObject.optString("WithDrawal"));
                        summaryClass.setAmount(jsonObject.optString("Balence"));
                        summaryClass.setPurpose(jsonObject.optString("purpose"));
                        summaryClass.setType(jsonObject.optString("Type"));

                        summaryClassArrayList.add(summaryClass);
                    }
                    setUpTransactionUpdate();
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
                params.put("AccountNo", getsbAccount);
                System.out.println("account No "+params.toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setUpTransactionUpdate() {
        adapter = new TenTransactionAdapter(summaryClassArrayList,LastTenTransaActivity.this);
        recy_last_10.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recy_last_10.setLayoutManager(layoutManager);

    }

    private void serviceForACC_SplitDetails(final String acc_no) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LastTenTransaActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_SplitDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("AccountDetails");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                   //     tv_acc_no.setText(jsonObject.optString("ACCOUNTNO"));
//                        serviceForLoadStatement(jsonObject.optString("ACCOUNTNO"));
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
                params.put("AccountNo", acc_no);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    private void setListener() {
        img_last_ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(LastTenTransaActivity.this)) {
                    rl_last_10_trans.setVisibility(View.VISIBLE);
                    li_trans_header.setVisibility(View.VISIBLE);
                    serviceForLoadLastTenTrans();
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });

        //download
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.checkConnectivity(LastTenTransaActivity.this)) {
                    if (rl_last_10_trans.getVisibility() == View.VISIBLE) {
                        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(LastTenTransaActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    showMessageOKCancel("You need to allow access to Storage",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                            REQUEST_CODE_DOWNLOAD_PERMISSIONS);
                                                }
                                            }, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    return;
                                }

                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_DOWNLOAD_PERMISSIONS);
                            } else {
                                PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
                                    @Override
                                    public void onFirstButtonClick() {
                                        finish();
                                    }
                                });
                            }
                            return;
                        } else {
                            generatePDF();
                        }
                    } else {
                        Toast.makeText(LastTenTransaActivity.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });

        //share
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Utility.checkConnectivity(LastTenTransaActivity.this)) {
                    if (rl_last_10_trans.getVisibility() == View.VISIBLE) {
                        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(LastTenTransaActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    showMessageOKCancel("You need to allow access to Storage",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                                REQUEST_CODE_ASK_PERMISSIONS);
                                                    } else {
                                                        PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "Permission Needed!!", "Storage and Location permission needed..", "", new PopupCallBackOneButton() {
                                                            @Override
                                                            public void onFirstButtonClick() {
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                }
                                            }, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    return;
                                }

                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            } else {
                                PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
                                    @Override
                                    public void onFirstButtonClick() {
                                        finish();
                                    }
                                });
                            }
                            return;
                        } else {


                            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                            if(file!=null) {
                                intentShareFile.setType("application/pdf");
                                Uri uri=FileProvider.getUriForFile(LastTenTransaActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                        "Sharing File...");
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
                            }else {

                                PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "Sorry!!", "You have to click download first..", "", new PopupCallBackOneButton() {
                                    @Override
                                    public void onFirstButtonClick() {

                                    }
                                });
                            }



                        }
                    } else {
                        Toast.makeText(LastTenTransaActivity.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }

            }

        });
//
//        ib_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utility.checkConnectivity(LastTenTransaActivity.this)) {
//                    serviceForACC_SplitDetails(et_acc_name.getText().toString());
//                } else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                }
//            }
//        });

//        acc_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            String selectedItem;
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedItem = parent.getItemAtPosition(position).toString();
//
//                if (Utility.checkConnectivity(LastTenTransaActivity.this)) {
//                    rl_last_10_trans.setVisibility(View.GONE);
//                    serviceForACC_SplitDetails(selectedItem);
//                } else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(LastTenTransaActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        spin_sbAcNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String payMode;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getsbAccount = SBAccountList.get(position);
                //  Toast.makeText(getApplicationContext(),getsbAccount,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .create()
                .show();
    }

    public void generatePDF() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.print_account_statement, new LinearLayout(this), false);
        TextView tv_header = headerView.findViewById(R.id.tv_header);
        TextView tv_FD_header = headerView.findViewById(R.id.tv_FD_header);
        TextView tv_TD_header = headerView.findViewById(R.id.tv_TD_header);
        TextView tv_ac_no = headerView.findViewById(R.id.tv_ac_no);
        TextView tv_ac_name = headerView.findViewById(R.id.tv_ac_name);
        TextView tv_from_date = headerView.findViewById(R.id.tv_from_date);
        TextView tv_to_date = headerView.findViewById(R.id.tv_to_date);


        tv_from_date.setVisibility(View.GONE);
        tv_FD_header.setVisibility(View.GONE);
        tv_TD_header.setVisibility(View.GONE);
        tv_to_date.setVisibility(View.GONE);

//        tv_ac_no.setText(tv_acc_no.getText().toString());
//        String currentString = acc_no.getSelectedItem().toString();
    //    String[] separated = currentString.split("-");
   //     tv_ac_name.setText(separated[0]);
        tv_header.setText("Last 10 Transactions");
        /*tv_from_date.setText(Utility.changeDateFormat("yyyyMMdd", "dd/MM/yyyy", from_date.getText().toString()));
        tv_to_date.setText(Utility.changeDateFormat("yyyyMMdd", "dd/MM/yyyy", to_date.getText().toString()));*/

        View rv_print_view_item = getLayoutInflater().inflate(R.layout.print_account_statement_rv, new LinearLayout(this), false);

        TextView item_ac_no = rv_print_view_item.findViewById(R.id.item_ac_no);
        TextView item_date = rv_print_view_item.findViewById(R.id.item_date);
        TextView item_trans = rv_print_view_item.findViewById(R.id.item_trans);
        TextView item_narra = rv_print_view_item.findViewById(R.id.item_narra);
        TextView item_debit = rv_print_view_item.findViewById(R.id.item_debit);
        TextView item_credit = rv_print_view_item.findViewById(R.id.item_credit);
        TextView item_payment = rv_print_view_item.findViewById(R.id.item_payment);

        for (int i = 0; i < adapter.getItemCount(); i++) {
            WCAccountSummaryClass summaryClass = summaryClassArrayList.get(i);
            item_ac_no.setText(summaryClass.getAccNo());
            item_date.setText(summaryClass.getDate());
            item_trans.setText(summaryClass.getTransactionNo());
            item_narra.setText(summaryClass.getNarration());
            item_debit.setText(summaryClass.getDebit());
            item_credit.setText(summaryClass.getCredit());
            item_payment.setText(summaryClass.getAmount());

            PDFExportClass.addRVItemToView(i, rv_print_view_item, 1000, 500);
        }

     //   String file_name = "acc_statement_" + tv_acc_no.getText().toString();

//        file = PDFExportClass.exportToPdfWithRecyclerView(headerView, 1000, 1200, recy_last_10,
//                getString(R.string.company_name),file_name, PageSize.A4);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(LastTenTransaActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                notificationHelper.createNotification("Last Ten Statement", "File Download successfully", intent);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LastTenTransaActivity.this);
                builder.setTitle("Success")
                        .setMessage("PDF File Generated Successfully.\n" + file)
                        .setIcon(R.drawable.success)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startActivity(intent);
                            }

                        }).show();
            }
        });

    }



    private void init() {
        img_last_ten = findViewById(R.id.img_last_ten);
//        tv_acc_no = findViewById(R.id.tv_acc_no);
//        acc_no = findViewById(R.id.acc_no);
//        et_acc_name = findViewById(R.id.et_acc_name);
//        ib_search = findViewById(R.id.ib_search);
        li_trans_header = findViewById(R.id.li_trans_header);
        btn_show = findViewById(R.id.btn_show);
        btn_download = findViewById(R.id.btn_download);
        btn_share = findViewById(R.id.btn_share);
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);

        rl_last_10_trans = findViewById(R.id.rl_last_10_trans);
        recy_last_10 = findViewById(R.id.recy_last_10);

        userClass = WCUserClass.getInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_DOWNLOAD_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF();
            } else {
                Toast.makeText(this, "You have to grant permission..", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
