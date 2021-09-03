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
import android.widget.ScrollView;
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
import com.cmb_collector.adapter.MyRecyclerFDAdapter;
import com.cmb_collector.model.WCFDDataClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.NotificationHelper;
import com.cmb_collector.utility.PDFExportClass;
import com.cmb_collector.utility.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.PageSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FDActivity extends AppBaseClass {

    private ImageView img_fd;
    private Spinner spin_policy;
    private LinearLayout li_header;
    private Button btn_show,btn_download,btn_share;

    private TextView tv_policyNo;

    private RecyclerView list_fd;
    private AutoCompleteTextView tvAuto_policy;

    private ScrollView scroll;

    private ArrayList<String> policyList = new ArrayList<>();
    private List<WCFDDataClass> listData = new ArrayList<>();

    private MyRecyclerFDAdapter adapter;

    private WCUserClass userClass;

    private File file;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    final private int REQUEST_CODE_DOWNLOAD_PERMISSIONS = 222;
    private ImageButton ib_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_fd);

        initView();
        setListener();

        serviceForLoadFDPolicyList();
    }

    private void serviceForLoadFDPolicyList() {
        final CustomProgressDialog dialog = new CustomProgressDialog(FDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_LoadFDPolicyList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoadFDPolicyList");
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
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void setSpinnerItem() {
        ArrayAdapter to_policy = new ArrayAdapter(this, R.layout.spinner_item, policyList);
        spin_policy.setAdapter(to_policy);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, policyList);
        tvAuto_policy.setAdapter(adapter);
    }

    private void serviceForGetPolicyNo(final String policyNo) {
        final CustomProgressDialog dialog = new CustomProgressDialog(FDActivity.this);
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
                    list_fd.removeAllViewsInLayout();
                    list_fd.setVisibility(View.GONE);
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


    private void serviceForFDPolicyStatement() {
        final CustomProgressDialog dialog = new CustomProgressDialog(FDActivity.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Policy_FDPolicyStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("FDPolicyStatement");
                    listData.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        setFDData(jsonObject.optString("POLICYNO"), jsonObject.optString("PAYDATE"), jsonObject.optString("DEPOSITAMOUNT"), jsonObject.optString("MATURITYAMOUNT"), jsonObject.optString("MATDATE"));
                    }
                    list_fd.setVisibility(View.VISIBLE);
                    li_header.setVisibility(View.VISIBLE);
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

    private void setFDData(String policyNo, String payDate, String amount, String matAmount, String matDate) {
        listData.add(new WCFDDataClass(policyNo, payDate, amount, matAmount, matDate));
        setUpFDAdapter();

    }

    private void setUpFDAdapter() {
        adapter = new MyRecyclerFDAdapter(this, listData);
        list_fd.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_fd.setLayoutManager(layoutManager);
    }

    private void setListener() {

        img_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAuto_policy.equals("")){
                    tvAuto_policy.setError("");
                }else {
                    serviceForGetPolicyNo(tvAuto_policy.getText().toString());
                }
            }
        });

        /*spin_policy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select_policy_to = parent.getItemAtPosition(position).toString();

                if (Utility.checkConnectivity(FDActivity.this)) {
                    serviceForGetPolicyNo(select_policy_to);
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(FDActivity.this)) {
                    if (!tv_policyNo.getText().toString().equals("")) {
                        serviceForFDPolicyStatement();
                    } else {
                        Snackbar.make(scroll, "Please Select a Policy", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });


        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.checkConnectivity(FDActivity.this)) {
                    if (li_header.getVisibility() == View.VISIBLE) {
                        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(FDActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                                PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                        Toast.makeText(FDActivity.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }



            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.checkConnectivity(FDActivity.this)) {
                    if (li_header.getVisibility() == View.VISIBLE) {
                        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(FDActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                                                        PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "Permission Needed!!", "Storage and Location permission needed..", "", new PopupCallBackOneButton() {
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
                                PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                                Uri uri= FileProvider.getUriForFile(FDActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                        "Sharing File...");
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
                            }else {

                                PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "Sorry!!", "You have to click download first..", "", new PopupCallBackOneButton() {
                                    @Override
                                    public void onFirstButtonClick() {

                                    }
                                });
                            }



                        }
                    } else {
                        Toast.makeText(FDActivity.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(FDActivity.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }



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
        View headerView = LayoutInflater.from(this).inflate(R.layout.fd_query_statement, new LinearLayout(this), false);
        TextView tv_header = headerView.findViewById(R.id.tv_header);
        TextView tv_acc_no = headerView.findViewById(R.id.tv_acc_no);
        TextView tv_acc_name = headerView.findViewById(R.id.tv_acc_name);


        tv_header.setText("FD Account Statement List");
        tv_acc_no.setText(tv_policyNo.getText().toString());
        String currentString = spin_policy.getSelectedItem().toString();
        String[] separated = currentString.split("-");
        tv_acc_name.setText(separated[0]);


        View rv_print_view_item = getLayoutInflater().inflate(R.layout.print_fd_query_list, new LinearLayout(this), false);

        TextView item_policy_date = rv_print_view_item.findViewById(R.id.item_policy_date);
        TextView item_paydate = rv_print_view_item.findViewById(R.id.item_paydate);
        TextView item_amount = rv_print_view_item.findViewById(R.id.item_amount);
        TextView item_maturity_amount = rv_print_view_item.findViewById(R.id.item_maturity_amount);
        TextView item_maturity_date = rv_print_view_item.findViewById(R.id.item_maturity_date);

        for (int i = 0; i < adapter.getItemCount(); i++) {
            WCFDDataClass summaryClass = listData.get(i);
            item_policy_date.setText(summaryClass.getPolicyNo());
            item_paydate.setText(summaryClass.getPayDate());
            item_amount.setText(summaryClass.getAmount());
            item_maturity_amount.setText(summaryClass.getMtAmount());
            item_maturity_date.setText(summaryClass.getMatDate());

            PDFExportClass.addRVItemToView(i, rv_print_view_item, 1000, 500);
        }

        String file_name = "acc_statement_fd_" + tv_policyNo.getText().toString();

        file = PDFExportClass.exportToPdfWithRecyclerView(headerView, 1000, 1200, list_fd,
                getString(R.string.company_name),file_name, PageSize.A4);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(FDActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                notificationHelper.createNotification("FD Statement", "File Download successfully", intent);


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FDActivity.this);
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

    private void initView() {
        li_header = findViewById(R.id.li_header);
        img_fd = findViewById(R.id.img_fd);
        btn_show = findViewById(R.id.btn_show);
        scroll = findViewById(R.id.scroll);
        spin_policy = findViewById(R.id.spin_policy);
        tv_policyNo = findViewById(R.id.tv_policyNo);
        list_fd = findViewById(R.id.list_fd);

        tvAuto_policy = findViewById(R.id.tvAuto_policy);
        ib_search = findViewById(R.id.ib_search);


        btn_download = findViewById(R.id.btn_download);
        btn_share = findViewById(R.id.btn_share);

        userClass = WCUserClass.getInstance();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

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
