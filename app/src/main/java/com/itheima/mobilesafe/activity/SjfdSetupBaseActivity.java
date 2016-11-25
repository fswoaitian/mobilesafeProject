package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.itheima.mobilesafe.R;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public abstract class SjfdSetupBaseActivity extends Activity {
    private GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       detector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getRawX() - e1.getRawX() > 100) {
//                    向右滑动，上一步
                    showPrePage();
                    return true;
                } else if(e1.getRawX() - e2.getRawX() > 100) {
//                    向左滑动,下一步
                    showNextPage();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    private void showPrePage() {
        if (!enterPrePage()) {

            return;
        }
        finish();
        overridePendingTransition(R.anim.pre_enter_anim, R.anim.pre_exit_anim);

    }

    private void showNextPage() {
        if (!enterNextPage()) {
            return;
        }
            finish();
            overridePendingTransition(R.anim.next_enter_anim, R.anim.next_exit_anim);


    }

    public abstract boolean enterNextPage();

    public abstract boolean enterPrePage();

    /**
     * 点击上一步按钮
     *
     * @param v
     */
    public void prePage(View v) {
        showPrePage();

    }

    /**
     * 点击下一步按钮
     *
     * @param v
     */
    public void nextPage(View v) {
        showNextPage();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

