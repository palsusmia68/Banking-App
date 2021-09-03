package com.cmb_collector.activity;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.cmb_collector.adapter.MyAccountSummaryAdapter;
import com.cmb_collector.model.WCAccountSummaryClass;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.services.ServiceConnector;
import com.cmb_collector.utility.AppBaseClass;
import com.cmb_collector.utility.CustomProgressDialog;
import com.cmb_collector.utility.NotificationHelper;
import com.cmb_collector.utility.PDFExportClass;
import com.cmb_collector.utility.Utility;
import com.itextpdf.text.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AccountSummary extends AppBaseClass {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    final private int REQUEST_CODE_DOWNLOAD_PERMISSIONS = 222;
    private static Image image;
    private ImageView new_acc_back_img;
  //  private Spinner acc_no;
 //   private TextView  tv_acc_name;

   // private AutoCompleteTextView et_acc_name;
  //  private ImageButton ib_search;

    private Button btn_show, btn_download,btn_share;

    private RelativeLayout rl_last_10_trans;
    private RecyclerView recy_last_10;

    private MyAccountSummaryAdapter adapter;

    private ArrayList<String> accountNameList = new ArrayList<>();
    private ArrayList<WCAccountSummaryClass> summaryClassArrayList = new ArrayList<>();

    private TextView from_date;
    private TextView to_date;
    private Spinner spin_sbAcNo;
    private  String getsbAccount;
    private ArrayList<String> SBAccountList = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener dateFrom;
    private DatePickerDialog.OnDateSetListener dateTo;

    private Calendar myFromDate = Calendar.getInstance();
    private Calendar myToDate = Calendar.getInstance();

    private WCUserClass userClass;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBodyContentView(R.layout.activity_account_summary);

        init();
        setDefaultValues();
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
                            ArrayAdapter<String> spinnerSBAmount = new ArrayAdapter<String>(AccountSummary.this, R.layout.spinner_item, SBAccountList);
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
        final CustomProgressDialog dialog = new CustomProgressDialog(AccountSummary.this);
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
//        acc_no.setAdapter(adapterMember);
     //   et_acc_name.setAdapter(adapterMember);
    }

    private void setListener() {
        new_acc_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkConnectivity(AccountSummary.this)) {
                    serviceForLoadSavingStatement();
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }
            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.checkConnectivity(AccountSummary.this)) {
                    if (rl_last_10_trans.getVisibility() == View.VISIBLE) {
                        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(AccountSummary.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                                PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                        Toast.makeText(AccountSummary.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
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

                if (Utility.checkConnectivity(AccountSummary.this)) {
                    if (rl_last_10_trans.getVisibility() == View.VISIBLE) {
                        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(AccountSummary.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                                PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "Permission Needed!!", "Storage permission needed..", "", new PopupCallBackOneButton() {
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
                                Uri uri=FileProvider.getUriForFile(AccountSummary.this, BuildConfig.APPLICATION_ID + ".provider",file);
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                        "Sharing File...");
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
                            }else {

                                PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "Sorry!!", "You have to click download first..", "", new PopupCallBackOneButton() {
                                    @Override
                                    public void onFirstButtonClick() {

                                    }
                                });
                            }
                        }
                    } else {
                        Toast.makeText(AccountSummary.this, "Statement not loaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "", "Sorry!!!Internet Connection needed", "", new PopupCallBackOneButton() {
                        @Override
                        public void onFirstButtonClick() {

                        }
                    });
                }

            }
        });

//        ib_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utility.checkConnectivity(AccountSummary.this)) {
//                    serviceForACC_SplitDetails(et_acc_name.getText().toString());
//                } else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
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
//                if (Utility.checkConnectivity(AccountSummary.this)) {
//                    rl_last_10_trans.setVisibility(View.GONE);
//                    serviceForACC_SplitDetails(selectedItem);
//                } else {
//                    PopupClass.showPopUpWithTitleMessageOneButton(AccountSummary.this, "OK", "", "Sorry!!Internet Connection needed", "", new PopupCallBackOneButton() {
//                        @Override
//                        public void onFirstButtonClick() {
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
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

    public void generatePDF() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.print_account_statement, new LinearLayout(this), false);
        TextView tv_ac_no = headerView.findViewById(R.id.tv_ac_no);
        TextView tv_ac_name = headerView.findViewById(R.id.tv_ac_name);
        TextView tv_from_date = headerView.findViewById(R.id.tv_from_date);
        TextView tv_to_date = headerView.findViewById(R.id.tv_to_date);

      //  tv_ac_no.setText(tv_acc_no.getText().toString());
     //   tv_ac_name.setText(tv_acc_name.getText().toString());
        tv_from_date.setText(Utility.changeDateFormat("yyyyMMdd", "dd/MM/yyyy", from_date.getText().toString()));
        tv_to_date.setText(Utility.changeDateFormat("yyyyMMdd", "dd/MM/yyyy", to_date.getText().toString()));

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

      //  String file_name = "acc_statement_" + tv_acc_no.getText().toString() + "_" + from_date.getText().toString() + "_" + to_date.getText().toString();

