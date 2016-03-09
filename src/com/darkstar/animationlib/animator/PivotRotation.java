package com.darkstar.animationlib.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import com.darkstar.animationlib.interpolator.CubicBezierInterpolator;

/**
 * Created by levy on 2015/12/9.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PivotRotation extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener,
    Animator.AnimatorListener {
    private static final String TAG = PivotRotation.class.getSimpleName();

    private View mTarget;
    private Point mCenter;
    private Point mPivot;
    private double mRadius;
    private Interpolator mInterpolator = new CubicBezierInterpolator(.39, .59, .69, .49);

    public void rotateAround(View target, Point pivot) {
        rotateAround(target, pivot, 10000);
    }

    public void rotateAround(View target, Point pivot, int duration) {
        rotateAround(target, pivot, duration, mInterpolator);
    }

    public void rotateAround(View target, Point pivot, int duration, Interpolator interpolator) {
        if (target == null || pivot == null) return;
        // save pivot
        mPivot = pivot;
        // save target
        mTarget = target;
        mCenter = getCenter(target);
        mRadius = Math.sqrt(Math.pow(mCenter.x - pivot.x, 2) + Math.pow(mCenter.y - pivot.y, 2));
        Log.d(TAG, "pivot = " + pivot + "center = " + mCenter + " radius = " + mRadius);

        double theta = Math.asin((mCenter.y - pivot.y) / mRadius);
        theta = theta + ((mCenter.x > pivot.x) ? 0 : Math.PI);
        setTarget(target);
        setFloatValues((float) theta, (float) (theta + Math.PI * 2));
        setRepeatCount(INFINITE);
        setDuration(duration);
        setInterpolator(interpolator);
        addUpdateListener(this);
        addListener(this);
        start();
    }

    private Point getCenter(View target) {
        int[] location = new int[2];
        target.getLocationOnScreen(location);
        return new Point(location[0] + target.getWidth() / 2, location[1] + target.getHeight() / 2);
    }

    private void release() {
        removeUpdateListener(this);
        mTarget = null;
        mPivot = null;
        mCenter = null;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float theta = (float) animation.getAnimatedValue();
        mTarget.setTranslationX((float) (mPivot.x + Math.cos(theta) * mRadius - mCenter.x));
        mTarget.setTranslationY((float) (mPivot.y - Math.sin(theta) * mRadius - mCenter.y));
    }

    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationEnd(Animator animation) {
        release();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        release();
    }

    @Override
    public void onAnimationRepeat(Animator animation) {}
}
