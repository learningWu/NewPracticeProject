package com.chulan.newtestproject

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import com.chulan.newtestproject.activity.AvatarActivity
import com.chulan.newtestproject.ext.startActivity
import com.chulan.newtestproject.util.dp2px
import com.chulan.newtestproject.util.dp2pxInt

class MainActivity : AppCompatActivity() {
    lateinit var llContainer: ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        llContainer = findViewById(R.id.llContainer)

        addItem(getString(R.string.avatar_text)) {
            startActivity<AvatarActivity>()
        }
    }

    private fun addItem(title: String, block: () -> Unit) {
        llContainer.addView(TextView(this).apply {
            text = title
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setPadding(5f.dp2pxInt())
            setOnClickListener {
                block()
            }
        })
    }
}