package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.util.Constants;
import com.itheima.mobilesafe.util.SharedPrefUtil;
import com.itheima.mobilesafe.util.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
    protected static final int SHOW_UPDATE_DIALOG = 0;
    protected static final int SERVER_ERROR = 1;
    protected static final int URL_ERROR = 2;
    protected static final int NET_ERROR = 3;
    protected static final int JSON_ERROR = 4;
    protected static final int NO_UPDATE = 5;
    private static final int REQUEST_CODE_INSTALL = 100;

    private int mVersionCode;
    private int mServerVersionCode; // member
    private String mServerVersionName;
    private String mDesc;
    private String mDownloadUrl;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(getApplicationContext(), "网址错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case NET_ERROR:
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(getApplicationContext(), "数据异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case NO_UPDATE:
                    enterHome();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 在代码中, 动态获取版本号和版本信息
        TextView tvVersion = (TextView) findViewById(R.id.tv_splash_version);
        // 获得包管理器
        PackageManager packageManager = getPackageManager();
        try {
            // 获取当前包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            mVersionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            tvVersion.setText("版本: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // 不会发生
            e.printStackTrace();
        }
        boolean isAutoUpdate = SharedPrefUtil.getBoolean(getApplicationContext(),
               Constants.SETTING_AUTO_UPDATE, true);
        if (isAutoUpdate) {
            checkVersion();
        } else {
            // 不能再主界面睡眠
            // SystemClock.sleep(2000);
            // enterHome();
            mHandler.sendEmptyMessageDelayed(NO_UPDATE, 2000);
        }
    }

    /**
     * 联网获取更新信息
     */
    private void checkVersion() {
        new Thread() {
            public void run() {
                long startTime = System.currentTimeMillis();
                HttpURLConnection connection = null;
                Message msg = Message.obtain();
                try {
                    // 10.0.2.2, 系统预留ip, 专门用于模拟器访问电脑
                    URL url = new URL("http://10.0.2.2:8080/update78.json");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(2000); // 连接超时
                    connection.setReadTimeout(2000); // 读取超时
                    connection.connect();
                    int responseCode = connection.getResponseCode(); // 200,
                    // 3xx,
                    // 4xx, 5xx
                    if (200 == responseCode) {
                        // 访问成功, 解析数据
                        InputStream inputStream = connection.getInputStream();
                        String jsonStr = StreamUtil.streamToString(inputStream);
                        System.out.println(jsonStr);
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        mServerVersionCode = jsonObject.getInt("versionCode");
                        mServerVersionName = jsonObject.getString("versionName");
                        mDesc = jsonObject.getString("desc");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        System.out.println(mServerVersionCode + " " + mServerVersionName + " "
                                + mDesc + " " + mDownloadUrl);
                        if (mServerVersionCode > mVersionCode) {
                            // 需要更新, 弹出对话框, 提示用户
                            // 不能再子线程修改主界面UI
                            // showUpdateDialog();
                            // runOnUiThread(new Runnable() {
                            // @Override
                            // public void run() {
                            // }
                            // });
                            msg.what = SHOW_UPDATE_DIALOG;
                        } else {
                            // 不需要更新, 进主页面
                            msg.what = NO_UPDATE;
                            // enterHome();
                        }
                    } else {
                        // 访问失败
                        msg.what = SERVER_ERROR;
                    }
                } catch (MalformedURLException e) {
                    // URL异常
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    // 网络异常
                    msg.what = NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    // JSON异常
                    e.printStackTrace();
                } finally {
                    // 断开连接
                    if (connection != null) {
                        connection.disconnect();
                    }
                    // 睡够两秒再发消息
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;
                    if (timeUsed < 2000) {
                        SystemClock.sleep(2000 - timeUsed);
                    }
                    mHandler.sendMessage(msg);
                }
            };
        }.start();
    }

    protected void enterHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    protected void showUpdateDialog() {
        // 对于对话框, 一定要使用Activity的this, 对话框要依附于Activity
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否更新");
        builder.setMessage(mDesc);
        // builder.setCancelable(false); // 设置不能取消, 不要用这样的方法
        // 确定按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // System.out.println("立即更新");
                downloadApk();
            }
        });
        // 取消按钮
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("下次再说");
                enterHome();
            }
        });
        // 取消时的回调
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        // builder.show();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 下载新版本apk
     */
    protected void downloadApk() {
        // SD卡可用时下载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("正在下载");
            progressDialog.setCancelable(false); // 不能取消
            progressDialog.setCanceledOnTouchOutside(false); // 点击对话框外部区域, 不取消
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // 使用xUtils里的HttpUtils下载文件
            HttpUtils http = new HttpUtils();
            String target = Environment.getExternalStorageDirectory() + "/MobileSafe2.apk";
            // RequestCallBack, 在下载开始/下载中/成功/失败调相应的回调方法
            http.download(mDownloadUrl, target, new RequestCallBack<File>() {
                @Override
                public void onStart() {
                    super.onStart();
                    progressDialog.show();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    progressDialog.setMax((int) total);
                    progressDialog.setProgress((int) current);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    progressDialog.dismiss();
                    System.out.println("contentLength: " + responseInfo.contentLength);
                    installApk(responseInfo.result);
                }

                @Override
                public void onFailure(HttpException e, String error) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    System.out.println(error);
                    enterHome();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "SD卡不可用", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 安装apk
     */
    protected void installApk(File file) {
        // 调用 系统的 PackageInstaller, 让它帮我们安装apk
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        // intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        // 防止用于在安装界面取消
        startActivityForResult(intent, REQUEST_CODE_INSTALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSTALL) {
            enterHome();
        }
    }
}
