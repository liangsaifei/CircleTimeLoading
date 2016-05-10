package com.sf;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private List<String> textList;
    private MyAdapter adapter;
    private PullToRefreshListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_time_loading);

        listView = (PullToRefreshListView) findViewById(R.id.refreshlistview);
        textList = new ArrayList();
        for (int i = 0; i < 25; i++) {
            textList.add("这是一条ListView的数据" + i);
        }
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                for (int i = 0; i < 2; i++) {
                    textList.add(0, "这是下拉刷新出来的数据" + i);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter.notifyDataSetChanged();
                listView.hideHeaderView();
                Toast.makeText(MainActivity.this, "刷新完成，当前数据条数:" + textList.size(), Toast.LENGTH_SHORT).show();

            }
        }.execute();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return textList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return textList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = new TextView(MainActivity.this);
            textView.setText(textList.get(position));
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(18.0f);
            return textView;
        }

    }


}
