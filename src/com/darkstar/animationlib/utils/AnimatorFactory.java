package com.darkstar.animationlib.utils;

import android.animation.*;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import com.darkstar.animationlib.interpolator.CubicBezierInterpolator;
import com.darkstar.animationlib.interpolator.UnderDampingInterpolator;

/**
 * Created by levy on 2015/9/6.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class AnimatorFactory {

    private static ObjectAnimator shake(View view, float shakeFactor) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
            Keyframe.ofFloat(0f, 1f), Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f), Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f), Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f), Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f), Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
            Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f),
            Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f),
            Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f),
            Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f),
            Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.1f, -3f * shakeFactor),
            Keyframe.ofFloat(.2f, -3f * shakeFactor),
            Keyframe.ofFloat(.3f, 3f * shakeFactor),
            Keyframe.ofFloat(.4f, -3f * shakeFactor),
            Keyframe.ofFloat(.5f, 3f * shakeFactor),
            Keyframe.ofFloat(.6f, -3f * shakeFactor),
            Keyframe.ofFloat(.7f, 3f * shakeFactor),
            Keyframe.ofFloat(.8f, -3f * shakeFactor),
            Keyframe.ofFloat(.9f, 3f * shakeFactor),
            Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate)
            .setDuration(1000);
    }

    /**
     *
     * @param view
     * @param pivotX
     * @param pivotY
     * @return
     */
    public static Animator photoDrop(final View view, int pivotX, int pivotY) {
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
        int w = view.getWidth();
        int h = view.getHeight();

        float degree = (float) Math.toDegrees(Math.atan2(w, h));
        boolean clockwise = pivotX < w / 2;

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofFloat(View.ROTATION, 0,
            clockwise ? degree : -degree);
        ObjectAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(view, pvhRotate);
        rotation.setInterpolator(new UnderDampingInterpolator(4, 40));
        rotation.setDuration(2000);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, h * 2);
        translationY.setStartDelay(600);
        translationY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translationY);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setTranslationY(0);
                view.setRotation(0);
            }
        });

        return set;
    }

    public static Animator fadein(final View view) {
        Animator animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
        animator.setDuration(500);
        animator.setInterpolator(new CubicBezierInterpolator(.42,0,1,1));
        return animator;
    }

    public static Animator paperDropDownIn(final View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        final int w = view.getWidth();
        int h = view.getHeight();

        Animator show = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y,
            -location[1] - h,
            -location[1] - h / 2);
        show.setDuration(200);
        show.setInterpolator(new CubicBezierInterpolator(.67,.2,1,.7));

        view.setPivotX(w / 2);
        view.setPivotY(0);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -location[1]
         - h / 2, 0);
        PropertyValuesHolder pvhR = PropertyValuesHolder.ofKeyframe(View.ROTATION,
            Keyframe.ofFloat(0, 0),
            Keyframe.ofFloat(.4f, 40),
            Keyframe.ofFloat(.5f, 0),
            Keyframe.ofFloat(.8f, -30),
            Keyframe.ofFloat(1, 0));

        Animator fall = ObjectAnimator.ofPropertyValuesHolder(view, pvhR, pvhY).setDuration(800);
        fall.setInterpolator(new CubicBezierInterpolator(0,.43,.65,.98));

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(show, fall);
        return set;
    }

    public static Animator paperDropDownOut(View view) {
        final int w = view.getWidth();
        int h = view.getHeight();

        view.setPivotX(w / 2);
        view.setPivotY(0);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0,
            h * 4);
        PropertyValuesHolder pvhR = PropertyValuesHolder.ofKeyframe(View.ROTATION,
            Keyframe.ofFloat(0, 0),
            Keyframe.ofFloat(.3f, 30),
            Keyframe.ofFloat(.4f, 0),
            Keyframe.ofFloat(.7f, -18),
            Keyframe.ofFloat(1, 0));
        Animator dropout = ObjectAnimator.ofPropertyValuesHolder(view, pvhR, pvhY).setDuration
            (1000);
        dropout.setInterpolator(new CubicBezierInterpolator(.67,.2,1,.7));
        return dropout;
    }
}
