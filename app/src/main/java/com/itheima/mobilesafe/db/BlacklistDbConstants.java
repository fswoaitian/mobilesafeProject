package com.itheima.mobilesafe.db;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public interface BlacklistDbConstants {
    /**
     * 数据库名称
     */
    String BASE_NAME = "blacklist.db";
    /**
     * 表名称
     */
    String TABLE_NAME = "blacklist";
    /**
     * id
     */
    String COLUM_ID = "_id";
    /**
     * 列：电话号码
     */
    String COLUM_NUMBER = "number";
    /**
     * 列：拦截类型
     */
    String COLUM_TYPE = "type";

    String CREATE_TABLE = "create table "+TABLE_NAME+" ( "+COLUM_ID+" integer primary key autoincrement,"+COLUM_NUMBER+" varchar(20) unique,"+COLUM_TYPE+" integer(10))";
}

