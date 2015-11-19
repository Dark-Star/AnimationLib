package com.darkstar.animationlib.interpolator;

import android.view.animation.Interpolator;

/**
 * Created by levy on 2015/9/10.
 */
public class UnderDampingInterpolator implements Interpolator {
    private static final String TAG = UnderDampingInterpolator.class.getSimpleName();

    private double mLambda;
    private double mOmega;

    /**
     * UnderDamping parameter
     * @param lambda the bigger means the stronger of resistance, which cause the curve converge
     *               faster
     * @param omega the bigger means the shorter of period of cos function, which cause more times
     *              of oscillation in final position
     */
    public UnderDampingInterpolator(double lambda, double omega) {
        mLambda = lambda;
        mOmega = omega;
    }

    @Override
    public float getInterpolation(float input) {
        float r = 1.0f - (float) (Math.pow(Math.E, - mLambda * input) * Math.cos(mOmega * input));
        return r;
    }
}
