package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.itheima.mobilesafe.R;

public class BlacklistActivity extends Activity implements View.OnClickListener {

    private ListView mLvBlacklist;
    private ImageButton mIbAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        initView();
        initData();

    }



    private void initView() {
        mLvBlacklist = (ListView) findViewById(R.id.lv_blacklist);
        mIbAdd = (ImageButton) findViewById(R.id.ib_add_blacklist);
        mIbAdd.setOnClickListener(this);
    }
    private void initData() {
        mLvBlacklist.setAdapter(new BlacklistAdapter());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_add_blacklist:
                startActivity(new Intent(this, BlacklistEditActivity.class));
                break;
            default:
                break;
        }
    }

    private class BlacklistAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
