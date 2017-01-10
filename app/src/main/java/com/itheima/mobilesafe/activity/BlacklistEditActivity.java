package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.bean.BlacklistItem;
import com.itheima.mobilesafe.db.BlacklistDao;
import com.itheima.mobilesafe.util.ToastUtil;

public class BlacklistEditActivity extends Activity implements View.OnClickListener {

    public static final String ACTION_UPDATE = "update";
    public static final String POSITION = "position";
    private static final String ACTION_ADD = "add";
    private EditText mEtNumber;
    private RadioGroup mRgType;
    private RadioButton mRbCall;
    private RadioButton mRbSms;
    private RadioButton mRbAll;
    public static final String NUMBER = "number";
    public static final String TYPE = "type";
    private BlacklistDao mBlacklistDao;
    private TextView mTvTitle;
    private Button mBtnSave;
    private Button mBtnCancel;
    private boolean mIsUpdate;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist_edit);
        initView();
        initData();
    }

    private void initData() {
        mBlacklistDao = new BlacklistDao(getApplicationContext());
        Intent intent = getIntent();
        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_UPDATE)) {
            mIsUpdate = true;
//            更新
            int mPosition = intent.getIntExtra(POSITION, -1);
//            修改标题
            mTvTitle.setText("更新黑名单");
//            EditText不可用
            mEtNumber.setEnabled(false);
            String number = intent.getStringExtra(NUMBER);
            mEtNumber.setText(number);
//            设置RadioGroup中勾选的条目
            int type = intent.getIntExtra(TYPE, BlacklistItem.TYPE_NONE);
            switch (type) {
                case BlacklistItem.TYPE_CALL:
                    mRgType.check(R.id.rb_blacklist_type_call);
                    break;
                case BlacklistItem.TYPE_SMS:
                    mRgType.check(R.id.rb_blacklist_type_sms);
                    break;
                case BlacklistItem.TYPE_ALL:
                    mRgType.check(R.id.rb_blacklist_type_all);
                    break;
                default:
                    break;

            }
            mBtnSave.setText("更新");

        } else if (TextUtils.equals(action, ACTION_ADD)) {
            mIsUpdate = false;
        }
    }

    private void initView() {
        mEtNumber = (EditText) findViewById(R.id.et_blacklist_number);
        mRgType = (RadioGroup) findViewById(R.id.rg_blacklist_type);
        mRbCall = (RadioButton) findViewById(R.id.rb_blacklist_type_call);
        mRbSms = (RadioButton) findViewById(R.id.rb_blacklist_type_sms);
        mRbAll = (RadioButton) findViewById(R.id.rb_blacklist_type_all);
        mTvTitle = (TextView) findViewById(R.id.tv_blacklist_edit_title);
        mBtnSave = (Button) findViewById(R.id.btn_blacklist_save);
        mBtnCancel = (Button) findViewById(R.id.btn_blacklist_cancel);
        mBtnSave.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_blacklist_save:
                checkOk();

                break;
            case R.id.btn_blacklist_cancel:
                ToastUtil.showToast(getApplicationContext(), "没有输入黑名单信息");
                finish();
                break;
            default:
                break;
        }
    }

    private void checkOk() {

//                获取用户输入的黑名单号码
        String number = mEtNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            ToastUtil.showToast(getApplicationContext(), "号码不能为空");
            mEtNumber.isFocusable();
            return;
        }
//                获取用户选择的拦截类型
        int buttonId = mRgType.getCheckedRadioButtonId();
        int type = BlacklistItem.TYPE_NONE;
        switch (buttonId) {
            case R.id.rb_blacklist_type_call:
                type = BlacklistItem.TYPE_CALL;
                break;
            case R.id.rb_blacklist_type_sms:
                type = BlacklistItem.TYPE_SMS;
                break;
            case R.id.rb_blacklist_type_all:
                type = BlacklistItem.TYPE_ALL;
                break;
            default:
                ToastUtil.showToast(getApplicationContext(), "需要选择拦截类型");
                return;
        }
        if (mIsUpdate) {
            boolean update = mBlacklistDao.update(number, type);
            if (update) {
                Intent intent = new Intent();
                intent.putExtra(NUMBER, number);
                intent.putExtra(TYPE, type);
                intent.putExtra(POSITION, position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else {
            //                将输入输入的号码及拦截保存到黑名单数据库中
            boolean insert = mBlacklistDao.insert(number, type);
            if (insert) {
//                将输入带到黑名单管理界面
                Intent intent = new Intent();
                intent.putExtra(NUMBER, number);
                intent.putExtra(TYPE, type);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

}
