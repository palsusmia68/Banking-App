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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.itextpdf.text.PageSize;
import com.cmb_collector.BuildConfig;
import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.adapter.MySBAccountAdapter;
import com.cmb_collector.model.WCSBAccountData;
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
import java.util.List;
import java.util.Map;

public class SBAcount extends AppBaseClass {

    private ImageView sbacc_img;
    private TextView edit_acc_no;
    private TextView edit_holder_name;
    private TextView edit_balance;

    private AutoCompleteTextView et_acc_name;
    private ImageButton ib_search;
    private Spinner spin_acc_no;
    private LinearLayout li_header;
    private Button btn_show_tran,btn_download,btn_share;
    private RecyclerView list_tran;
    private List<WCSBAccountData> listData = new ArrayList<>();
    private MySBAccountAdapter sbAdapter;

    private ArrayList<String> accountNameList = new ArrayList();
    private WCUserClass userClass;
    private File file;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    final private int REQUEST_CODE_DOWNLOAD_PERMISSIONS = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_sbacount);

        init();
        setListener();
        serviceForAccountNameList();
    }

    private void serviceForAccountNameList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(SBAcount.this);
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

                    setAccountName();
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

    private void setAccountName() {
        ArrayAdapter adapterMember = new ArrayAdapter(this, R.layout.spinner_item, accountNameList);
//        spin_acc_no.setAdapter(adapterMember);
        et_acc_name.setAdapter(adapterMember);
    }

    private void serviceForACC_SplitDetails(final String acc_no) {
        final CustomProgressDialog dialog = new CustomProgressDialog(SBAcount.this);
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

                        edit_acc_no.setText(jsonObject.optString("ACCOUNTNO"));
                        edit_holder_name.setText(jsonObject.optString("AccountHolderName"));
                        edit_balance.setText(jsonObject.optString("BALANCE"));
                        list_tran.removeAllViewsInLayout();
                        list_tran.setVisibility(View.GONE);
                        li_header.setVisibility(View.GONE);

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

    private void serviceForLoadStatement(final String AccNo) {
        final CustomProgressDialog dialog = new CustomProgressDialog(SBAcount.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_LoadStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoadStatement");
                    listData.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);

                        getSBAccountData(jsonObject.optString("TDATE"), jsonObject.optString("INSTNO"),
                                jsonObject.optString("PARTICULAR"), jsonObject.optString("DEPOSIT"),
                                jsonObject.optString("WITHDRAWAL"), jsonObject.optString("BALENCE"));
                    }
                    li_header.setVisibility(View.VISIBLE);
                    list_tran.setVisibility(View.VISIBLE);
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
                params.put("AccountNo", AccNo);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void init() {
        li_header = findViewById(R.id.li_header);
        sbacc_img = findViewById(R.id.sbacc_img);
        edit_acc_no = findViewById(R.id.edit_acc_no);
        edit_holder_name = findViewById(R.id.edit_holder_name);
        edit_balance = findViewById(R.id.edit_balance);
        spin_acc_no = findViewById(R.id.spin_acc_no);
        btn_show_tran = findViewById(R.id.btn_show_tran);
        list_tran = findViewById(R.id.list_tran);
        et_acc_name = findViewById(R.id.et_acc_name);
        ib_search = findViewById(R.id.ib_search);
        btn_download = findViewById(R.id.btn_download);
        btn_share = findViewById(R.id.btn_share);

        userClass = WCUserClass.getInstance();
    }

    private void setListener() {
        sbacc_img.setOnClickListener(backListener);
//        spin_acc_no.setOnItemSelectedListener(accNoListener);
        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.checkConnectivity(SBAcount.this)) {
                    serviceForACC_SplitDetails(et_acc_name.getText().toString());
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });
        btn_show_tran.setOnClickListener(showListener);

        btn_download.setOnClickListener(downloadListener);
        btn_share.setOnClickListener(shareListener);
    }


    private void getSBAccountData(String date, String trnNo, String particulars, String deposit, String withdrawal, String balnc) {
        listData.add(new WCSBAccountData(date, trnNo, particulars, deposit, withdrawal, balnc));
        setUpSBAccountAdapter();
    }

    private void setUpSBAccountAdapter() {
        sbAdapter = new MySBAccountAdapter(this, listData);
        list_tran.setAdapter(sbAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_tran.setLayoutManager(layoutManager);
    }


    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    AdapterView.OnItemSelectedListener accNoListener = new AdapterView.OnItemSelectedListener() {
        String acc_no;

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            acc_no = parent.getItemAtPosition(position).toString();
            if (Utility.checkConnectivity(SBAcount.this)) {
                serviceForACC_SplitDetails(acc_no);
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener showListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utility.checkConnectivity(SBAcount.this)) {
                if (!edit_acc_no.getText().toString().equals(""))
                    serviceForLoadStatement(edit_acc_no.getText().toString());
                else {
                    Toast.makeText(SBAcount.this, "Please Select an Account", Toast.LENGTH_SHORT).show();
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        }
    };


    View.OnClickListener downloadListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Utility.checkConnectivity(SBAcount.this)) {
                if (li_header.getVisibility() == View.VISIBLE) {
                    int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(SBAcount.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                            PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                    Toast.makeText(SBAcount.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }



        }
    };

    View.OnClickListener shareListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Utility.checkConnectivity(SBAcount.this)) {
                if (li_header.getVisibility() == View.VISIBLE) {
                    int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(SBAcount.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                                                    PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "Permission Needed!!", "Storage and Location permission needed..", "", new PopupCallBackOneButton() {
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
                            PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    finish();
                                }
                            });
                        }
                        return;
                    } else
                        {

                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                        if(file!=null) {
                            intentShareFile.setType("application/pdf");
                            Uri uri= FileProvider.getUriForFile(SBAcount.this, BuildConfig.APPLICATION_ID + ".provider",file);
                            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                    "Sharing File...");
                            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                            startActivity(Intent.createChooser(intentShareFile, "Share File"));
                        }else {

                            PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "Sorry!!", "You have to click download first..", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(SBAcount.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(SBAcount.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }




        }
    };

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
        View headerView = LayoutInflater.from(this).inflate(R.layout.sb_query_statement, new LinearLayout(this), false);
        TextView tv_header = headerView.findViewById(R.id.tv_header);

        TextView tv_header2 = headerView.findViewById(R.id.tv_header2);
        TextView tv_header3 = headerView.findViewById(R.id.tv_header3);
        TextView tv_header4 = headerView.findViewById(R.id.tv_header4);

        TextView tv_acc_no = headerView.findViewById(R.id.tv_acc_no);
        TextView tv_acc_name = headerView.findViewById(R.id.tv_acc_name);
        TextView tv_current_bal = headerView.findViewById(R.id.tv_current_bal);


        TextView tv_hh1 = headerView.findViewById(R.id.tv_hh1);
        TextView tv_hh2 = headerView.findViewById(R.id.tv_hh2);
        TextView tv_hh3 = headerView.findViewById(R.id.tv_hh3);
        TextView tv_hh4 = headerView.findViewById(R.id.tv_hh4);
        TextView tv_hh5 = headerView.findViewById(R.id.tv_hh5);
        TextView tv_hh6 = headerView.findViewById(R.id.tv_hh6);


        tv_hh1.setText("Date");
        tv_hh2.setText("Trn No.");
        tv_hh3.setText("Particulars");
        tv_hh4.setText("Deposit");
        tv_hh5.setText("Withdrawal");
        tv_hh6.setText("Balance");

        tv_header.setText("SB Account Statement List");
        tv_acc_no.setText(edit_acc_no.getText().toString());
        tv_acc_name.setText(edit_holder_name.getText().toString());
        tv_current_bal.setText(edit_balance.getText().toString());


        View rv_print_view_item = getLayoutInflater().inflate(R.layout.print_sb_query_list, new LinearLayout(this), false);

        TextView item_date = rv_print_view_item.findViewById(R.id.item_date);
        TextView item_trnno = rv_print_view_item.findViewById(R.id.item_trnno);
        TextView item_particulars = rv_print_view_item.findViewById(R.id.item_particulars);
        TextView item_deposit = rv_print_view_item.findViewById(R.id.item_deposit);
        TextView item_withdrawal = rv_print_view_item.findViewById(R.id.item_withdrawal);
        TextView item_balance = rv_print_view_item.findViewById(R.id.item_balance);

        for (int i = 0; i < sbAdapter.getItemCount(); i++) {
            WCSBAccountData summaryClass = listData.get(i);
            item_date.setText(summaryClass.getDate());
            item_trnno.setText(summaryClass.getTrnNo());
            item_particulars.setText(summaryClass.getParticulars());
            item_deposit.setText(summaryClass.getDeposit());
            item_withdrawal.setText(summaryClass.getWithdrawal());
            item_balance.setText(summaryClass.getBalance());

            PDFExportClass.addRVItemToView(i, rv_print_view_item, 1000, 500);
        }

        String file_name = "acc_statement_sb_" + edit_acc_no.getText().toString();

        file = PDFExportClass.exportToPdfWithRecyclerView(headerView, 1000, 1200, list_tran,
                getString(R.string.company_name),file_name, PageSize.A4);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(SBAcount.this, BuildConfig.APPLICATION_ID + ".provider",file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                notificationHelper.createNotification("Account Statement", "File Download successfully", intent);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SBAcount.this);
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
