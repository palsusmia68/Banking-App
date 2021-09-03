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
import com.cmb_collector.adapter.MyLoanEmiAdapter;
import com.cmb_collector.model.WCLoanEmiData;
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

public class LoanEMI extends AppBaseClass {
    private ImageView loan_emi_img;
    private LinearLayout li_header;
    private TextView edit_loan_id;
    private TextView edit_customer_name;

    private Button btn_show_loan,btn_download,btn_share;
    private Spinner spin_loan;
    private RecyclerView list_loanemi;
    private List<WCLoanEmiData> listData = new ArrayList<>();
    private List<String> loanList = new ArrayList<>();

    private WCUserClass userClass;

    private AutoCompleteTextView tvAuto_policy;


    private MyLoanEmiAdapter loanEmiAdapter;


    private File file;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    final private int REQUEST_CODE_DOWNLOAD_PERMISSIONS = 222;

    private ImageButton ib_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_loan_emi);
        init();
        setListener();
        serviceForLoanNameListForStatement();
    }

    private void serviceForLoanNameListForStatement() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanEMI.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoanNameListForStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("LoanNameList");
                    loanList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        loanList.add(jsonObject.optString("APPLICANTNAME"));
                    }
                    setLoan();
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

    private void setLoan() {
        ArrayAdapter to_policy = new ArrayAdapter(this, R.layout.spinner_item, loanList);
        spin_loan.setAdapter(to_policy);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, loanList);
        tvAuto_policy.setAdapter(adapter);
    }

    private void serviceForSearchLoanForRepaymentNonEmi(final String loan_name) {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanEMI.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_SearchLoanForRepaymentNonEmi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("SearchLoanForRepaymentNonEmi");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        edit_loan_id.setText(jsonObject.optString("LoanId"));
                        edit_customer_name.setText(jsonObject.optString("ApplicantName"));
                        list_loanemi.removeAllViewsInLayout();
                    }
                    list_loanemi.setVisibility(View.GONE);
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
                params.put("SearchLoan", loan_name);
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    public void serviceForEmiStatement() {
        final CustomProgressDialog dialog = new CustomProgressDialog(LoanEMI.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_Loan_LoanEmiStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optString("Error_Code").equals("1")) {
                        PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "", "", "No EMI Statement", new PopupCallBackOneButton() {
                            @Override
                            public void onFirstButtonClick() {

                            }
                        });
                    } else {
                        JSONArray array = object.getJSONArray("LoanStatement");
                        listData.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            getLoanEmiData(jsonObject.optString("LoanId"), jsonObject.optString("PayNo"),
                                    jsonObject.optString("Paydate"), jsonObject.optString("PayAmount"),
                                    jsonObject.optString("Principal"), jsonObject.optString("Interest"));
                        }
                        list_loanemi.setVisibility(View.VISIBLE);
                        li_header.setVisibility(View.VISIBLE);
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
                params.put("Loanid", edit_loan_id.getText().toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void init() {
        li_header = findViewById(R.id.li_header);
        loan_emi_img = findViewById(R.id.loan_emi_img);
        edit_loan_id = findViewById(R.id.edit_loan_id);
        edit_customer_name = findViewById(R.id.edit_customer_name);
        btn_show_loan = findViewById(R.id.btn_show_loan);
        spin_loan = findViewById(R.id.spin_loan);
        list_loanemi = findViewById(R.id.list_loanemi);

        btn_download = findViewById(R.id.btn_download);
        btn_share = findViewById(R.id.btn_share);

        tvAuto_policy = findViewById(R.id.tvAuto_policy);
        ib_search = findViewById(R.id.ib_search);

        userClass = WCUserClass.getInstance();
    }

    private void setListener() {
        loan_emi_img.setOnClickListener(backListener);
/*
        spin_loan.setOnItemSelectedListener(loanListener);
*/
        btn_show_loan.setOnClickListener(showListener);

        btn_download.setOnClickListener(downloadListener);
        btn_share.setOnClickListener(shareListener);
        ib_search.setOnClickListener(searchListener);
    }


    private void getLoanEmiData(String loanId, String emiNo, String payDate, String payAmount, String principle, String interest) {
        listData.add(new WCLoanEmiData(loanId, emiNo, payDate, payAmount, principle, interest));
        setUpLoanEmiAdapter();

    }

    private void setUpLoanEmiAdapter() {
        loanEmiAdapter = new MyLoanEmiAdapter(this, listData);
        list_loanemi.setAdapter(loanEmiAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_loanemi.setLayoutManager(layoutManager);
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
                serviceForSearchLoanForRepaymentNonEmi(tvAuto_policy.getText().toString());

            }
        }
    };

