package com.ytech.common.gesture;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class FroyoGestureDetector extends EclairGestureDetector {
    protected final ScaleGestureDetector mDetector;

    private float mBeginFocusX;
    private float mBeginFocusY;
    private float mMovingFocusX;
    private float mMovingFocusY;

    public FroyoGestureDetector(Context context) {
        super(context);

        ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();

                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }

                float focusX = detector.getFocusX();
                float focusY = detector.getFocusY();
                float diffFocusX = focusX - mMovingFocusX;
                float diffFocusY = focusY - mMovingFocusY;
                mMovingFocusX = focusX;
                mMovingFocusY = focusY;
                mListener.onScale(scaleFactor, mBeginFocusX, mBeginFocusY, diffFocusX, diffFocusY,
                        focusX, focusY);
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                mBeginFocusX = detector.getFocusX();
                mBeginFocusY = detector.getFocusY();
                mMovingFocusX = mBeginFocusX;
                mMovingFocusY = mBeginFocusY;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                // NO-OP
            }
        };
        mDetector = new ScaleGestureDetector(context, mScaleListener);
    }

    @Override
    public boolean isScaling() {
        return mDetector.isInProgress();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }
}
