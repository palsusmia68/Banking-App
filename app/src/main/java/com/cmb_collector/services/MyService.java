package com.cmb_collector.services;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cmb_collector.activity.LogActivity;
import com.cmb_collector.model.WCUserClass;
import com.cmb_collector.utility.Utility;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service implements LocationListener, OnMapReadyCallback {
    boolean isGPSEnable=false, isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler myHandler = new Handler();
    private Handler myHandlerr = new Handler();
    private Timer myTimer = null;//60000 300000 900000 3000
    long timeInterval =  900000;
    private Timer mmyTimer = null;//60000 300000 900000
    long ttimeInterval = 900000;
    public static String str_receiver = "com.bracesmedia.tracking";
    Intent intent;
    public static final String SERVICE = "";
    public static final String KEY_INT_FROM_SERVICE = "KEY_INT_FROM_SERVICE";
    public static final String ACTION_UPDATE_CNT = "UPDATE_CNT";
    public static final String KEY_STRING_FROM_SERVICE = "KEY_STRING_FROM_SERVICE";
    public static final String ACTION_UPDATE_MSG =  "UPDATE_MSG";
    final static String KEY_MSG_TO_SERVICE = "KEY_MSG_TO_SERVICE";
    final static String ACTION_MSG_TO_SERVICE = "MSG_TO_SERVICE";
    MyServiceReceiver myServiceReceiver;
    MyServiceThread myServiceThread;
    int cnt;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private int PERMISSION_REQUEST_CODE = 100;
    String stringLatitude;
    String stringLongitude;
    String country;
    String city;
    String postalCode;
    String addressLine;
    String time1,Date;
    String addressLinee;
    String offlineAddress;
    boolean mAllowRebind;
    ExampleService exampleService;
    String runtime;
    private WCUserClass userClass;
    String userflag ="0";
    Calendar calander;
    SimpleDateFormat simpledateformat;
    @Override
    public void onCreate() {
        super.onCreate();
        myServiceReceiver = new MyServiceReceiver();
        myTimer = new Timer();
        myTimer.schedule(new TimerTaskToGetLocation(), 1, timeInterval);
        mmyTimer = new Timer();
        mmyTimer.schedule(new Logg(), 1, ttimeInterval);
        intent = new Intent(str_receiver);
        exampleService =new ExampleService();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
            }
        }
        else{

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        System.out.println("STARTSERVICE"+"ok");
        userClass = WCUserClass.getInstance();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MSG_TO_SERVICE);
        registerReceiver(myServiceReceiver, intentFilter);
        myServiceThread = new MyServiceThread();
        myServiceThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onDestroy"+"onBind!");
        if(myServiceReceiver!=null)
            unregisterReceiver(myServiceReceiver);
        return null;

    }
    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("onDestroy"+"onUnbind!");
        return mAllowRebind;
    }
    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {
        Log.e("onDestroy", "onRebind!");
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("onDestroy"+"onStart!");
    }
    public class ExampleService extends IntentService {
        public ExampleService() {
            super("example-service");
        }
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            System.out.println("onDestroy"+"onHandleIntent!");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy"+"0");

    }

    private void out(){
        userClass = WCUserClass.getInstance();
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("HH:mm");
        String time = simpledateformat.format(calander.getTime());
        GPSTracker gpsTracker = new GPSTracker(this);
        stringLatitude = String.valueOf(gpsTracker.latitude);
        stringLongitude = String.valueOf(gpsTracker.longitude);
        country = gpsTracker.getCountryName(this);
        city = gpsTracker.getLocality(this);
        postalCode = gpsTracker.getPostalCode(this);
        addressLine = gpsTracker.getAddressLine(this);
        offlineAddress = country+" "+city+" "+" "+postalCode+" "+addressLine;
        String usercode = userClass.getGlobalUserCode();
        System.out.println("PUT_InsertLocationout"+offlineAddress+" "+stringLatitude+" "+stringLongitude+" "+usercode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_InsertLocation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("PUT_InsertLocationout"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);


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
                Map<String, String> params = new HashMap<String, String>();
                params.put("ArrangerCode", usercode);
                params.put("Edate", Utility.setCurrentDate());
                params.put("ETime", time);
                params.put("EType", "O");
                params.put("Lat", stringLatitude);
                params.put("Lang", stringLongitude);
                params.put("Adrs", offlineAddress);
                System.out.println("PUT_InsertLocationout" + params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 1, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public boolean isNetworkAvailable() {
        boolean connect=false;
        ConnectivityManager conMgr =  (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            connect=false;
        }else{
            connect= true;
        }
        return connect;
    }
    public void off() {
        LocationManager loc = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (loc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
    }

    public class MyServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_MSG_TO_SERVICE)){
                String msg = intent.getStringExtra(KEY_MSG_TO_SERVICE);
                msg = new StringBuilder(msg).reverse().toString();
                //send back to Profile_Activity
                Intent i = new Intent();
                i.setAction(ACTION_UPDATE_MSG);
                i.putExtra(KEY_STRING_FROM_SERVICE, msg);
                sendBroadcast(i);
            }
        }
    }
    private class MyServiceThread extends Thread{
        private boolean running;
        public void setRunning(boolean running){
            this.running = running;
        }
        @Override
        public void run() {
            cnt = 0;
            running = true;
            while (running){
                try {
                    Thread.sleep(1000);
                    Intent intent = new Intent();
                    intent.setAction(ACTION_UPDATE_CNT);
                    intent.putExtra(KEY_INT_FROM_SERVICE, cnt);
                    sendBroadcast(intent);
                    cnt++;
                    if (isNetworkAvailable()){
                    }
                    else {
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getLocationInfo(){
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnable && !isNetworkEnable){

        } else {
            if (isNetworkEnable) {
//                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, (LocationListener) this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        System.out.println("latitude"+location.getLatitude() + "");
                        System.out.println("longitude"+ location.getLongitude() + "");
                        //  Toast.makeText(this,"Location"+location.getLatitude()+" "+location.getLongitude(),Toast.LENGTH_SHORT).show();
                        time1 = new SimpleDateFormat("HH:mm").format(location.getTime());
                        Date = new SimpleDateFormat("yyyy-MM-dd").format(location.getTime());
                        GPSTracker gpsTracker = new GPSTracker(this);
                        stringLatitude = String.valueOf(gpsTracker.latitude);
                        stringLongitude = String.valueOf(gpsTracker.longitude);
                        country = gpsTracker.getCountryName(this);
                        city = gpsTracker.getLocality(this);
                        postalCode = gpsTracker.getPostalCode(this);
                        addressLine = gpsTracker.getAddressLine(this);
                        //  Toast.makeText(getApplicationContext()," " +stringLatitude+ ""+stringLongitude+ ""+country+""+city+""+postalCode+ ""+addressLine+ "service lati", Toast.LENGTH_SHORT).show();
                        System.out.println("gps"+stringLatitude+ "," +stringLongitude+ ","+country+ ","+city+""+postalCode+""+addressLine);
                        //   addressLinee =  country+ ","+city+", "+postalCode+", "+addressLine;
                        addressLinee =  addressLine;
                        if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
                            System.out.println("Location1"+"Time GPS: " + time1); // This is what we want!
                        else
                            Log.e("Location1", "Time Device (" + location.getProvider() + "): " + time1);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                        runtime = time.format(c.getTime());
                        Log.e("Rtime",runtime);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        System.out.println("Service"+", "+latitude +longitude);
                        serviceUpdateLocation(location);
                        getCompleteAddressString(latitude,longitude);

                    }
                }
                if (isGPSEnable) {
                    //   location = null;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, (LocationListener) this);
                    if (locationManager!=null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location!=null){
                            System.out.println("latitude"+location.getLatitude()+"");
                            System.out.println("longitude"+location.getLongitude()+"");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            System.out.println("Service1"+", "+location.getLatitude()+" " +location.getLongitude()+" "+"Loc"+latitude+" "+","+longitude);
                            //  getAddressFromLocation(latitude,longitude, new GeocoderHandler());
                           // final String time2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(location.getTime());
                            final String time2 = new SimpleDateFormat("HH:mm:ss").format(location.getTime());
                            if( location.getProvider().equals(LocationManager.GPS_PROVIDER))
                                System.out.println("Location"+ "Time GPS: " + time2); // This is what we want!
                            else
                                System.out.println("Location"+ "Time Device (" + location.getProvider() + "): " + time2);
                            serviceUpdateLocation(location);
                            getCompleteAddressString(latitude,longitude);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceConnector.base_URL + "PUT_InsertLocation",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            System.out.println("PUT_InsertLocation"+response);
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);


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
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("ArrangerCode", userClass.getGlobalUserCode());
                                    params.put("Edate", Utility.setCurrentDate());
                                    params.put("ETime", runtime);
                                    params.put("EType", "R");
                                    params.put("Lat", String.valueOf(latitude));
                                    params.put("Lang", String.valueOf(latitude));
                                    params.put("Adrs", offlineAddress);
                                    System.out.println("PUT_InsertLocation" + params.toString());
                                    return params;
                                }
                            };
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConnector.MY_SOCKET_TIME, 1, 0));
                            RequestQueue requestQueue = Volley.newRequestQueue(this);
                            requestQueue.add(stringRequest);

                        }
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
    private class Logg extends TimerTask{
        @Override
        public void run() {
            myHandlerr.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }
    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    userflag = userClass.getFlag();
                    System.out.println("FLAGDATA"+userflag);
                    if (userflag.equals("1")){
                        getLocationInfo();
                    }
                }
            });

        }
    }
    public void autoLogout(){
        CountDownTimer timer = new CountDownTimer(15 *60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                //Logout
                Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent);
            }
        };
    }
    private void serviceUpdateLocation(Location location){
        intent.putExtra("latutide",location.getLatitude()+"");
        intent.putExtra("longitude",location.getLongitude()+"");
        sendBroadcast(intent);
        System.out.println("cityyy"+" "+location.getLatitude()+" "+location.getLongitude());
        getCompleteAddressString(location.getLatitude(),location.getLongitude());
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                offlineAddress = strReturnedAddress.toString();
                System.out.println("aadd"+offlineAddress);
                System.out.println("My Current loction"+time1+strReturnedAddress.toString());
            } else {
                System.out.println("My Current loction"+time1+"No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("My Current loction"+time1+"Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onLocationChanged(Location loc){
        locationManager.removeUpdates(this);
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        getCompleteAddressString(loc.getLatitude(),loc.getLongitude());
        System.out.println("LOCATIONCHANGE"+loc.getLatitude()+" "+loc.getLongitude());
        String cityName = null;
        String locality = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0)
//                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
             // locality = addresses.get(1).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + " " + latitude + "My Currrent City is:"
                + cityName;
        addressLinee = cityName;
        System.out.println("city"+s+" "+addressLinee);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }
}