/*    AdapterView.OnItemSelectedListener loanListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String loan_name;
            if (Utility.checkConnectivity(LoanEMI.this)) {
                loan_name = parent.getItemAtPosition(position).toString();
                serviceForSearchLoanForRepaymentNonEmi(loan_name);
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
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
            if (Utility.checkConnectivity(LoanEMI.this)) {
                serviceForEmiStatement();
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
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

            if (Utility.checkConnectivity(LoanEMI.this)) {
                if (li_header.getVisibility() == View.VISIBLE) {
                    int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(LoanEMI.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                            PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                    Toast.makeText(LoanEMI.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
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

            if (Utility.checkConnectivity(LoanEMI.this)) {
                if (li_header.getVisibility() == View.VISIBLE) {
                    int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(LoanEMI.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                                                    PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "Permission Needed!!", "Storage and Location permission needed..", "", new PopupCallBackOneButton() {
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
                            PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                            Uri uri= FileProvider.getUriForFile(LoanEMI.this, BuildConfig.APPLICATION_ID + ".provider",file);
                            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                    "Sharing File...");
                            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                            startActivity(Intent.createChooser(intentShareFile, "Share File"));
                        }else {

                            PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "Sorry!!", "You have to click download first..", "", new PopupCallBackOneButton() {
                                @Override
                                public void onFirstButtonClick() {

                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(LoanEMI.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                }
            } else {
                PopupClass.showPopUpWithTitleMessageOneButton(LoanEMI.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
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

        TextView tv_hh1 = headerView.findViewById(R.id.tv_hh1);
        TextView tv_hh2 = headerView.findViewById(R.id.tv_hh2);
        TextView tv_hh3 = headerView.findViewById(R.id.tv_hh3);
        TextView tv_hh4 = headerView.findViewById(R.id.tv_hh4);
        TextView tv_hh5 = headerView.findViewById(R.id.tv_hh5);
        TextView tv_hh6 = headerView.findViewById(R.id.tv_hh6);

        tv_hh1.setText("Loan ID");
        tv_hh2.setText("EMI No.");
        tv_hh3.setText("Date");
        tv_hh4.setText("Pay Amount");
        tv_hh5.setText("Particulars");
        tv_hh6.setText("Interest");


        tv_header.setText("Loan EMI List");
        tv_header2.setText("Loan ID.:");
        tv_header3.setText("Customer Name.");
        tv_header4.setVisibility(View.GONE);

        tv_acc_no.setText(edit_loan_id.getText().toString());
        tv_acc_name.setText(edit_customer_name.getText().toString());


        View rv_print_view_item = getLayoutInflater().inflate(R.layout.print_sb_query_list, new LinearLayout(this), false);

        TextView item_date = rv_print_view_item.findViewById(R.id.item_date);
        TextView item_trnno = rv_print_view_item.findViewById(R.id.item_trnno);
        TextView item_particulars = rv_print_view_item.findViewById(R.id.item_particulars);
        TextView item_deposit = rv_print_view_item.findViewById(R.id.item_deposit);
        TextView item_withdrawal = rv_print_view_item.findViewById(R.id.item_withdrawal);
        TextView item_balance = rv_print_view_item.findViewById(R.id.item_balance);


        for (int i = 0; i < loanEmiAdapter.getItemCount(); i++) {
            WCLoanEmiData summaryClass = listData.get(i);
            item_date.setText(summaryClass.getLoanId());
            item_trnno.setText(summaryClass.getEmiNo());
            item_particulars.setText(summaryClass.getDate());
            item_deposit.setText(summaryClass.getParticulars());
            item_withdrawal.setText(summaryClass.getParticulars());
            item_balance.setText(summaryClass.getInterest());

            PDFExportClass.addRVItemToView(i, rv_print_view_item, 1000, 500);
        }

        String file_name = "acc_statement_loan_emi_" + edit_loan_id.getText().toString();

        file = PDFExportClass.exportToPdfWithRecyclerView(headerView, 1000, 1200, list_loanemi,
                getString(R.string.company_name),file_name, PageSize.A4);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(LoanEMI.this, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                notificationHelper.createNotification("EMI Statement", "File Download successfully", intent);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoanEMI.this);
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
