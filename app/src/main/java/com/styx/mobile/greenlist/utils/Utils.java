package com.styx.mobile.greenlist.utils;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by amal.george on 16-01-2017.
 */

public class Utils {
    public static final String CONST_IMAGE_DIRECTORY = "images";
    public static final int REQUEST_PLACE_PICKER = 1000;
    public static final int REQUEST_IMAGE_PICKER = 1001;
    public static final float PARAMETER_FLOAT_EMPTY = -1f;
    public static final long PARAMETER_LONG_EMPTY = -1;


    public static void animationIn(final View view, final int animation, int delayTime, final Context context) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation inAnimation = AnimationUtils.loadAnimation(
                        context.getApplicationContext(), animation);
                view.setAnimation(inAnimation);
                view.setVisibility(View.VISIBLE);
            }
        }, delayTime);
    }

    public static void animationOut(final View view, final int animation, int delayTime, final boolean isViewGone, final Context context) {
        view.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation outAnimation = AnimationUtils.loadAnimation(
                        context.getApplicationContext(), animation);
                view.setAnimation(outAnimation);
                if (isViewGone)
                    view.setVisibility(View.GONE);
                else
                    view.setVisibility(View.INVISIBLE);
            }
        }, delayTime);
    }

    public static void startActivityWithClipReveal(Intent intent, Context context, View view) {
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            options = ActivityOptions.makeClipRevealAnimation(view, 0, 0,
                    view.getWidth(), view.getHeight());
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }


}