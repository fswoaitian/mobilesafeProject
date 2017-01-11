package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.itheima.mobilesafe.bean.BlacklistItem;
import com.itheima.mobilesafe.db.BlacklistDao;

public class BlacklistService extends Service {
    private PhoneStateListener listener=new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
//                获取保存在数据库中的黑名单电话号码

                int type = blacklistDao.queryType(incomingNumber);
                if (type == BlacklistItem.TYPE_ALL || type == BlacklistItem.TYPE_CALL) {
//                    拦截电话通过反射获取ServieManager
                    try {
                        Class<?> clazz = Class.forName("android.os.ServiceManager");
                        clazz.getDeclaredMethod("getServicer", String.class);
                        //TODO


                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    };
    private BroadcastReceiver mSmsReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objs) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
//                发送短信的号码
                String address = sms.getOriginatingAddress();
                int type = blacklistDao.queryType(address);
                if (type == BlacklistItem.TYPE_ALL || type == BlacklistItem.TYPE_SMS) {
                    abortBroadcast();
                }
            }

        }
    };
    private BlacklistDao blacklistDao;

    public BlacklistService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blacklistDao = new BlacklistDao(getApplicationContext());
//        注册短信接受者
        IntentFilter filter=new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSmsReceiver, filter);
        TelephonyManager mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
