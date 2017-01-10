package com.itheima.mobilesafe.bean;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class BlacklistItem {
    public static final int TYPE_NONE =-1 ;
    public static final int TYPE_CALL = 0;
    public static final int TYPE_SMS = 1;
    public static final int TYPE_ALL = 2;

    public String mNumber;
    public int mType;

    public BlacklistItem(String number, int type) {
        mNumber= number;
        mType = type;
    }

    public BlacklistItem() {
        super();
    }
}
