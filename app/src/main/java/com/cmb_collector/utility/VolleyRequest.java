package com.cmb_collector.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    Context context;
    String url;
    JSONObject object;
    ResponseListener listener;
    ErrorListener errorListener;
    String message;
    boolean showDialog;
    View dialogView;
    static String errorMsg;
    Map<String, String> params;

    public VolleyRequest(Context context, String url, HashMap<String, String> params, boolean showDialog, ResponseListener listener, ErrorListener errorListener) {

        this.context = context;
        this.url = url;
        this.message = "Please wait...";
        this.listener = listener;
        this.errorListener = errorListener;
        this.showDialog = showDialog;
        this.params = params;
        if (isInternetAvailable())
            getDataWithDialogWithParams();
        else
            Toast.makeText(context, "Network Not available", Toast.LENGTH_SHORT).show();
    }

    public synchronized void getDataWithDialogWithParams() {
        final CustomProgressDialog dialog = new CustomProgressDialog(context);
        if (showDialog) {
            dialog.showLoader();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponseReceive(response);
                        if (showDialog) {
                            dialog.dismissDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                returnErrorMsg(error);
                errorListener.onErrorReceive(errorMsg);
                if (showDialog) {
                    dialog.dismissDialog();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache(false);
        //   jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);
    }


    public synchronized boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if network is NOT available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public interface ResponseListener {
        public void onResponseReceive(String response);
    }

    public interface ErrorListener {
        public void onErrorReceive(String response);
    }

    public static final String returnErrorMsg(VolleyError error) {
        if (error instanceof TimeoutError) {
            errorMsg = "Server Timeout";
        } else if (error instanceof NoConnectionError) {
            errorMsg = "No network connection found";
        } else if (error instanceof AuthFailureError) {
            errorMsg = "Authentication Failure";
        } else if (error instanceof ServerError) {
            errorMsg = "Server down";
        } else if (error instanceof NetworkError) {
            errorMsg = "No internet";
        } else if (error instanceof ParseError) {
            errorMsg = "Parsing Failure";
        } else {
            errorMsg = "No internet";
        }
        return errorMsg;
    }
}