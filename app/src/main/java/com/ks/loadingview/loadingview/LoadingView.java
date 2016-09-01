package com.ks.loadingview.loadingview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by ks on 16/9/1.
 */
public class LoadingView extends View {

    private Context mContext;
    private Paint mPaint;

    private int widthSpecSize;
    private int heightSpecSize;
    private int radiusSmall = 38;
    private int radiusBig = 76;
    private int moveX;
    private int xPoint;
    private Bitmap bitmapFailed;
    private Bitmap bitmapSuccess;
    private Rect bitmapRect;

    private int mState = -1;//0失败，1成功，-1默认
    private boolean mflag;

    private ValueAnimator animator;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Math.abs(moveX) > widthSpecSize * 5 / 4) {
            xPoint = (moveX < 0) ? xPoint = widthSpecSize * 7 / 4 - Math.abs(moveX) : widthSpecSize - widthSpecSize * 7 / 4 + Math.abs(moveX);
            canvas.drawCircle(xPoint, heightSpecSize / 2, radiusSmall, mPaint);
        }
        if (Math.abs(moveX) > widthSpecSize && Math.abs(moveX) < widthSpecSize * 3 / 2) {
            xPoint = (moveX < 0) ? xPoint = widthSpecSize * 3 / 2 - Math.abs(moveX) : widthSpecSize - widthSpecSize * 3 / 2 + Math.abs(moveX);
            canvas.drawCircle(xPoint, heightSpecSize / 2, radiusSmall, mPaint);
        }
        if (Math.abs(moveX) > widthSpecSize * 3 / 4 && Math.abs(moveX) < widthSpecSize * 5 / 4) {
            xPoint = (moveX < 0) ? xPoint = widthSpecSize * 5 / 4 - Math.abs(moveX) : widthSpecSize - widthSpecSize * 5 / 4 + Math.abs(moveX);
            canvas.drawCircle(xPoint, heightSpecSize / 2, radiusSmall, mPaint);
        }
        if (Math.abs(moveX) > widthSpecSize / 2 && Math.abs(moveX) < widthSpecSize) {
            xPoint = (moveX < 0) ? xPoint = widthSpecSize - Math.abs(moveX) : widthSpecSize - widthSpecSize * 4 / 4 + Math.abs(moveX);
            canvas.drawCircle(xPoint, heightSpecSize / 2, radiusSmall, mPaint);
        }
        if (Math.abs(moveX) > widthSpecSize / 4 && Math.abs(moveX) < widthSpecSize * 3 / 4) {
            xPoint = (moveX < 0) ? xPoint = widthSpecSize * 3 / 4 - Math.abs(moveX) : widthSpecSize - widthSpecSize * 3 / 4 + Math.abs(moveX);
            canvas.drawCircle(xPoint, heightSpecSize / 2, radiusSmall, mPaint);
        }
        if (Math.abs(moveX) > 0 && Math.abs(moveX) < widthSpecSize / 2) {
            xPoint = (moveX < 0) ? xPoint = widthSpecSize / 2 - Math.abs(moveX) : widthSpecSize - widthSpecSize / 2 + Math.abs(moveX);
            canvas.drawCircle(xPoint, heightSpecSize / 2, radiusSmall, mPaint);
        }
        //中间大圆
        if (Math.abs(moveX) >= 0 && Math.abs(moveX) < widthSpecSize * 5 / 4) {
            radiusBig = 2 * radiusSmall - radiusSmall * (Math.abs(moveX)) / (widthSpecSize * 5 / 4);
            radiusBig = (radiusBig > radiusSmall) ? radiusBig : radiusSmall;
            canvas.drawCircle(widthSpecSize / 2, heightSpecSize / 2, radiusBig, mPaint);
        }
        // 成功失败标志
        if (Math.abs(moveX) < 12 && mState >= 0) {
            if (mState == 0) {
                canvas.drawCircle(widthSpecSize / 2, heightSpecSize / 2, radiusBig, mPaint);
                canvas.drawBitmap(bitmapFailed, null, bitmapRect, mPaint);
            }
            if (mState == 1) {
                canvas.drawCircle(widthSpecSize / 2, heightSpecSize / 2, radiusBig, mPaint);
                canvas.drawBitmap(bitmapSuccess, null, bitmapRect, mPaint);
            }
        }
    }

    private  void init()
    {
        mPaint = new Paint();
        mPaint.setColor(0xFFFFBC53);
        mPaint.setAntiAlias(true);
        bitmapFailed=BitmapFactory.decodeResource(getContext().getResources(), R.drawable.connect_failed);
        bitmapSuccess=BitmapFactory.decodeResource(getContext().getResources(), R.drawable.connect_success);


    }
    public void start() {
        if (animator != null)
            animator.cancel();
        moveX = widthSpecSize * (-9 / 4);
        mState = -1;
        mflag = true;
        bitmapRect=new Rect(widthSpecSize / 2 - radiusBig, heightSpecSize / 2 - radiusBig, widthSpecSize / 2 +
                radiusBig, heightSpecSize / 2 + radiusBig);
        animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setDuration(3000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mState < 0) {
                    moveX = (moveX > widthSpecSize * 7 / 4) ? widthSpecSize * (-9 / 4) : moveX + 12;
                    if (Math.abs(moveX) < 12)
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                } else {
                    if (moveX > 0)
                        moveX = (moveX > widthSpecSize * 7 / 4) ? widthSpecSize * (-9 / 4) : moveX + 12;
                    else if (moveX < 0 && mflag) {
                        moveX += 12;
                        if (Math.abs(moveX) < 12)
                            mflag = false;
                    }
                }
                postInvalidate();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                animator.start();
                Looper.loop();
            }
        }).start();


    }

    public void success() {
        mState = 1;
    }

    public void failed() {
        mState = 0;
    }
}
