package com.chaoyang805.indexablelistview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Created by chaoyang805 on 2015/9/26.
 */
public class IndexScroller {

    private static final int STATE_HIDDEN = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOWN = 2;
    private static final int STATE_HIDING = 3;
    /**
     * 索引条的宽度
     */
    private float mIndexbarWidth;
    /**
     * 索引条距离右侧边缘的距离
     */
    private float mIndexbarMargin;
    /**
     * 在中心显示的预览文本到四周的距离
     */
    private float mPreviewPadding;
    /**
     * 当前屏幕密度除以160
     */
    private float mDensity;
    /**
     * 当前屏幕密度除以160(设置字体尺寸)
     */
    private float mScaleDensity;
    /**
     * 透明度，用于显示和隐藏索引条
     */
    private float mAlphaRate;
    /**
     * 索引条当前的状态
     */
    private int mState = STATE_HIDDEN;
    /**
     * listview的宽度
     */
    private int mListViewWidth;
    /**
     * listview的高度
     */
    private int mListViewHeight;
    /**
     * 当前的section
     */
    private int mCurrentSection = -1;
    /**
     * 是否正在索引中
     */
    private boolean mIsIndexing = false;
    private ListView mListView = null;
    private SectionIndexer mIndexer = null;
    private String[] mSections = null;
    private RectF mIndexbarRect;

    public IndexScroller(Context context, ListView listView) {
        mListView = listView;
        //获得屏幕密度的比值
        mDensity = context.getResources().getDisplayMetrics().density;
        mScaleDensity = context.getResources().getDisplayMetrics().scaledDensity;
        setAdapter(mListView.getAdapter());
        mIndexbarWidth = 20 * mDensity;
        mIndexbarMargin = 10 * mDensity;
        mPreviewPadding = 5 * mDensity;

    }

    public void hide() {
        if (mState == STATE_SHOWING) {
            setState(STATE_HIDING);
        }
    }

    private void setState(int state) {
        if (state < STATE_HIDDEN || state > STATE_HIDING) {
            return ;
        }
        mState = state;
        switch (mState) {
            case STATE_HIDDEN:
                mHandler.removeMessages(0);
                break;
            case STATE_SHOWING:
                mAlphaRate = 0;
                fade(0);
                break;
            case STATE_SHOWN:
                mHandler.removeMessages(0);
                break;
            case STATE_HIDING:
                mAlphaRate = 1;
                fade(3000);
                break;
        }
    }

    private void fade(long delay) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis() + delay);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (mState) {
                case STATE_HIDDEN:
                    mHandler.removeMessages(0);
                    break;
                case STATE_SHOWING:
                    mAlphaRate += (1 - mAlphaRate) * 0.2;//malphaRate + 0.2 - 0.2 * mAlphaRate;
                    if (mAlphaRate > 0.9) {
                        mAlphaRate = 1;
                        setState(STATE_SHOWN);
                    }
                    mListView.invalidate();
                    fade(10);
                    break;
                case STATE_SHOWN:
                    setState(STATE_HIDING);
                    break;
                case STATE_HIDING:
                    mAlphaRate -= mAlphaRate * 0.2;
                    if (mAlphaRate < 0.1) {
                        mAlphaRate = 0;
                        setState(STATE_HIDDEN);
                    }
                    mListView.invalidate();
                    fade(10);
                    break;

            }
        }
    };

    public void draw(Canvas canvas) {
        //绘制索引条和索引条的背景和文本
        if (mState == STATE_HIDDEN) {
            return;
        }
        Paint indexbarPaint = new Paint();
        indexbarPaint.setColor(Color.BLACK);
        indexbarPaint.setAlpha((int) (mAlphaRate * 64));
        canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);
        //绘制预览文本
        if (mSections != null && mSections.length > 0) {
            if (mCurrentSection >= 0) {
                Paint previewPaint = new Paint();
                previewPaint.setColor(Color.BLACK);
                previewPaint.setAlpha(96);

                Paint previewTextPaint = new Paint();
                previewTextPaint.setColor(Color.WHITE);
                previewTextPaint.setTextSize(50 * mScaleDensity);
                float previewTextWidth = previewTextPaint.measureText(mSections[mCurrentSection]);
                float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();

                RectF previewRect = new RectF((mListViewWidth - previewSize) / 2,
                        (mListViewHeight - previewSize) / 2,
                        (mListViewWidth - previewSize) / 2 + previewSize,
                        (mListViewHeight - previewSize) / 2 + previewSize);
                canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
                canvas.drawText(mSections[mCurrentSection],
                        previewRect.left + (previewSize - previewTextWidth) / 2 - 1,
                        previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1,
                        previewTextPaint);
            }
        }
        //
        Paint indexPaint = new Paint();
        indexPaint.setColor(Color.WHITE);
        indexPaint.setAlpha((int) (255 * mAlphaRate));
        indexPaint.setTextSize(12 * mScaleDensity);
        float sectionHeight = (mIndexbarRect.height() - mIndexbarMargin * 2) / mSections.length;
        float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
        for (int i = 0; i < mSections.length; i++) {
            float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
            canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft,
                    mIndexbarRect.top + mIndexbarMargin + i * sectionHeight + paddingTop - indexPaint.ascent(),
                    indexPaint);

        }

    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {
                    setState(STATE_SHOWN);
                    mIsIndexing = true;
                    mCurrentSection = getSectionByPoint(ev.getY());
                    mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing) {
                    if (contains(ev.getX(), ev.getY())) {
                        mCurrentSection = getSectionByPoint(ev.getY());
                        mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsIndexing) {
                    mIsIndexing = false;
                    mCurrentSection = -1;
                }
                if (mState == STATE_SHOWN) {
                    setState(STATE_HIDING);

                }
                break;
        }
        return false;
    }

    private int getSectionByPoint(float y) {
        if (mSections == null || mSections.length == 0) {
            return 0;
        }
        if (y < mIndexbarRect.top + mIndexbarMargin) {
            return 0;
        }
        if (y > mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin) {
            return mSections.length - 1;
        }
        return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
    }

    private boolean contains(float x, float y) {
        return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
    }

    public void show() {
        if (mState == STATE_HIDING) {
            setState(STATE_HIDING);
        }else if (mState == STATE_HIDDEN) {
            setState(STATE_SHOWING);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof SectionIndexer) {
            mIndexer = (SectionIndexer) adapter;
            mSections = (String[]) mIndexer.getSections();
        }
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mListViewWidth = w;
        mListViewHeight = h;
        mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth,
                mIndexbarMargin,w - mIndexbarMargin,h - mIndexbarMargin);
    }
}
