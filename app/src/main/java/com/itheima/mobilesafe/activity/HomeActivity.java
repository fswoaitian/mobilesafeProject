package com.itheima.mobilesafe.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.bean.HomeItem;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;
import com.itheima.mobilesafe.util.ToastUtil;

import java.util.ArrayList;

public class HomeActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView mIvLogo;
    private GridView mGvHome;
    private ArrayList<HomeItem> mDatas = new ArrayList<HomeItem>();
    private static final String[] NAME = new String[]{"手机防盗", "骚扰拦截", "软件管家", "进程管理", "流量统计",
            "手机杀毒", "缓存清理", "常用工具"};
    private static final String[] DESC = new String[]{"远程定位手机", "全面拦截骚扰", "管理您的软件", "管理运行进程",
            "流量一目了然", "病毒无处藏身", "系统快如火箭", "工具大全"};
    private static final int[] ICON_ID = new int[]{R.mipmap.sjfd, R.mipmap.srlj,
            R.mipmap.rjgj, R.mipmap.jcgl, R.mipmap.lltj, R.mipmap.sjsd, R.mipmap.hcql,
            R.mipmap.cygj};
    private ImageButton mIbSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        startLogoAnimation();
        initData();
    }

    private void initView() {
        mIvLogo = (ImageView) findViewById(R.id.iv_home_logo);
        mGvHome = (GridView) findViewById(R.id.gv_home);
        mIbSetting = (ImageButton) findViewById(R.id.ib_home_setting);
        mIbSetting.setOnClickListener(this);
        mGvHome.setOnItemClickListener(this);
    }

    private void initData() {
        // 添加数据
        for (int i = 0; i < NAME.length; i++) {
            HomeItem item = new HomeItem(NAME[i], DESC[i], ICON_ID[i]);
            mDatas.add(item);
        }
        // GridView用法和ListView很类似, 都要设置Adapter
        mGvHome.setAdapter(new HomeAdapter());
    }

    private void startLogoAnimation() {
        // 属性动画, 绕着Y轴旋转
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvLogo, "rotationY", 0, 180, 360);
        // 设置重复次数
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        // 设置重复模式, ObjectAnimator.RESTART, ObjectAnimator.REVERSE
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setDuration(2000);
        animator.start();
    }

    private class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // View view;
            // if(convertView != null) {
            // view = convertView;
            // }else {
            // view = View.inflate(getApplicationContext(), R.layout.item_home,
            // null);
            // }
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_home, null);
            }
            // 找到需要设置内容的控件
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_item_home_name);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.tv_item_home_desc);
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_home_icon);
            // 给控件设置内容
            HomeItem homeItem = mDatas.get(position);
            tvName.setText(homeItem.mName);
            tvDesc.setText(homeItem.mDesc);
            ivIcon.setImageResource(homeItem.mIconId);
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
//                进入手机防盗
                showSjfdDialog();
                break;
            case 1:
//                进入黑名单管理
                startActivity(new Intent(this, BlacklistActivity.class));
                break;
            default:
                break;
        }
    }

    private void showSjfdDialog() {
        String sjfdPwd = SharedPrefUtil
                .getString(getApplicationContext(), Constants.SJFD_PWD, null);
        if (TextUtils.isEmpty(sjfdPwd)) {
            // 弹出设置密码对话框
            showSetPwdDialog();
        } else {
            // 弹出输入密码对话框
            showInputPwdDialog();
        }
    }

    private void showSetPwdDialog() {
        // 自定义对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        // 找到布局中的按钮, 设置点击事件
        Button btnOk = (Button) view.findViewById(R.id.btn_set_pwd_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_set_pwd_cancel);
        final EditText etSetPwd = (EditText) view.findViewById(R.id.et_set_pwd);
        final EditText etConfirmPwd = (EditText) view.findViewById(R.id.et_set_pwd_confirm);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setPwd = etSetPwd.getText().toString().trim();
                String confirmPwd = etConfirmPwd.getText().toString().trim();
                if (TextUtils.isEmpty(setPwd)) {
                    ToastUtil.showToast(getApplicationContext(), "输入密码不能为空");
                    etSetPwd.requestFocus();
                    return;
                }
                if (!TextUtils.equals(setPwd, confirmPwd)) {
                    ToastUtil.showToast(getApplicationContext(), "两次输入不一致");
                    etConfirmPwd.requestFocus();
                    return;
                }
                // 保存密码
                SharedPrefUtil.putString(getApplicationContext(), Constants.SJFD_PWD, setPwd);
                // 进入第一个手机防盗设置向导页面
                enterSjfd();
                // startActivity(new Intent(HomeActivity.this,
                // SjfdSetup1Activity.class));
                // 取消对话框
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void showInputPwdDialog() {
        // 自定义对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_input_pwd, null);
        // 找到布局中的按钮, 设置点击事件
        Button btnOk = (Button) view.findViewById(R.id.btn_input_pwd_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_input_pwd_cancel);
        final EditText etInputPwd = (EditText) view.findViewById(R.id.et_input_pwd);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPwd = etInputPwd.getText().toString().trim();
                if (TextUtils.isEmpty(inputPwd)) {
                    ToastUtil.showToast(getApplicationContext(), "输入不能为空");
                    etInputPwd.requestFocus();
                    return;
                }
                String savedPwd = SharedPrefUtil.getString(getApplicationContext(),
                        Constants.SJFD_PWD, null);
                if (!TextUtils.equals(inputPwd, savedPwd)) {
                    ToastUtil.showToast(getApplicationContext(), "密码输入错误");
                    etInputPwd.requestFocus();
                    return;
                }
                // 根据情况, 跳转页面
                enterSjfd();
                // startActivity(new Intent(HomeActivity.this,
                // SjfdActivity.class));
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    // 如果设置过手机防盗, 进入手机防盗页面, 如果没有设置过, 进入到第一个设置向导页面.
    protected void enterSjfd() {
        boolean hasSet = SharedPrefUtil.getBoolean(getApplicationContext(), Constants.HAS_SET_SJFD,
                false);
        if (hasSet) {
            startActivity(new Intent(this, SjfdActivity.class));
        } else {
            startActivity(new Intent(this, SjfdSetup1Activity.class));
        }
    }

}
