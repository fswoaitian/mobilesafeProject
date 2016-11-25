package com.itheima.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;
import com.itheima.mobilesafe.util.ToastUtil;

public class SjfdSetup2Activity extends SjfdSetupBaseActivity implements View.OnClickListener {

    private RelativeLayout mRlSetup2;
    private ImageView mIvLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjfd_setup2);
//        初始化控件
        initView();
//        初始化显示内容
        initData();
    }



    private void initView() {
//        获取绑定SIM卡Button控件
        mRlSetup2 = (RelativeLayout) findViewById(R.id.rl_sjfd_setup2);
        mIvLock = (ImageView) findViewById(R.id.iv_sjdf_setup2_lock);
        mRlSetup2.setOnClickListener(this);
    }
    private void initData() {
        String simNum = SharedPrefUtil.getString(getApplicationContext(), Constants.SIM_NUMBER, null);
        if(TextUtils.isEmpty(simNum)){
            mIvLock.setImageResource(R.mipmap.unlock);
        }else{
            mIvLock.setImageResource(R.mipmap.lock);
        }
    }

    @Override
    public boolean enterNextPage() {
//        判断是否绑定SIM卡信息
        String simNumber = SharedPrefUtil.getString(getApplicationContext(), Constants.SIM_NUMBER, null);
        if (TextUtils.isEmpty(simNumber)) {
            ToastUtil.showToast(getApplicationContext(), "需要绑定SIM卡信息");
            return false;
        }
        startActivity(new Intent(this, SjfdSetup3Activity.class));
        return true;
    }

    @Override
    public boolean enterPrePage() {
        startActivity(new Intent(this, SjfdSetup1Activity.class));
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sjfd_setup2:
                String mSimNumber = SharedPrefUtil.getString(getApplicationContext(), Constants.SIM_NUMBER, null);
                if (TextUtils.isEmpty(mSimNumber)) {
//                    绑定SIM卡号
//                    获取手机的SIM卡信息
                    TelephonyManager mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = mTm.getSimSerialNumber();
                    SharedPrefUtil.putString(getApplicationContext(), Constants.SIM_NUMBER, simSerialNumber);
                    mIvLock.setImageResource(R.mipmap.lock);
                } else {
//                    解除绑定的SIM卡信息
                    SharedPrefUtil.removeString(getApplicationContext(), Constants.SIM_NUMBER);
                    mIvLock.setImageResource(R.mipmap.unlock);
                }
                break;
            default:
                break;
        }
    }
}
