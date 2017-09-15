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
    public enum PresentationMode { FromLeft, FromTop, FromRight, FromBottom }

    private FrameLayout backdrop;
    private FrameLayout container;
    private Options options = new Options();
    private boolean isViewPresented = false;

    private int containerWidth;
    private int containerHeight;

    public ButlerLayout(@NonNull Context context) {
        super(context);
    }

    public ButlerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButlerLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Options getOptions() {
        return options;
    }

    public boolean isViewPresented() {
        return isViewPresented;
    }

    public void presentView(View view) {
        if (isViewPresented) return; // TODO Maybe throw an exception?

        makeBackdrop();
        makeContainer();

        addView(backdrop);
        addView(container);
        container.addView(view);

        backdrop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPresentedView();
            }
        });
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do nothing
            }
        });

        makePresentationAnimator().start();
    }

    public void dismissPresentedView() {
        if (!isViewPresented) return; // TODO Maybe throw an exception?

        makeDismissAnimator().start();
    }

    private void makeBackdrop() {
        backdrop = new FrameLayout(getContext());
        LayoutParams params = new LayoutParams(getWidth(), getHeight());
        backdrop.setLayoutParams(params);
        backdrop.setBackgroundColor(Color.parseColor(options.backdropBackgroundColor));
        backdrop.setAlpha(0);
    }

    private void makeContainer() {
        if (options.presentationMode == PresentationMode.FromLeft || options.presentationMode == PresentationMode.FromRight) {
            containerWidth = (int) (getWidth() * options.sizeRatio);
            containerHeight = getHeight();
        } else {
            containerWidth = getWidth();
            containerHeight = (int) (getHeight() * options.sizeRatio);
        }

        container = new FrameLayout(getContext());
        LayoutParams params = new LayoutParams(containerWidth, containerHeight);

        if (options.presentationMode == PresentationMode.FromLeft) params.leftMargin = -containerWidth;
        if (options.presentationMode == PresentationMode.FromTop) params.topMargin = -containerHeight;
        if (options.presentationMode == PresentationMode.FromRight) params.leftMargin = getWidth();
        if (options.presentationMode == PresentationMode.FromBottom) params.bottomMargin = getHeight();

        container.setLayoutParams(params);
        container.setBackgroundColor(Color.parseColor(options.containerBackgroundColor));
    }

    private ValueAnimator makePresentationAnimator() {
        ValueAnimator animator;

        if (options.presentationMode == PresentationMode.FromLeft) {
            animator = ValueAnimator.ofInt(-containerWidth, 0);
        } else if (options.presentationMode == PresentationMode.FromTop) {
            animator = ValueAnimator.ofInt(-containerHeight, 0);
        } else if (options.presentationMode == PresentationMode.FromRight) {
            animator = ValueAnimator.ofInt(getWidth(), getWidth() - containerWidth);
        } else {
            animator = ValueAnimator.ofInt(getHeight(), getHeight() - containerHeight);
        }

        animator.setDuration(options.animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backdrop.setAlpha(animation.getAnimatedFraction());

                int margin = (int) animation.getAnimatedValue();
                LayoutParams params = (LayoutParams) container.getLayoutParams();

                if (options.presentationMode == PresentationMode.FromLeft) params.leftMargin = margin;
                if (options.presentationMode == PresentationMode.FromTop) params.topMargin = margin;
                if (options.presentationMode == PresentationMode.FromRight) params.leftMargin = margin;
                if (options.presentationMode == PresentationMode.FromBottom) params.topMargin = margin;

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

        return animator;
    }

    private ValueAnimator makeDismissAnimator() {
        ValueAnimator animator;

        if (options.presentationMode == PresentationMode.FromLeft) {
            animator = ValueAnimator.ofInt(0, -2 * containerWidth);
        } else if (options.presentationMode == PresentationMode.FromTop) {
            animator = ValueAnimator.ofInt(0, -2 * containerHeight);
        } else if (options.presentationMode == PresentationMode.FromRight) {
            animator = ValueAnimator.ofInt(getWidth() - containerWidth, 2 * getWidth());
        } else {
            animator = ValueAnimator.ofInt(getHeight() - containerHeight, 2 * getHeight());
        }

        animator.setDuration(2 * options.animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backdrop.setAlpha(1f - animation.getAnimatedFraction());

                int margin = (int) animation.getAnimatedValue();
                LayoutParams params = (LayoutParams) container.getLayoutParams();

                if (options.presentationMode == PresentationMode.FromLeft) params.leftMargin = margin;
                if (options.presentationMode == PresentationMode.FromTop) params.topMargin = margin;
                if (options.presentationMode == PresentationMode.FromRight) params.leftMargin = margin;
                if (options.presentationMode == PresentationMode.FromBottom) params.topMargin = margin;

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

        return animator;
    }

    public static class Options {
        private PresentationMode presentationMode = PresentationMode.FromBottom;
        private long animationDuration = 200;
        private float sizeRatio = .75f;
        private String backdropBackgroundColor = "#77000000";
        private String containerBackgroundColor = "#ffffff";

        public Options setPresentationMode(PresentationMode mode) {
            this.presentationMode = mode;
            return this;
        }

        public Options setAnimationDuration(long duration) {
            this.animationDuration = duration;
            return this;
        }

        public Options setSizeRatio(float ratio) {
            this.sizeRatio = ratio;
            return this;
        }

        public Options setBackdropBackgroundColor(String color) {
            this.backdropBackgroundColor = color;
            return this;
        }

        public Options setContainerBackgroundColor(String color) {
            this.containerBackgroundColor = color;
            return this;
        }
    }
}