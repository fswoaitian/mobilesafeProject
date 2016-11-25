package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        判断手机是否开启防盗保护
        boolean openSjfd = SharedPrefUtil.getBoolean(context, Constants.OPEN_SJFD, false);
        if(openSjfd){
//            获取系统服务
            TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = mTm.getSimSerialNumber();
//            获取保存到手机中的SIM卡信息
            String simNumber = SharedPrefUtil.getString(context, Constants.SIM_NUMBER, null);
            if (!TextUtils.isEmpty(simNumber)) {
                if (!TextUtils.equals(simNumber, simSerialNumber)) {
//                    手机卡发生变化
//                   获取安全号码 发送短信到安全号码
                    String safeNumber = SharedPrefUtil.getString(context, Constants.SAFE_NUMBER, null);
                    if (!TextUtils.isEmpty(safeNumber)) {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(safeNumber,null,"nin de shou ji bei tou le",null,null);
                    }else{
                        System.out.print("安全号码不存在");
                    }
                }else{
                    System.out.println("手机卡没有变化");
                }
            }
        }


    }
}
