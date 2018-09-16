package com.outplaysoftworks.sidedeck

import android.animation.ValueAnimator
import android.widget.TextView

/**
 * Helper class to animate TextViews displaying integers in an odometer style
 */

internal object AnimateTextView {

    /**
     * Animates a TextView displaying integers odometer style
     * @param initialValue Value to start at
     * @param finalValue Value to finish animation at
     * @param textview Target TextView
     * @param isZero If true uses longer animation duration to accommodate longer sound effects
     */
    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView, isZero: Boolean) {
        if (initialValue != finalValue) { //will not do anything if both values are equal
            val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
            if (isZero) {
                valueAnimator.duration = AppConstants.LPCHANGEANIMATIONDURATIONLONG.toLong()
            } else {
                valueAnimator.duration = AppConstants.LPCHANGEANIMATIONDURATION.toLong()
            }
            valueAnimator.addUpdateListener { valueAnimator1 -> textview.text = valueAnimator1.animatedValue.toString() }
            valueAnimator.start()
        }
    }
}