//        file = PDFExportClass.exportToPdfWithRecyclerView(headerView, 1000, 1200, recy_last_10,
//                getString(R.string.company_name),file_name, PageSize.A4);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(AccountSummary.this, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                notificationHelper.createNotification("Account Statement", "File Download successfully", intent);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AccountSummary.this);
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

    private void serviceForLoadSavingStatement() {
        final CustomProgressDialog dialog = new CustomProgressDialog(AccountSummary.this);
        dialog.showLoader();
        StringRequest str = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "GET_ACC_SavingsStatement", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("AccountStatement "+response);
               // Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                dialog.dismissDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("AccountStatement");
                    summaryClassArrayList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        WCAccountSummaryClass summaryClass = new WCAccountSummaryClass();

                        summaryClass.setAccNo(jsonObject.optString("AccountNo"));
                        summaryClass.setDate(jsonObject.optString("Tdate"));
                        summaryClass.setTransactionNo(jsonObject.optString("Trnno"));
                        summaryClass.setNarration(jsonObject.optString("Type"));
                        summaryClass.setDebit(jsonObject.optString("Deposit"));
                        summaryClass.setCredit(jsonObject.optString("WithDrawal"));
                        summaryClass.setAmount(jsonObject.optString("Balence"));

                        summaryClassArrayList.add(summaryClass);

                    }
                    rl_last_10_trans.setVisibility(View.VISIBLE);
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
                params.put("FromDate", from_date.getText().toString());
                params.put("ToDate", to_date.getText().toString());
                System.out.println("params summary "+params.toString());
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 0, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void serviceForACC_SplitDetails(final String acc_no) {
        final CustomProgressDialog dialog = new CustomProgressDialog(AccountSummary.this);
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
                     //   tv_acc_no.setText(jsonObject.optString("ACCOUNTNO"));
                     //   tv_acc_name.setText(jsonObject.optString("AccountHolderName"));
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

    private void setDefaultValues() {
        dateFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myFromDate.set(Calendar.YEAR, year);
                myFromDate.set(Calendar.MONTH, monthOfYear);
                myFromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                FromDateFormat();
            }
        };

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AccountSummary.this, dateFrom,
                        myFromDate.get(Calendar.YEAR),
                        myFromDate.get(Calendar.MONTH),
                        myFromDate.get(Calendar.DAY_OF_MONTH)).
                        show();
            }
        });

        dateTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myToDate.set(Calendar.YEAR, year);
                myToDate.set(Calendar.MONTH, monthOfYear);
                myToDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                ToDateFormat();
            }
        };

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AccountSummary.this, dateTo,
                        myToDate.get(Calendar.YEAR),
                        myToDate.get(Calendar.MONTH),
                        myToDate.get(Calendar.DAY_OF_MONTH)).
                        show();
            }
        });

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c);

        from_date.setText(formattedDate);
        to_date.setText(formattedDate);
    }

    private void init() {
       // tv_acc_no = findViewById(R.id.tv_acc_no);
    //    tv_acc_name = findViewById(R.id.tv_acc_name);
        new_acc_back_img = findViewById(R.id.new_acc_back_img);
       // acc_no = findViewById(R.id.acc_no);
        rl_last_10_trans = findViewById(R.id.rl_last_10_trans);
        recy_last_10 = findViewById(R.id.recy_last_10);

        from_date = findViewById(R.id.from_date);
        to_date = findViewById(R.id.to_date);
        spin_sbAcNo = findViewById(R.id.spin_sbAcNo);

     //   et_acc_name = findViewById(R.id.et_acc_name);
     //   ib_search = findViewById(R.id.ib_search);
        btn_download = findViewById(R.id.btn_download);
        btn_show = findViewById(R.id.btn_show);
        btn_share = findViewById(R.id.btn_share);

        userClass = WCUserClass.getInstance();
    }

    private void setUpTransactionUpdate() {
        adapter = new MyAccountSummaryAdapter(summaryClassArrayList, AccountSummary.this);
        recy_last_10.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recy_last_10.setLayoutManager(layoutManager);

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

    private void FromDateFormat() {
        String myformat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        from_date.setText(sdf.format(myFromDate.getTime()));
    }

    private void ToDateFormat() {
        String myformat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        to_date.setText(sdf.format(myToDate.getTime()));
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
