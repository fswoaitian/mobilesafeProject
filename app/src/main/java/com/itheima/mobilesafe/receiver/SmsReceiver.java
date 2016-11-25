package com.itheima.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.GpsService;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class SmsReceiver extends BroadcastReceiver {

    private DevicePolicyManager mDpm;

    @Override
    public void onReceive(Context context, Intent intent) {

//获取是否开机手机防盗
        boolean openSjfd = SharedPrefUtil.getBoolean(context, Constants.OPEN_SJFD, false);
        if (openSjfd) {
//            获取短信
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objs) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String messageBody = message.getMessageBody();
                String address = message.getOriginatingAddress();
//                获取安全号码，判断是否是安全号码发送过来的短信
                String safeNumber = SharedPrefUtil.getString(context, Constants.SAFE_NUMBER, null);

//                短信处理
                if (TextUtils.equals(safeNumber, address)) {
                    process(context, messageBody);
                }
            }
        }
    }

    private void process(Context context, String messageBody) {
        if (TextUtils.equals(messageBody, "#*loaction*#")) {
            Intent intent = new Intent(context, GpsService.class);
            context.startService(intent);
            //            拦截短信广播
            abortBroadcast();
        } else if (TextUtils.equals(messageBody, "#*alarm*#")) {
//          开启报警声音
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.start();
            //            拦截短信广播
            abortBroadcast();
        } else if (TextUtils.equals(messageBody, "#*wipedata*#")) {
            mDpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDpm.wipeData(0);
//            拦截短信广播
            abortBroadcast();
        } else if (TextUtils.equals(messageBody, "#*lockscreen*#")) {
            mDpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//            重新设置密码
            mDpm.resetPassword("123", 0);
//            并锁屏
            mDpm.lockNow();
            //            拦截短信广播
            abortBroadcast();
        }
    }
}
