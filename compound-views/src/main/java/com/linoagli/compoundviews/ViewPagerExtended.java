/**
 * Compound Views Library Project.
 * com.linoagli.compoundviews
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.compoundviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerExtended extends ViewPager {
    private boolean isSwipeGestureEnabled = true;

    public ViewPagerExtended(Context context) {
        super(context);
    }

    public ViewPagerExtended(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isSwipeGestureEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSwipeGestureEnabled && super.onInterceptTouchEvent(ev);
    }

    public boolean isSwipeGestureEnabled() {
        return isSwipeGestureEnabled;
    }

    public void setSwipeGestureEnabled(boolean isSwipeGestureEnabled) {
        this.isSwipeGestureEnabled = isSwipeGestureEnabled;
    }
}