package com.demo.cl.mapbox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.cl.mapbox.R;
import com.demo.cl.mapbox.utils.ScreenUtils;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class BottomFly extends LinearLayout {

    private static final int MAX_CLICK_TIME = 250;

    private static float MAX_CLICK_DISTANCE = 5;

    private float shrinkHeight;

    private boolean isDragged = false;

    float currentY = Float.NaN;

    float downY = Float.NaN;
    float downX = Float.NaN;

    public static int STATE_CLOSE = 1;
    public static int STATE_DRAG = 2;
    public static int STATE_EXPANDED = 3;

    private int state = STATE_CLOSE;

    private long mPressStartTime;

    private View panel = getChildAt(0);


    public BottomFly(@NonNull Context context) {
        super(context);
    }

    public BottomFly(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setGravity(VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomFly);
        shrinkHeight = a.getDimension(R.styleable.BottomFly_panel_shrink_height, (int)(ScreenUtils.dp2px(getContext(), 55)));
        a.recycle();
    }

    public BottomFly(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        panel = getChildAt(0);
        //close panel when initialize
        if (panel != null) {
            panel.layout(0, (int)(getMeasuredHeight() - shrinkHeight), getMeasuredWidth(), (int)(panel.getMeasuredHeight() + getMeasuredHeight() - shrinkHeight));
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isTouchPointInView(panel, (int) event.getRawX(), (int) event.getRawY()) && !isDragged) {
            return false;
        }

        switch (event.getAction()) {
            case ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                currentY = downY;
                isDragged = false;
                mPressStartTime = System.currentTimeMillis();
                break;
            }
            case ACTION_MOVE: {
                isDragged = true;
                canRecyclerViewScroll(event);
                if (isDragged) {
                    state = STATE_DRAG;
                } else {
                    state = STATE_EXPANDED;
                }
                break;
            }
            case ACTION_UP: {
                break;
            }
        }
        return isDragged;
    }

    private void canRecyclerViewScroll(MotionEvent event) {
        if (panel instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) panel;
            RecyclerView recyclerView = null;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof RecyclerView) {
                    recyclerView = (RecyclerView) viewGroup.getChildAt(i);
                    break;
                }
            }
            if (recyclerView != null && isTouchPointInView(recyclerView, (int) downX, (int) downY) && recyclerView.canScrollVertically((int) (downY - event.getY()))) {
                Log.e("onInterceptTouchEvent", "false," + event.getAction());
                isDragged = false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchPointInView(panel, (int) event.getRawX(), (int) event.getRawY()) && !isDragged) {
            Log.e("onTouchEvent", "false," + event.getAction() + "," + isDragged);
            return false;
        }
        switch (event.getAction()) {
            case ACTION_DOWN: {
                break;
            }
            case ACTION_MOVE: {
                isDragged = true;
                requestDisallowInterceptTouchEvent(true);
                float deltay = event.getY() - currentY;
                currentY = event.getY();
                //stick at top or bottom
                if (panel.getY() + deltay <= 0) {
                    panel.setY(0);
                    state = STATE_EXPANDED;
                } else if (panel.getY() + deltay >= getMeasuredHeight() - shrinkHeight) {
                    panel.setY(getMeasuredHeight() - shrinkHeight);
                    state = STATE_CLOSE;
                } else {
                    panel.offsetTopAndBottom((int) deltay);
                }

                break;
            }
            case ACTION_UP: {
                requestDisallowInterceptTouchEvent(false);
                long pressDuration = System.currentTimeMillis() - mPressStartTime;

                //To see if it's a click event
                if (pressDuration < MAX_CLICK_TIME &&
                        distance(downX, downY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
                    if (state == STATE_EXPANDED) {
                        state = STATE_CLOSE;
                        closePanel();
                    } else if (state == STATE_CLOSE) {
                        state = STATE_EXPANDED;
                        expandPanel();
                    }
                } else {
                    //play animation to either expand or close state
                    if (panel.getY() <= (getMeasuredHeight() - shrinkHeight) / 2) {
                        expandPanel();
                    } else {
                        closePanel();
                    }
                }


                break;
            }
        }
        return true;

    }


    public void closePanel() {
        state = STATE_CLOSE;
        panel.animate().setInterpolator(new OvershootInterpolator()).y(getMeasuredHeight() - shrinkHeight);
    }

    public void expandPanel() {
        state = STATE_EXPANDED;
        panel.animate().setInterpolator(new AnticipateInterpolator()).y(0);
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return y >= top && y <= bottom && x >= left
                && x <= right;
    }


    private double distance(float x1, float y1, float x2, float y2) {
        float deltaX = x1 - x2;
        float deltaY = y1 - y2;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

}



