package com.yizheng.inspirationrewards;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

public class CommonInfo {
    public final static String studentid = "1427953";
    public final static String baseUrl = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";

    public static void makeCustomToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(50, 25, 50, 25);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }

    static void setupHomeIndicator(ActionBar actionBar) {

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_with_logo);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    static void setupLogo(ActionBar actionBar) {

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.logo_2);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


}
