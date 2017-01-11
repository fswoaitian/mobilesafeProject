package com.itheima.mobilesafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.bean.BlacklistItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class BlacklistDao {


    private final BlacklistOpenHelper openHelper;

    public BlacklistDao(Context context) {
        openHelper = new BlacklistOpenHelper(context);
    }

    public boolean insert(String number, int type) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BlacklistDbConstants.COLUM_NUMBER, number);
        values.put(BlacklistDbConstants.COLUM_TYPE, type);
        long insert = db.insert(BlacklistDbConstants.TABLE_NAME, null, values);
        db.close();
        if (insert != -1) {
            return true;
        } else {
            return false;
        }
    }

    public int delete(String number) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
//        sql语句
//        delete from table where number=?
        String whereClause = BlacklistDbConstants.COLUM_NUMBER + "=?";
        String[] whereArgs = new String[]{number};
        int delete = db.delete(BlacklistDbConstants.TABLE_NAME, whereClause, whereArgs);
        db.close();
        return delete;
    }

    public boolean update(String number, int type) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
//        update set type=? where number=?
        ContentValues values = new ContentValues();
        values.put(BlacklistDbConstants.COLUM_TYPE, type);
        String whereClause = BlacklistDbConstants.COLUM_NUMBER + "=?";
        String[] whereArgs = new String[]{number};
        int update = db.update(BlacklistDbConstants.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
        if (update > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<BlacklistItem> queryAll() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String[] columns = new String[]{BlacklistDbConstants.COLUM_NUMBER, BlacklistDbConstants.COLUM_TYPE};
        Cursor cursor = db.query(BlacklistDbConstants.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<BlacklistItem> list = new ArrayList<BlacklistItem>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(0);
                int type = cursor.getInt(1);
                BlacklistItem blacklistItem = new BlacklistItem(number, type);
                list.add(blacklistItem);
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * 分页查找
     * @param startNumber 开始行
     * @param lineNumber  显示的行数
     * @return  数据的集合
     */
    public ArrayList<BlacklistItem> queryPart(int startNumber,int lineNumber) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql="select number,type from "+BlacklistDbConstants.TABLE_NAME+" limit "+startNumber+" , "+lineNumber;
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<BlacklistItem> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String mNumber = cursor.getString(0);
                int mType = cursor.getInt(1);
                BlacklistItem blacklistItem = new BlacklistItem(mNumber, mType);
                list.add(blacklistItem);
            }
            cursor.close();
            db.close();
            return list;
        }

        return list;
    }

    public int queryType(String number) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
//        select type from table where number=?;
        String[] columns=new String[]{BlacklistDbConstants.COLUM_TYPE};
        String selection=BlacklistDbConstants.COLUM_NUMBER+"=?";
        String[] selectionArgs=new String[]{number};
        Cursor cursor = db.query(BlacklistDbConstants.TABLE_NAME, columns,selection,selectionArgs,null,null,null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int type = cursor.getInt(0);
                cursor.close();
                db.close();
                return type;

            }
        }
        return BlacklistItem.TYPE_NONE;
    }
}
