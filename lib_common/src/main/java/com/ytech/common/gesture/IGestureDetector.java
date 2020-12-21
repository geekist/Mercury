package com.ytech.common.gesture;

import android.view.MotionEvent;

public interface IGestureDetector {
    boolean onTouchEvent(MotionEvent ev);

    boolean isDragging();

    boolean isScaling();

    void setOnGestureListener(OnGestureListener listener);
}
