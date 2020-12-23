package com.example.mylibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Sencondctivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sencondctivity)

        findViewById<Button>(R.id.click).setOnClickListener {

        }
        initView();
    }

    private fun initView() {
    }
}