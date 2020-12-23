package com.example.performancehelper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mylibrary.SecondActivity;
import com.lalamove.lib.MyClass;
import com.lalamove.mylibrary2.ThirdActivity;

public class MainActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData("hahah");
        initView();
        initLisenter();
        getImei();


        new MyClass().test();
    }

    private void initEvent() {
        boolean b = false;
        if (b) {
            Toast.makeText(this, "我被修复了！！！", Toast.LENGTH_LONG);
        }
    }

    private void initLisenter() {

    }

    private void initData(String content) {
        Log.e("test", "initdata ---");
    }

    private void initView() {
        Button button = findViewById(R.id.btn_click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EmptyActivity.class));
                Log.i(TAG, "initView: " + v.getId());
                insertClickPoint();
            }
        });
        findViewById(R.id.btn_long_click).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initEvent();
                Log.i(TAG, "onLongClick: " + v.getId());
                return true;
            }
        });
    }

    private void insertClickPoint() {
        Log.e("test", "insertClickPoint ---");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public String getImei() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED)) {
            return "";
        }

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            return " ";
        }
        return imei;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                new MyClass().test();
                break;
            case R.id.menu_del:
                Intent intent1 = new Intent(this, ThirdActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
        return true;
    }
}
