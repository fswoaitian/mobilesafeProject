package com.itheima.mobilesafe.bean;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class BlacklistItem {
    public String number;
    public int type;

    public BlacklistItem(String number, int type) {
        this.number = number;
        this.type = type;
    }

    public BlacklistItem() {
        super();
    }
}
