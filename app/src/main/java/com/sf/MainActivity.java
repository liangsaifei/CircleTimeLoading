package com.sf;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class MainActivity extends Activity {
    private TimeLoadingBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_time_loading);
        this. bar = (TimeLoadingBar) findViewById(R.id.time_loading_bar);
        bar.start();
    }


    @Override
    protected void onDestroy() {
        bar.stop();
        super.onDestroy();

    }
}

