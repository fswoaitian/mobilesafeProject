package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;
import com.itheima.mobilesafe.util.ToastUtil;

public class SjfdSetup3Activity extends SjfdSetupBaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_NUMBER = 100;
    private EditText mEtSetup3;
    private Button mBtnSetup3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjfd_setup3);
//        初始化控件
        initView();
        initData();
    }

    private void initView() {
        mEtSetup3 = (EditText) findViewById(R.id.et_sjdf_setup3);
        mBtnSetup3 = (Button) findViewById(R.id.btn_sjfd_setup3_select);
        mBtnSetup3.setOnClickListener(this);
    }
    private void initData() {
//获取保存再系统中的安全号码
        String mSafeNumber = SharedPrefUtil.getString(getApplicationContext(), Constants.SAFE_NUMBER, null);
        if (!TextUtils.isEmpty(mSafeNumber)) {
            mEtSetup3.setText(mSafeNumber);
        }
    }


    @Override
    public boolean enterNextPage() {
//        获取用户输入的安全号码
        String safeNumber = mEtSetup3.getText().toString().trim();
        if (TextUtils.isEmpty(safeNumber)) {
            ToastUtil.showToast(getApplicationContext(), "需要输入或选择安全号码");
            return false;
        } else {
            SharedPrefUtil.putString(getApplicationContext(), Constants.SAFE_NUMBER, safeNumber);
            startActivity(new Intent(this, SjfdSetup4Activity.class));
            return true;
        }

    }

    @Override
    public boolean enterPrePage() {
        startActivity(new Intent(this, SjfdSetup2Activity.class));
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sjfd_setup3_select:
                startActivityForResult(new Intent(this,SelectContactsActivity.class),REQUEST_CODE_NUMBER);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_NUMBER){
            if(resultCode== Activity.RESULT_OK){
                String number = data.getStringExtra(SelectContactsActivity.CONTACTS_NUMBER);
                mEtSetup3.setText(number);
            }
        }
    }
}
