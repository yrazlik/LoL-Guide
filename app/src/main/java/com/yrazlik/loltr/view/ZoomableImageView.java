package com.yrazlik.loltr.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class ZoomableImageView extends ImageView
{
    Matrix matrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int CLICK = 3;
    static final int SCROLL = 4;
    int mode = NONE;

    float minScale = 1f;
    float maxScale = 4f;
    float[] m;

    float redundantXSpace, redundantYSpace;
    float width, height;
    float saveScale = 1f;
    float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

    ScaleGestureDetector mScaleDetector;
    Context context;

    public ZoomableImageView(Context context) {
        super(context);
        super.setClickable(true);
        this.context = context;
        init();
    }

    public ZoomableImageView(Context context, AttributeSet attr)
    {
        super(context, attr);
        super.setClickable(true);
        this.context = context;
        init();
    }

    private SparseArray<PointF> mActivePointers = new SparseArray<>();

    private void init() {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix.setTranslate(1f, 1f);
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        setOnTouchListener(new OnTouchListener()
        {

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                mScaleDetector.onTouchEvent(event);

                //test

                // get pointer index from the event object
                int pointerIndex = event.getActionIndex();

                // get pointer ID
                int pointerId = event.getPointerId(pointerIndex);

                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];

                switch (event.getActionMasked())
                {
                    //when one finger is touching
                    //set the mode to DRAG
                    case MotionEvent.ACTION_DOWN:
                        PointF f = new PointF();
                        f.x = event.getX(pointerIndex);
                        f.y = event.getY(pointerIndex);

                        mActivePointers.put(pointerId, f);

                        break;
                    //when two fingers are touching
                    //set the mode to ZOOM
                    case MotionEvent.ACTION_POINTER_DOWN:
                        PointF f2 = new PointF();
                        f2.x = event.getX(pointerIndex);
                        f2.y = event.getY(pointerIndex);

                        mActivePointers.put(pointerId, f2);

                        mode = ZOOM;
                        break;
                    //when a finger moves
                    //If mode is applicable move image
                    case MotionEvent.ACTION_MOVE:

                        float deltaX = 0;// x difference
                        float deltaY = 0;// y difference

                        for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                            PointF point = mActivePointers.get(event.getPointerId(i));
                            if (point!=null) {
                                deltaX += event.getX(i) - point.x;// x difference
                                deltaY += event.getY(i) - point.y;// y difference

                                point.x = event.getX(i);
                                point.y = event.getY(i);
                            }
                        }

                        if (mode==DRAG) {
                            //deltaX gt 0 means pointer movement direction left to right and
                            //x==0 means bmp is reached end of left edge
                            //than notify the parent view touch event
                            //
                            if ((x == 0 && deltaX > 0) || (x == -right && deltaX < 0)) {
                                mode = NONE;
                                setImageMatrix(matrix);
                                invalidate();
                                return true;
                            }
                        } else if (mode==NONE && saveScale > minScale) {
                            if ((x == 0 && deltaX < 0) || (x == -right && deltaX > 0)) {
                                mode = DRAG;
                            }
                        }

                        //if the mode is ZOOM or
                        //if the mode is DRAG and already zoomed
                        if (mode == ZOOM || (mode == DRAG && saveScale > minScale)) {

                            float scaleWidth = Math.round(origWidth * saveScale);// width after applying current scale
                            float scaleHeight = Math.round(origHeight * saveScale);// height after applying current scale

                            //if scaleWidth is smaller than the views width
                            //in other words if the image width fits in the view
                            //limit left and right movement
                            if (scaleWidth < width)
                            {
                                deltaX = 0;
                                if (y + deltaY > 0)
                                    deltaY = -y;
                                else if (y + deltaY < -bottom)
                                    deltaY = -(y + bottom);
                            }
                            //if scaleHeight is smaller than the views height
                            //in other words if the image height fits in the view
                            //limit up and down movement
                            else if (scaleHeight < height)
                            {
                                deltaY = 0;
                                if (x + deltaX > 0)
                                    deltaX = -x;
                                else if (x + deltaX < -right)
                                    deltaX = -(x + right);
                            }
                            //if the image doesnt fit in the width or height
                            //limit both up and down and left and right
                            else
                            {
                                if (x + deltaX > 0)
                                    deltaX = -x;
                                else if (x + deltaX < -right)
                                    deltaX = -(x + right);

                                if (y + deltaY > 0)
                                    deltaY = -y;
                                else if (y + deltaY < -bottom)
                                    deltaY = -(y + bottom);
                            }
                            //move the image with the matrix
                            matrix.postTranslate(deltaX, deltaY);

                            setImageMatrix(matrix);
                            invalidate();
                            return false;
                        }
                        break;
                    //first finger is lifted
                    case MotionEvent.ACTION_UP:
//                        if (saveScale > minScale) {
//                            mode = DRAG;
//                        } else {
//                            mode = NONE;
//                        }
//                        int xDiff = (int) Math.abs(curr.x - start.x);
//                        int yDiff = (int) Math.abs(curr.y - start.y);
//                        if (xDiff < CLICK && yDiff < CLICK)
//                            performClick();
                        break;
                    // second finger is lifted
                    case MotionEvent.ACTION_POINTER_UP:
                        if (saveScale > minScale) {
                            mode = DRAG;
                        } else {
                            mode = NONE;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mode = NONE;
                        break;
                }
                setImageMatrix(matrix);
                invalidate();
                return mode!=NONE?false:true;
            }

        });
    }

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
    }

    public void setMaxZoom(float x)
    {
        maxScale = x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale)
            {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            }
            else if (saveScale < minScale)
            {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }
            right = width * saveScale - width - (2 * redundantXSpace * saveScale);
            bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
            if (origWidth * saveScale <= width || origHeight * saveScale <= height)
            {
                matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
                if (mScaleFactor < 1)
                {
                    matrix.getValues(m);
                    float x = m[Matrix.MTRANS_X];
                    float y = m[Matrix.MTRANS_Y];
                    if (mScaleFactor < 1)
                    {
                        if (Math.round(origWidth * saveScale) < width)
                        {
                            if (y < -bottom)
                                matrix.postTranslate(0, -(y + bottom));
                            else if (y > 0)
                                matrix.postTranslate(0, -y);
                        }
                        else
                        {
                            if (x < -right)
                                matrix.postTranslate(-(x + right), 0);
                            else if (x > 0)
                                matrix.postTranslate(-x, 0);
                        }
                    }
                }
            }
            else
            {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (mScaleFactor < 1) {
                    if (x < -right)
                        matrix.postTranslate(-(x + right), 0);
                    else if (x > 0)
                        matrix.postTranslate(-x, 0);
                    if (y < -bottom)
                        matrix.postTranslate(0, -(y + bottom));
                    else if (y > 0)
                        matrix.postTranslate(0, -y);
                }
            }
            return true;
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        //Fit to screen.
        float scale;
        float scaleX =  width / bmWidth;
        float scaleY = height / bmHeight;
        scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);
        setImageMatrix(matrix);
        saveScale = 1f;

        // Center the image
        redundantYSpace = height - (scale * bmHeight) ;
        redundantXSpace = width - (scale * bmWidth);
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = width - 2 * redundantXSpace;
        origHeight = height - 2 * redundantYSpace;
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        setImageMatrix(matrix);
    }

    public boolean canScroll() {
        return mode!=NONE;
    }
}