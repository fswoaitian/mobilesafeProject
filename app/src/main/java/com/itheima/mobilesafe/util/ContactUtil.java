package com.itheima.mobilesafe.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.itheima.mobilesafe.bean.PhoneItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class ContactUtil {
    /**
     * 获取手机中联系人的信息
     * @param context
     * @return 联系人信息的集合
     */
    public static ArrayList<PhoneItem> getAllPhone(Context context){
//        获取内容决策者
        ContentResolver resolver = context.getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection=new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
//       select * from ;
        Cursor cursor = resolver.query(uri, projection, null, null, null);
//        创建集合
        ArrayList<PhoneItem> list = new ArrayList<PhoneItem>();
        if(cursor!=null){
            while(cursor.moveToNext()){
                int mId = cursor.getInt(0);
                String mNumber = cursor.getString(1);
                String mName = cursor.getString(2);
//                创建对象，并将内容填充进去
                PhoneItem phoneItem = new PhoneItem(mName,mNumber,mId);
//                将数据填充到集合中
                list.add(phoneItem);
            }
            cursor.close();

        }
        return list;
    }

    /**
     * 通过联通的ID获取联系人的照片
     * @param context
     * @param id 联系人id
     * @return
     */
    public  static Bitmap getContactPhoto(Context context, int id){
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
