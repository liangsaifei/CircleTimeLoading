package com.sf;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class PullToRefreshListView extends ListView {


    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;

    private int currState = PULL_TO_REFRESH;

    private int headerViewHeight;
    private View headerView;
    private TimeLoadingBar timeLoadingBar;
    private TextView stateTv;
    private RelativeLayout.LayoutParams params;


    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {


        initHeaderView();


    }

    private void initHeaderView() {
        View view = View.inflate(getContext(), R.layout.header, null);
        headerView = view.findViewById(R.id.header_view);
        timeLoadingBar = (TimeLoadingBar) headerView.findViewById(R.id.time_loading_bar);
        stateTv = (TextView) headerView.findViewById(R.id.state_tv);
        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();
        params = (RelativeLayout.LayoutParams) headerView.getLayoutParams();
        params.setMargins(0, -headerViewHeight, 0, 0);
        headerView.setLayoutParams(params);
//        headerView.setPadding(0, -headerViewHeight, 0, 0);
        timeLoadingBar.start();

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
                int diff = moveY - downY;

                int marginTop = (-headerViewHeight + diff);
                if (getFirstVisiblePosition() == 0 && marginTop <= 0) {
                    System.out.println("headerViewHeight:" + headerViewHeight + "|marginTop:" + marginTop);
                    if (marginTop > 0 && currState == PULL_TO_REFRESH) {
                        System.out.println("松开就可以刷新");
                        refreshHeaderView();
                        currState = RELEASE_TO_REFRESH;
                    } else if (marginTop < 0 && currState == RELEASE_TO_REFRESH) {
                        System.out.println("下拉去刷新");
                        refreshHeaderView();
                        currState = PULL_TO_REFRESH;
                    }


                    setMargins(headerView, 0, marginTop, 0, 0);
                    return true;
                }


                break;
            case MotionEvent.ACTION_UP:
                if (currState == RELEASE_TO_REFRESH) {
                    currState = REFRESHING;
                    System.out.println("刷新中");
                    setMargins(headerView, 0, 0, 0, 0);
                    refreshHeaderView();

                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }


                } else if (currState == PULL_TO_REFRESH) {
                    System.out.println("下拉可以刷新，松开手指");
                    setMargins(headerView, 0, -headerViewHeight, 0, 0);

                }
                break;


        }


        return super.onTouchEvent(ev);


    }

    private void refreshHeaderView() {

        switch (currState) {
            case PULL_TO_REFRESH:

                stateTv.setText("下拉刷新");

                break;
            case RELEASE_TO_REFRESH:
                stateTv.setText("松手刷新");

                break;
            case REFRESHING:
                stateTv.setText("刷新中");
                break;
        }
    }


    interface OnRefreshListener {
        public void onRefresh();

    }

    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;


    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    public void hideHeaderView() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        currState = PULL_TO_REFRESH;
        stateTv.setText("下拉刷新");
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
