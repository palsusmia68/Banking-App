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
import com.cmb_collector.BuildConfig;
import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.adapter.MyRecurringRenewalAdapter;
import com.cmb_collector.model.WCRecurringRenewalData;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.NotificationHelper;
import com.cmb_collector.utility.PDFExportClass;
import com.cmb_collector.utility.Utility;
import com.itextpdf.text.PageSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecurringRenewal extends AppBaseClass {
    private ImageView recurring_img;
    private TextView tv_policyNo;
    private LinearLayout li_header;
    private Button btn_show_recurring, btn_download, btn_share;
    private Spinner spin_policy;
    private RecyclerView list_policy;
    private ArrayList<String> policyList = new ArrayList<>();
    private List<WCRecurringRenewalData> listData = new ArrayList<>();
    private MyRecurringRenewalAdapter renewalAdapter;
    private AutoCompleteTextView tvAuto_policy;
    private ImageButton ib_search;
    private WCUserClass userClass;
    private File file;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    final private int REQUEST_CODE_DOWNLOAD_PERMISSIONS = 222;
    private String policyTYPE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_recurring_renewal);
        init();
        setListener();
        serviceForLoadPolicyList();
        policyTYPE = getIntent().getExtras().getString("Policy_type");
    }

    private void serviceForLoadPolicyList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(RecurringRenewal.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadPolicyList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PolicyList");
                    policyList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        policyList.add(jsonObject.optString("PolicyNo"));
                    }
                    setSpinnerItem();

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
                params.put("PolicyType", policyTYPE);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }
    private void serviceForGetPolicyNo(final String policyNo) {
        final CustomProgressDialog dialog = new CustomProgressDialog(RecurringRenewal.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_GetPolicyNo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PolicyNo");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        tv_policyNo.setText(jsonObject.optString("PolicyNo"));
                    }
                    list_policy.removeAllViewsInLayout();
                    list_policy.setVisibility(View.GONE);
                    li_header.setVisibility(View.GONE);

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
                params.put("PolicyCode", policyNo);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForPolicyStatement() {
        final CustomProgressDialog dialog = new CustomProgressDialog(RecurringRenewal.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_PolicyStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("PolicyStatement");
                    listData.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        getRecurringRenewalData(jsonObject.optString("PolicyNo"), jsonObject.optString("InstNo"), jsonObject.optString("Duedate"), jsonObject.optString("PayDate"), jsonObject.optString("Amount"), jsonObject.optString("Balance"));
                    }
                    li_header.setVisibility(View.VISIBLE);
                    list_policy.setVisibility(View.VISIBLE);

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
                params.put("PolicyNo", tv_policyNo.getText().toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }


    private void init() {
        li_header = findViewById(R.id.li_header);
        recurring_img = findViewById(R.id.recurring_img);
        tv_policyNo = findViewById(R.id.tv_policyNo);
        btn_show_recurring = findViewById(R.id.btn_show_recurring);
        spin_policy = findViewById(R.id.spin_policy);
        list_policy = findViewById(R.id.list_policy);
        btn_download = findViewById(R.id.btn_download);
        btn_share = findViewById(R.id.btn_share);

        tvAuto_policy = findViewById(R.id.tvAuto_policy);
        ib_search = findViewById(R.id.ib_search);

        userClass = WCUserClass.getInstance();
    }

    private void setListener() {
        recurring_img.setOnClickListener(backListener);
        btn_show_recurring.setOnClickListener(showListener);
        btn_download.setOnClickListener(downloadListener);
        btn_share.setOnClickListener(shareListener);
        ib_search.setOnClickListener(searchListener);

/*
        spin_policy.setOnItemSelectedListener(policyItemListener);
*/
    }

    private void setSpinnerItem() {
        ArrayAdapter to_policy = new ArrayAdapter(this, R.layout.spinner_item, policyList);
        spin_policy.setAdapter(to_policy);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, policyList);
        tvAuto_policy.setAdapter(adapter);
    }

    private void getRecurringRenewalData(String policyNo, String instNo, String dueDate, String payDate, String amount, String balance) {
        listData.add(new WCRecurringRenewalData(policyNo, instNo, dueDate, payDate, amount, balance));
        setUpRenewalAdapter();
    }

    private void setUpRenewalAdapter() {
        renewalAdapter = new MyRecurringRenewalAdapter(this, listData);
        list_policy.setAdapter(renewalAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_policy.setLayoutManager(layoutManager);
    }

    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    View.OnClickListener searchListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (tvAuto_policy.equals("")){
                tvAuto_policy.setError("");
            }else {
                serviceForGetPolicyNo(tvAuto_policy.getText().toString());
            }
        }
    };
