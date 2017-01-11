package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;
import com.itheima.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity implements View.OnClickListener {

    private SettingItemView mSivUpdate;
    private SettingItemView mSivBlacklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();

    }



    private void initView() {
        mSivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        mSivBlacklist = (SettingItemView) findViewById(R.id.siv_blacklist);
        mSivUpdate.setOnClickListener(this);
        mSivBlacklist.setOnClickListener(this);
    }
    private void initData() {
//        获取自动更新设置开关
        boolean mAutoUpdate = SharedPrefUtil.getBoolean(getApplicationContext(), Constants.SETTING_AUTO_UPDATE, false);
        if (mAutoUpdate) {
            mSivUpdate.setToggleOn(true);
        }else{
            mSivUpdate.setToggleOn(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.siv_update:
//                    判断是否开启自动更新设置
                if (mSivUpdate.isToggleOn()) {
                    SharedPrefUtil.putBoolean(getApplicationContext(), Constants.SETTING_AUTO_UPDATE,false);
                    mSivUpdate.setToggleOn(false);
                }else{
                    SharedPrefUtil.putBoolean(getApplicationContext(),Constants.SETTING_AUTO_UPDATE,true);
                    mSivUpdate.setToggleOn(true);
                }

                break;
            case R.id.siv_blacklist:

                break;
            default:
                break;
        }
    }
}
