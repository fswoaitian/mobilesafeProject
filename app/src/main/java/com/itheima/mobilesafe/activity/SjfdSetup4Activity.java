package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.receiver.SafeAdminReceiver;
import com.itheima.mobilesafe.util.ToastUtil;

public class SjfdSetup4Activity extends SjfdSetupBaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 100;
    private RelativeLayout mRlSetup4;
    private DevicePolicyManager mDpm;
    private ComponentName who;
    private ImageView mIvSetup4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjfd_setup4);
        initView();
        initData();
    }


    private void initView() {
//        获取激活设备管理员控件
        mRlSetup4 = (RelativeLayout) findViewById(R.id.rl_sjfd_setup4);
        mIvSetup4 = (ImageView) findViewById(R.id.iv_sjdf_setup4);
        mRlSetup4.setOnClickListener(this);

    }

    private void initData() {
        //获取系统管理员服务
        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        who = new ComponentName(getApplicationContext(),SafeAdminReceiver.class);
        if(mDpm.isAdminActive(who)){
            mIvSetup4.setImageResource(R.mipmap.admin_activated);
        }else{
            mIvSetup4.setImageResource(R.mipmap.admin_inactivated);
        }
    }


    @Override
    public boolean enterNextPage() {
//        判断是否激活设备管理员
        if(!mDpm.isAdminActive(who)){
            ToastUtil.showToast(getApplicationContext(),"需要激活设备管理员");
            return false;
        }
        startActivity(new Intent(this, SjfdSetup5Activity.class));
        return true;
    }

    @Override
    public boolean enterPrePage() {
        startActivity(new Intent(this, SjfdSetup3Activity.class));
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sjfd_setup4:
                if(mDpm.isAdminActive(who)){
//                    如果已经激活，则取消集合
                    mDpm.removeActiveAdmin(who);
//                    更改状态图片
                    mIvSetup4.setImageResource(R.mipmap.admin_inactivated);
                }else{
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,who);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "手机卫士时刻保护您的安全");
                    startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);

                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ENABLE_ADMIN){
            if(resultCode== Activity.RESULT_OK){
//                用户激活了设备管理员，修改状态图片
                mIvSetup4.setImageResource(R.mipmap.admin_activated);
            }

        }
    }
}
