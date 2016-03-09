package com.darkstar.animationlib.ui;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.TextView;
import com.darkstar.animationlib.interpolator.CubicBezierInterpolator;

/**
 * Created by levy on 2015/12/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PivotRotationView extends TextView implements
    ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = PivotRotationView.class.getSimpleName();

    private Point mCenter;
    private Point mPivot;
    private double mRadius;
    private ValueAnimator mAnimator;
    private Interpolator mInterpolator = new CubicBezierInterpolator(.39, .59, .69, .49);

    public PivotRotationView(Context context) {
        super(context);
    }

    public PivotRotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PivotRotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        mCenter = new Point(location[0] + getWidth() / 2, location[1] + getHeight() / 2);
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void rotateAround(Point pivot, int duration) {
        stopAround();
        mPivot = pivot;
        mRadius = Math.sqrt(Math.pow(mCenter.x - pivot.x, 2) + Math.pow(mCenter.y - pivot.y, 2));
        Log.d(TAG, "pivot = " + pivot + "center = " + mCenter + " radius = " + mRadius);

        double theta = Math.asin((mCenter.y - pivot.y) / mRadius);
        theta = theta + ((mCenter.x > pivot.x) ? 0 : Math.PI);
        mAnimator = ValueAnimator.ofFloat((float) theta, (float) (theta + Math.PI * 2));
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(mInterpolator);
        mAnimator.setDuration(duration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float theta = (float) animation.getAnimatedValue();
                setTranslationX((float) (mPivot.x + Math.cos(theta) * mRadius - mCenter.x));
                setTranslationY((float) (mPivot.y - Math.sin(theta) * mRadius - mCenter.y));
            }
        });
        mAnimator.start();
    }

    public void stopAround() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }
}
