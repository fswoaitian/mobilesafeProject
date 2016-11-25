package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.itheima.mobilesafe.R;

public class SjfdSetup1Activity extends SjfdSetupBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjfd_setup1);

    }

    @Override
    public boolean enterNextPage() {
        startActivity(new Intent(this,SjfdSetup2Activity.class));
        return true;
    }

    @Override
    public boolean enterPrePage() {
        return false;
    }

}
