package com.cmb_collector.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cmb_collector.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintAuthenticationHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    // Constructor
    public FingerprintAuthenticationHandler(Context mContext) {
        context = mContext;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject cryptoObject) {

        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);

    }

    //other finger save this method:
    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", true);

    }

    //device owner fingerprint check:
    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
        System.out.println("result..." + result);
    }


    public void update(String e, Boolean success) {
        TextView textView = ((Activity) context)
                .findViewById(R.id.textView);
        //   ImageView img = ((Activity)context).findViewById(R.id.img);

        textView.setText(e);
        if (success) {
            textView.setTextColor(ContextCompat.getColor(context,
                    R.color.colorPrimaryDark));
//            Intent i = new Intent(context, HomeActivity.class);
//            context.startActivity(i);
            Toast.makeText(context, "FingerPrint Saved Successfully", Toast.LENGTH_LONG).show();
            //img.setImageResource(R.drawable.ic_fingerprint_green);
            System.out.println("User fingerPrint...." + success);

        } else {
            Toast.makeText(context, "FingerPrint Not match for your device fingerprint", Toast.LENGTH_LONG).show();
            System.out.println("Not match fingerprint...." + success);
        }


    }


}
