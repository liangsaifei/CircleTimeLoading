package com.sf;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Saifei
 */
public class HeaderView extends LinearLayout {
    private TimeLoadingBar loadingBar;
    private TextView stateTv;
    private LinearLayout container;
    private int height;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
    }


    private void init(Context context) {

        View rootView = View.inflate(context, R.layout.header, null);
        container = (LinearLayout) rootView.findViewById(R.id.container);
        loadingBar = (TimeLoadingBar) rootView.findViewById(R.id.time_loading_bar);
        stateTv = (TextView) rootView.findViewById(R.id.state_tv);
        setGravity(Gravity.CENTER);
        addView(rootView);
    }

    public void setStateText(String state) {
        stateTv.setText(state);
    }

    public void setMarginTop(int margin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
        layoutParams.setMargins(0, margin, 0, 0);
        container.setLayoutParams(layoutParams);
    }


    public void show() {
        setMarginTop(0);
        loadingBar.start();
    }

    public void hide() {
        setMarginTop(-height);
        loadingBar.stop();
        stateTv.setText("下拉刷新");
    }

    public int getHeaderHeight() {
        measure(0, 0);
        height = getMeasuredHeight();
        setMarginTop(-height);
        return height;

    }
}
