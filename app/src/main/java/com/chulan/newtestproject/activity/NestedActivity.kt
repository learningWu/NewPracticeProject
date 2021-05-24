package com.chulan.newtestproject.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chulan.newtestproject.R

/**
 * Created by Admin on 2021/5/23
 */
class NestedActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scalable_image_view_demo)
    }
}