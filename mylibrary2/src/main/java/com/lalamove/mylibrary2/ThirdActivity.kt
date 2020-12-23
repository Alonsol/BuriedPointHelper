package com.lalamove.mylibrary2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class ThirdActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        findViewById<Button>(R.id.btn).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Log.e("test","ThirdActivity onclick")
    }
}