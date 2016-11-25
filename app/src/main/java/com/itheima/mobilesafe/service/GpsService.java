package com.itheima.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class GpsService extends Service {
    /**
     * gps经纬度转化接口KEY
     */
    private static final String APPKEY = "e06b8c6eea6a02068991ec2012182569";
    private LocationManager location;
    private LocationListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        获取系统定位服务
        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        long minTime = 500;
        float minDistance = 20;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
//                处理经纬度信息
                processGps(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);
    }

    private void processGps(final double latitude, final double longitude) {
        HttpUtils httpUtils = new HttpUtils();
        String uri = "http://apis.juhe.cn/geo/";
        RequestParams params = new RequestParams();
        params.addBodyParameter("lng", String.valueOf(longitude));
        params.addBodyParameter("lat", String.valueOf(latitude));
        params.addBodyParameter("key", APPKEY);
        params.addBodyParameter("type", "GPS");
        params.addBodyParameter("dtype", "json");
        httpUtils.send(HttpRequest.HttpMethod.GET, uri, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                访问成功获取JSON并解析
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONObject jsonResult = jsonObject.getJSONObject("result");
                    String address = jsonResult.getString("address");
                    if (!TextUtils.isEmpty(address)) {
//                        如果不为空，直接发送转换后的地址
                        sendSms(address);
                    } else {
                        sendSms("经度：" + longitude + "纬度：" + latitude);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                sendSms("经度：" + longitude + "纬度：" + latitude);
            }
        });

    }

    private void sendSms(String address) {
        SmsManager smsManager = SmsManager.getDefault();
//        获取安全号码
        String safeNumber = SharedPrefUtil.getString(getApplicationContext(), Constants.SAFE_NUMBER, null);
        if (!TextUtils.isEmpty(safeNumber)) {
            smsManager.sendTextMessage(safeNumber, null, address, null, null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭定位服务
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location.removeUpdates(listener);
    }
}
