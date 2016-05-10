package com.sf;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @author Saifei
 */
public class PullToRefreshListView extends ListView {


    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;

    private int currState = PULL_TO_REFRESH;

    private int headerViewHeight;
    private HeaderView headerView;


    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initHeaderView(context);
    }

    private void initHeaderView(Context context) {
        headerView = new HeaderView(context);
        headerViewHeight = headerView.getHeaderHeight();
        addHeaderView(headerView);
    }

    int downY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int distance = moveY - downY;

                int marginTop = -headerViewHeight + distance;
                if (getFirstVisiblePosition() == 0 && distance > 0) {
                    if (marginTop > 0 && currState == PULL_TO_REFRESH) {
                        currState = RELEASE_TO_REFRESH;
                    } else if (marginTop < 0 && currState == RELEASE_TO_REFRESH) {
                        currState = PULL_TO_REFRESH;
                    }
                    refreshHeaderView();
                    headerView.setMarginTop(marginTop);
                    return true;
                }


                break;
            case MotionEvent.ACTION_UP:
                if (currState == RELEASE_TO_REFRESH) {
                    currState = REFRESHING;

                    headerView.show();
                    refreshHeaderView();

                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }


                } else if (currState == PULL_TO_REFRESH) {
                    headerView.hide();

                }
                break;


        }


        return super.onTouchEvent(ev);


    }

    private void refreshHeaderView() {

        switch (currState) {
            case PULL_TO_REFRESH:

                headerView.setStateText("下拉刷新");

                break;
            case RELEASE_TO_REFRESH:
                headerView.setStateText("释放刷新");

                break;
            case REFRESHING:
                headerView.setStateText("刷新中");
                break;
        }
    }


    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;


    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    public void hideHeaderView() {
        currState = PULL_TO_REFRESH;
        headerView.hide();
    }


}
