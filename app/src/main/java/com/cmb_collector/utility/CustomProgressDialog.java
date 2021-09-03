package com.cmb_collector.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.cmb_collector.R;

public class CustomProgressDialog {

    private ProgressBar progressBar;
    private ImageView iv_dialog;
    private LinearLayout li_layout;
    private Context context;
    private ShowDialogClass showDialogClass;

    public CustomProgressDialog(Context context) {
        this.context = context;
    }

    public void showLoader() {
        showDialogClass = new ShowDialogClass(context);
        showDialogClass.show();
    }

    public void dismissDialog() {
        if (showDialogClass != null & showDialogClass.isShowing()) {
            showDialogClass.dismiss();
        }
    }

    class ShowDialogClass extends Dialog {

        public ShowDialogClass(Context context) {
            super(context, R.style.TransParentProgressDialog);

            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            getWindow().setAttributes(wlmp);
            setTitle(null);
            setCancelable(false);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setGravity(Gravity.CENTER);
            layout.setOrientation(LinearLayout.VERTICAL);
            //layout.setBackgroundResource(R.drawable.xml_red_box);
            layout.setPadding(5, 5, 5, 5);
            int screeSize = context.getResources().getDisplayMetrics().heightPixels;
            int imgHeightWidth = (int) (screeSize * 0.08);
            //
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imgHeightWidth, imgHeightWidth);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            iv_dialog = new ImageView(context);
//            iv_dialog.setImageResource(R.drawable.loader_red);
            progressBar = new ProgressBar(context);
            progressBar.setIndeterminate(true);
            progressBar.setBackgroundResource(R.drawable.xml_white_box);
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            layout.addView(progressBar, params);
//            TextView tv_loading = new TextView(context);
//            tv_loading.setText("Loading...");
//            //tv_loading.setTextColor(Color.WHITE);
//            tv_loading.setTextColor(Color.parseColor("#2478A7"));
//            tv_loading.setTextSize(11);
//            tv_loading.setPadding(1, (int) (screeSize * 0.009),1,1);
//            layout.addView(tv_loading,new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT));

            int heightWidth = (int) (screeSize * 0.15);

            LinearLayout.LayoutParams li_params = new LinearLayout.LayoutParams(heightWidth, heightWidth);
            addContentView(layout, li_params);
        }

        @Override
        public void show() {
            super.show();
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(3000);
            progressBar.setAnimation(anim);
            progressBar.startAnimation(anim);
        }

    }

}
