package com.itheima.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class SettingItemView extends RelativeLayout {
    private boolean mIsToggleOn;
    private ImageView mToggle;

    public SettingItemView(Context context) {
        super(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(view);
//        获取控件
        TextView mName = (TextView) view.findViewById(R.id.tv_setting_name);
        mToggle = (ImageView) view.findViewById(R.id.iv_settting_toggle);
        String name = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "sivName");
        mName.setText(name);
    }

    public boolean isToggleOn() {
        return mIsToggleOn;
    }

    public void setToggleOn(boolean b) {
        mIsToggleOn=b;
        if (mIsToggleOn) {
            mToggle.setImageResource(R.mipmap.on);
        }else{
            mToggle.setImageResource(R.mipmap.off);
        }
    }
}
