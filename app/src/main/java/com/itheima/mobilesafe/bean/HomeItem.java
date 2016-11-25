package com.itheima.mobilesafe.bean;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class HomeItem {
    public String mName;
    public String mDesc;
    public int mIconId;
    public HomeItem(String name, String desc, int iconId) {
        super();
        mName = name;
        mDesc = desc;
        mIconId = iconId;
    }
    public HomeItem() {
        super();
    }
}
