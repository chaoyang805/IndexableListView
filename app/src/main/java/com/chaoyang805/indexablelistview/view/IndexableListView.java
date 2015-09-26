package com.chaoyang805.indexablelistview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by chaoyang805 on 2015/9/26.
 */
public class IndexableListView extends ListView {

    private boolean mIsFastScrollEnabled = false;

    private IndexScroller mScroller;

    private GestureDetector mDetector;

    public IndexableListView(Context context) {
        this(context, null);
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public boolean isFastScrollEnabled() {
        return mIsFastScrollEnabled;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        super.setFastScrollEnabled(enabled);
        mIsFastScrollEnabled = enabled;
        if (mIsFastScrollEnabled) {
            if (mScroller == null) {
                mScroller = new IndexScroller(getContext(), this);
            }
        } else {
            if (mScroller != null) {
                mScroller.hide();
                mScroller = null;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mScroller != null) {
            mScroller.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mScroller != null && mScroller.onTouchEvent(ev)) {
            return true;
        }
        if (mDetector == null) {
            mDetector = new GestureDetector(getContext(),
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            mScroller.show();
                            return super.onFling(e1, e2, velocityX, velocityY);
                        }
                    });
        }
        mDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null) {
            mScroller.setAdapter(adapter);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null) {
            mScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }
}
