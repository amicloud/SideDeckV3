package com.outplaysoftworks.sidedeck;

import android.animation.ValueAnimator;
import android.widget.TextView;

/**
 * Helper class to animate TextViews displaying integers in an odometer style
 */

class AnimateTextView {

    /**
     * Animates a TextView displaying integers odometer style
     * @param initialValue Value to start at
     * @param finalValue Value to finish animation at
     * @param textview Target TextView
     * @param isZero If true uses longer animation duration to accommodate longer sond effects
     */
    static void animateTextView(int initialValue, int finalValue, final TextView textview, boolean isZero) {
        if (initialValue != finalValue) { //will not do anything if both values are equal
            ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
            if(isZero){
                valueAnimator.setDuration(AppConstants.LPCHANGEANIMATIONDURATIONLONG);
            } else{
                valueAnimator.setDuration(AppConstants.LPCHANGEANIMATIONDURATION);
            }
            valueAnimator.addUpdateListener(valueAnimator1 -> textview.setText(valueAnimator1.getAnimatedValue().toString()));
            valueAnimator.start();
        }
    }
}
