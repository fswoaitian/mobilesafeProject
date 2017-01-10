package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.bean.BlacklistItem;
import com.itheima.mobilesafe.db.BlacklistDao;
import com.itheima.mobilesafe.util.ToastUtil;

import java.util.ArrayList;

public class BlacklistActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int REQUEST_CODE_BLACKLIST_ADD = 100;
    private static final int REQUEST_CODE_BLACKLIST_UPDATE = 101;
    private ListView mLvBlacklist;
    private ImageButton mIbAdd;
    private ArrayList<BlacklistItem> list = new ArrayList<>();
    private BlacklistDao blacklistDao;
    private ProgressBar mPbBlacklist;
    private BlacklistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        initView();
        initData();

    }



    private void initView() {
        mLvBlacklist = (ListView) findViewById(R.id.lv_blacklist);
        mIbAdd = (ImageButton) findViewById(R.id.ib_add_blacklist);
        mPbBlacklist = (ProgressBar) findViewById(R.id.pb_blacklist);
        mIbAdd.setOnClickListener(this);
        mLvBlacklist.setOnItemClickListener(this);
    }
    private void initData() {

        blacklistDao = new BlacklistDao(getApplicationContext());
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                ArrayList<BlacklistItem> items = blacklistDao.queryPart(0, 10);
                list.addAll(items);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPbBlacklist.setVisibility(View.GONE);
                        adapter = new BlacklistAdapter();
                        mLvBlacklist.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                    }
                });
            }
        }.start();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_add_blacklist:
                startActivityForResult(new Intent(this, BlacklistEditActivity.class),REQUEST_CODE_BLACKLIST_ADD);
                break;
            default:
                break;
        }
    }



    private class BlacklistAdapter extends BaseAdapter {
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
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView=View.inflate(getApplicationContext(), R.layout.item_blacklist, null);
                holder = new ViewHolder();
                holder.mNumber = (TextView) convertView.findViewById(R.id.tv_item_blacklist_number);
                holder.mType = (TextView) convertView.findViewById(R.id.tv_item_blacklist_type);
                holder.mDel = (ImageButton) convertView.findViewById(R.id.ib_blacklist_del);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            final BlacklistItem blacklistItem = list.get(position);
            holder.mNumber.setText(blacklistItem.mNumber);
            switch (blacklistItem.mType) {
                case BlacklistItem.TYPE_CALL:
                    holder.mType.setText("电话");
                    break;
                case BlacklistItem.TYPE_SMS:
                    holder.mType.setText("短信");
                    break;
                case BlacklistItem.TYPE_ALL:
                    holder.mType.setText("电话+短信");
                    break;
                default:
                    break;
            }
            holder.mDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    删除当前位置的数据从集合中删除，从数据库中删除
                    int delete = blacklistDao.delete(blacklistItem.mNumber);

                    if (delete>0) {
//                        从集合中删除
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }else{
                        ToastUtil.showToast(getApplicationContext(),"删除失败");
                    }

                }

            });
            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BLACKLIST_ADD) {
            if (resultCode == Activity.RESULT_OK) {
                String number = data.getStringExtra(BlacklistEditActivity.NUMBER);
                int type = data.getIntExtra(BlacklistEditActivity.TYPE,BlacklistItem.TYPE_ALL);
                BlacklistItem item = new BlacklistItem(number,type);
                list.add(item);
                adapter.notifyDataSetChanged();

            } else if (resultCode==Activity.RESULT_CANCELED) {
                ToastUtil.showToast(getApplicationContext(),"取消保存号码");
            }

        } else if (requestCode == REQUEST_CODE_BLACKLIST_UPDATE) {
            if (resultCode == Activity.RESULT_OK) {
                // 获取从 BlacklistEditActivity 返回的数据
                int type = data.getIntExtra(BlacklistEditActivity.TYPE,
                        BlacklistItem.TYPE_ALL);
                // 根据更新的的条目的位置, 修改对应的对象属性值
                int position = data.getIntExtra(BlacklistEditActivity.POSITION, -1);
                if (position != -1) {
                    list.get(position).mType = type;
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class ViewHolder {
        TextView mNumber;
        TextView mType;
        ImageButton mDel;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, BlacklistEditActivity.class);
        intent.setAction(BlacklistEditActivity.ACTION_UPDATE);
        BlacklistItem info = list.get(position);
        intent.putExtra(BlacklistEditActivity.NUMBER, info.mNumber);
        intent.putExtra(BlacklistEditActivity.TYPE, info.mType);
        intent.putExtra(BlacklistEditActivity.POSITION, position);
        startActivityForResult(intent,REQUEST_CODE_BLACKLIST_UPDATE);

    }
}
