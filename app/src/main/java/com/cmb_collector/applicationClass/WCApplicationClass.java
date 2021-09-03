package com.cmb_collector.applicationClass;

import android.app.Activity;
import android.app.Application;
import android.os.Messenger;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class WCApplicationClass extends Application {
    private Activity currentActivity = null;
    private Messenger resultMessenger;
    private Messenger errorMessenger;

    public static final String TAG = WCApplicationClass.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // Store Current Display Activity Refference......

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void clearReferenceActivity(Activity mActivity) {
        Activity curActivity = getCurrentActivity();
        if (mActivity.equals(curActivity)) {
            setCurrentActivity(null);
        }
    }


    //For Image Download....


}