/*    AdapterView.OnItemSelectedListener policyItemListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String select_policy_to = parent.getItemAtPosition(position).toString();

            if (Utility.checkConnectivity(RecurringRenewal.this)) {
                serviceForGetPolicyNo(select_policy_to);
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(RecurringRenewal.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };*/

    View.OnClickListener showListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utility.checkConnectivity(RecurringRenewal.this)) {
                serviceForPolicyStatement();
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(RecurringRenewal.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }
        }
    };

    View.OnClickListener downloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Utility.checkConnectivity(RecurringRenewal.this)) {
                if (li_header.getVisibility() == View.VISIBLE) {
                    int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(RecurringRenewal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                            PopupClass.showPopUpWithTitleMessageOneButton(RecurringRenewal.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                    Toast.makeText(RecurringRenewal.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(RecurringRenewal.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
            }


        }
    };

    View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utility.checkConnectivity(RecurringRenewal.this)) {
                if (li_header.getVisibility() == View.VISIBLE) {
                    int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(RecurringRenewal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to Storage",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                        REQUEST_CODE_ASK_PERMISSIONS);
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
                            PopupClass.showPopUpWithTitleMessageOneButton(RecurringRenewal.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {
                                    finish();
                                }
                            });
                        }
                        return;
                    } else {


                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                        if (file != null) {
                            intentShareFile.setType("application/pdf");
                            Uri uri = FileProvider.getUriForFile(RecurringRenewal.this, BuildConfig.APPLICATION_ID + ".provider", file);
                            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                    "Sharing File...");
                            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                            startActivity(Intent.createChooser(intentShareFile, "Share File"));
                        } else {
                            PopupClass.showPopUpWithTitleMessageOneButton(RecurringRenewal.this, "OK", "Sorry!!", "You have to click download first..", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }


                    }
                } else {
                    Toast.makeText(RecurringRenewal.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(RecurringRenewal.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
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
        View headerView = LayoutInflater.from(this).inflate(R.layout.rd_query_statement, new LinearLayout(this), false);
        TextView tv_header = headerView.findViewById(R.id.tv_header);
        TextView tv_policy_no = headerView.findViewById(R.id.tv_policy_no);
        TextView tv_policy_name = headerView.findViewById(R.id.tv_policy_name);


        tv_header.setText("Recurring Renewal List");
        tv_policy_no.setText(tv_policyNo.getText().toString());
        String currentString = spin_policy.getSelectedItem().toString();
        String[] separated = currentString.split("-");
        tv_policy_name.setText(separated[0]);


        View rv_print_view_item = getLayoutInflater().inflate(R.layout.print_recurring_renewal_list, new LinearLayout(this), false);

        TextView item_policyno = rv_print_view_item.findViewById(R.id.item_policyno);
        TextView item_instno = rv_print_view_item.findViewById(R.id.item_instno);
        TextView item_duedate = rv_print_view_item.findViewById(R.id.item_duedate);
        TextView item_paydate = rv_print_view_item.findViewById(R.id.item_paydate);
        TextView item_amount = rv_print_view_item.findViewById(R.id.item_amount);
        TextView item_balance = rv_print_view_item.findViewById(R.id.item_balance);

        for (int i = 0; i < renewalAdapter.getItemCount(); i++) {
            WCRecurringRenewalData summaryClass = listData.get(i);
            item_policyno.setText(summaryClass.getPolicyNo());
            item_instno.setText(summaryClass.getInstNo());
            item_duedate.setText(summaryClass.getDueDate());
            item_paydate.setText(summaryClass.getPayDate());
            item_amount.setText(summaryClass.getAmount());
            item_balance.setText(summaryClass.getBalance());

            PDFExportClass.addRVItemToView(i, rv_print_view_item, 1000, 500);
        }

        String file_name = "acc_statement_rr" + tv_policyNo.getText().toString();

        file = PDFExportClass.exportToPdfWithRecyclerView(headerView, 1000, 1200, list_policy,
                getString(R.string.company_name), file_name, PageSize.A4);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(RecurringRenewal.this, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                notificationHelper.createNotification("Renewal Statement", "File Download successfully", intent);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RecurringRenewal.this);
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
