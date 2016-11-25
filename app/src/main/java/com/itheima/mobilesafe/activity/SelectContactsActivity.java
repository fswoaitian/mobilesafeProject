package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.bean.PhoneItem;
import com.itheima.mobilesafe.util.ContactUtil;

import java.util.ArrayList;

public class SelectContactsActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String CONTACTS_NUMBER = "contacts_number";
    private ListView mLvSelect;
    private ArrayList<PhoneItem> list = new ArrayList<PhoneItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
//        获取ListView控件
        initView();
//        初始化数据
        initData();

    }

    private void initView() {
        mLvSelect = (ListView) findViewById(R.id.lv_select_contacts);
        mLvSelect.setOnItemClickListener(this);
    }

    private void initData() {
//        填充数据
        ArrayList<PhoneItem> mDatas = ContactUtil.getAllPhone(getApplicationContext());
        list.addAll(mDatas);
        mLvSelect.setAdapter(new ContactsAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        获取指定位置的内容，并把电话号码带到安全号码页面
        PhoneItem phoneItem = list.get(position);
        Intent intent = new Intent();
        intent.putExtra(CONTACTS_NUMBER, phoneItem.mPhone);
//        设置结果
        setResult(Activity.RESULT_OK, intent);
//        并关闭此页面
        finish();
    }

    private class ContactsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_phone, null);
                holder = new ViewHolder();
//                获取条目中的控件
                holder.mIcon = (ImageView) convertView.findViewById(R.id.iv_item_phone_icon);
                holder.mName = (TextView) convertView.findViewById(R.id.tv_item_phone_name);
                holder.mNumber = (TextView) convertView.findViewById(R.id.tv_item_phone_number);
//                装入口袋里
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            获取指定位置的条目
            PhoneItem phoneItem = list.get(position);
//            设置条目的显示内容
            holder.mName.setText(phoneItem.mName);
            holder.mNumber.setText(phoneItem.mPhone);
            Bitmap bitmap = ContactUtil.getContactPhoto(getApplicationContext(), phoneItem.mIconId);
            if (bitmap != null) {
                holder.mIcon.setImageBitmap(bitmap);
            } else {
                holder.mIcon.setImageResource(R.mipmap.ic_contact);
            }
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mName;
        TextView mNumber;

        public ViewHolder() {
        }
    }
}
