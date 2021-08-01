package com.chulan.newtestproject.activity.launchermode

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chulan.newtestproject.R
import com.chulan.newtestproject.ext.startActivity

/**
 * Created by Admin on 2021/8/1
 */

class Activity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_next_demo)

        findViewById<View>(R.id.tvToNextActivity).setOnClickListener {
            startActivity<Activity2> {  }
        }
    }
}