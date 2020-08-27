package com.mattech.task_list.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.annotation.NonNull;
import android.view.View;

public class ViewAnimator {

    public static void animateViewBounce(View view) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).setDuration(400).start();
    }

    public static void animateViewAppearance(@NonNull View view) {
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.setAlpha(0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).setDuration(400).start();
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
    }

    public static void animateViewDisappearance(@NonNull View view) {
        view.setClickable(false);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f);
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).setDuration(400).start();
    }
}
