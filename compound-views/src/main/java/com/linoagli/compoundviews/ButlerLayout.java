/**
 * CompoundViews Library Project.
 * com.linoagli.compoundviews
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.compoundviews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class ButlerLayout extends FrameLayout {
    private final long ANIMATION_DURATION = 200;

    private FrameLayout backdrop;
    private FrameLayout container;
    private int width;
    private int height;
    private boolean isViewPresented = false;

    public ButlerLayout(@NonNull Context context) {
        super(context);
    }

    public ButlerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButlerLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isViewPresented() {
        return isViewPresented;
    }

    public void presentView(View view) {
        if (isViewPresented) return; // TODO Maybe throw an exception?

        width = getWidth();
        height = (int) (getHeight() * 2f / 3f);

        LayoutParams params;

        backdrop = new FrameLayout(getContext());
        params = new LayoutParams(getWidth(), getHeight());
        backdrop.setLayoutParams(params);
        backdrop.setBackgroundColor(Color.parseColor("#77000000"));

        container = new FrameLayout(getContext());
        params = new LayoutParams(width, height);
        params.topMargin = getHeight();
        container.setLayoutParams(params);
        container.setBackgroundColor(Color.parseColor("#ffffff"));

        addView(backdrop);
        addView(container);
        container.addView(view);

        backdrop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPresentedView();
            }
        });

        ValueAnimator animator = ValueAnimator.ofInt(getHeight(), getHeight() - height);
        animator.setDuration(ANIMATION_DURATION);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backdrop.setAlpha(animation.getAnimatedFraction());

                LayoutParams params = (LayoutParams) container.getLayoutParams();
                params.topMargin = (int) animation.getAnimatedValue();
                container.setLayoutParams(params);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                isViewPresented = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        animator.start();
    }

    // TODO May need to handle soft input dismissal as well
    public void dismissPresentedView() {
        if (!isViewPresented) return; // TODO Maybe throw an exception?

        ValueAnimator animator = ValueAnimator.ofInt(getHeight() - height, 2 * getHeight());
        animator.setDuration(2 * ANIMATION_DURATION);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backdrop.setAlpha(1f - animation.getAnimatedFraction());

                LayoutParams params = (LayoutParams) container.getLayoutParams();
                params.topMargin = (int) animation.getAnimatedValue();
                container.setLayoutParams(params);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(container);
                removeView(backdrop);

                backdrop = null;
                container = null;
                isViewPresented = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        animator.start();
    }
}