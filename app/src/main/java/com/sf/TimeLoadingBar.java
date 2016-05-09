package com.sf;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author Saifei
 *         联系邮箱
 *         liangsaifei@163.com
 */
public class TimeLoadingBar extends View {

    private Paint circlePaint;
    private Paint minutePaint;
    private Paint hourPaint;
    private int strokeWidth = 6;
    private float defaultSize;
    private int mWidth;
    private int centerX;
    private int centerY;
    private float hourEndDegree = 30f;
    private float lastHourDegree = 0f;
    private float currMinuteDegree = 0f;
    private float currHourDegree = 0f;
    private ValueAnimator mMinuteValueAnimator;
    private ValueAnimator mHourValueAnimator;
    private int count = 1;
    private long duration;


    public TimeLoadingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                getContext().getResources().getDisplayMetrics());

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimeLoadingBar);
        int hourColor = ta.getColor(R.styleable.TimeLoadingBar_hour_color, Color.GRAY);
        int minuteColor = ta.getColor(R.styleable.TimeLoadingBar_minute_color, Color.GRAY);
        int circleColor = ta.getColor(R.styleable.TimeLoadingBar_circle_color, Color.GRAY);
        duration = (long) ta.getInteger(R.styleable.TimeLoadingBar_duration, 3000);
        ta.recycle();

        circlePaint = simplePaint(circleColor);
        circlePaint.setStrokeWidth(strokeWidth);

        minutePaint = simplePaint(minuteColor);
        minutePaint.setStrokeWidth(strokeWidth);

        hourPaint = simplePaint(hourColor);
        hourPaint.setStrokeWidth(strokeWidth);

        initMinuteAnimator();

        initHourAnimator();

    }

    private void initHourAnimator() {
        mHourValueAnimator = simpleAnimator(lastHourDegree, hourEndDegree);
        mHourValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currHourDegree = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private void initMinuteAnimator() {
        mMinuteValueAnimator = simpleAnimator(0f, 360f);
        mMinuteValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                hourEndDegree = count++ * 30;
                lastHourDegree = hourEndDegree;
            }
        });

        mMinuteValueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currMinuteDegree = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                }
        );

    }

    private Paint simplePaint(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    private ValueAnimator simpleAnimator(float from, float to) {
        ValueAnimator animator = ObjectAnimator.ofFloat(from, to);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        return animator;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, (mWidth - strokeWidth) / 2, circlePaint);
        canvas.rotate(currMinuteDegree, centerX, centerY);
        canvas.drawLine(centerX, centerY, centerX, centerY / 5, minutePaint);
        canvas.rotate(-currMinuteDegree, centerX, centerY);
        canvas.rotate(lastHourDegree + currHourDegree, centerX, centerY);
        canvas.drawLine(centerX, centerY, centerX, centerY / 3, hourPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size;

        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = (int) defaultSize;
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = (int) defaultSize;
        }

        size = Math.min(widthSize, heightSize);
        mWidth = size;
        centerX = centerY = size / 2;

        setMeasuredDimension(size, size);
    }

    public void start() {
        if (!mMinuteValueAnimator.isStarted()) {
            mMinuteValueAnimator.start();
            mHourValueAnimator.start();
        }

    }

    public void stop() {
        mMinuteValueAnimator.cancel();
        mHourValueAnimator.cancel();
    }


}
