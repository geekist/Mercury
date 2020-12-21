package com.ytech.common.gesture;

public interface OnGestureListener {
    void onDrag(float dx, float dy);

    void onFling(float startX, float startY, float velocityX, float velocityY);

    void onTapUp(float x, float y);

    void onScale(float scaleFactor, float beginFocusX, float beginFocusY,
                 float diffFocusX, float diffFocusY, float focusX, float focusY);
}
