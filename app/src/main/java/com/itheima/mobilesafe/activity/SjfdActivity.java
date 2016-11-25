package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;

public class SjfdActivity extends Activity implements View.OnClickListener {

    private TextView mTvNumber;
    private ImageView mIvLock;
    private TextView mTvReenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjfd);
//        初始化控件
        initView();
        initData();
    }



    private void initView() {
        mTvNumber = (TextView) findViewById(R.id.tv_sjfd_number);
        mIvLock = (ImageView) findViewById(R.id.iv_sjfd_lock);
        mTvReenter = (TextView) findViewById(R.id.tv_sjfd_reenter);
        mTvReenter.setOnClickListener(this);
    }
    private void initData() {
//    获取保存在手机中的安全号码
        String safeNumber = SharedPrefUtil.getString(getApplicationContext(), Constants.SAFE_NUMBER, null);
        if (!TextUtils.isEmpty(safeNumber)) {
            mTvNumber.setText(safeNumber);
        }
//        获取是否开启手机防盗
        boolean openSjfd = SharedPrefUtil.getBoolean(getApplicationContext(), Constants.OPEN_SJFD, false);
        if(openSjfd){
            mIvLock.setImageResource(R.mipmap.lock);
        }else{
            mIvLock.setImageResource(R.mipmap.unlock);
        }


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_sjfd_reenter:
                startActivity(new Intent(this,SjfdSetup1Activity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
