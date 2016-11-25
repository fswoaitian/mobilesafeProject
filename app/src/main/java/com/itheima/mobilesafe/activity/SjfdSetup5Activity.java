package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;
import com.itheima.mobilesafe.util.ToastUtil;

public class SjfdSetup5Activity extends SjfdSetupBaseActivity {

    private CheckBox mCbSetup5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjfd_setup5);
        initView();
        initData();
    }

    private void initView() {
        mCbSetup5 = (CheckBox) findViewById(R.id.cb_sjfd_setup5);
        mCbSetup5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
//                    保存设置
                    SharedPrefUtil.putBoolean(getApplicationContext(), Constants.OPEN_SJFD, b);
                } else {
//                    取消保存
                    SharedPrefUtil.removeBoolean(getApplicationContext(), Constants.OPEN_SJFD);
                }
            }
        });
    }

    private void initData() {
        boolean opensjfd = SharedPrefUtil.getBoolean(getApplicationContext(), Constants.OPEN_SJFD, false);
        mCbSetup5.setChecked(opensjfd);

    }

    @Override
    public boolean enterNextPage() {
        boolean checked = mCbSetup5.isChecked();
        if (!checked) {
//            没有选择开启防盗保护，提示用户需要开启防盗保护
            ToastUtil.showToast(getApplicationContext(), "需要开启防盗保护");
            return false;
        }
        SharedPrefUtil.putBoolean(getApplicationContext(), Constants.HAS_SET_SJFD, true);
        startActivity(new Intent(this, SjfdActivity.class));
        return true;
    }

    @Override
    public boolean enterPrePage() {
        startActivity(new Intent(this, SjfdSetup4Activity.class));
        return true;
    }
}
