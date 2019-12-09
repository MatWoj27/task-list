package com.mattech.task_list;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.widget.TextView;

public class ViewAnimator {

    public static void animateTextChange(TextView textView, String newText) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(textView, scaleX, scaleY, alpha).setDuration(800).start();
        textView.setText(newText);
    }

    public static void animateViewAppearance(View view) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).setDuration(400).start();
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
    }

    public static void animateViewDisappearance(View view) {
        view.setClickable(false);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f);
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).setDuration(400).start();
    }
}