package com.example.ipcplayer.widget;

import android.view.animation.Interpolator;

/**
 * An interpolator where the rate of change starts out quickly and 
 * and then decelerates.
 *
 */
public class ElasticInterpolator implements Interpolator {
	private final float mTension;
	/**
     * Constructor
     * 
     * @param factor Degree to which the animation should be eased. Seting factor to 1.0f produces
     *        an upside-down y=x^2 parabola. Increasing factor above 1.0f makes exaggerates the
     *        ease-out effect (i.e., it starts even faster and ends evens slower)
     */
    public ElasticInterpolator(float tension) {
        mTension = tension;
    }
       
    public float getInterpolation(float t) {
        // _o(t) = t * t * ((tension + 1) * t + tension)
        // o(t) = _o(t - 1) + 1
        t -= 1.0f;
        return t * t * t * t * ((mTension + 1) * t + mTension) + 1.0f;
    }
}
