package com.chulan.newtestproject

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import com.chulan.newtestproject.activity.*
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
        addItem(getString(R.string.clock_text)) {
            startActivity<ClockActivity>()
        }
        addItem(getString(R.string.tag_layout_text)) {
            startActivity<TagLayoutActivity>()
        }
        addItem(getString(R.string.flip_book)) {
            startActivity<FlipActivity>()
        }
        addItem(getString(R.string.scalable_image)) {
            startActivity<ScalableImageViewActivity>()
        }
        addItem(getString(R.string.multi_pointer_control_image)) {
            startActivity<MultiPointerControlActivity>()
        }
        addItem(getString(R.string.view_pager)) {
            startActivity<ViewPagerActivity>()
        }
    }

    private fun addItem(title: String, block: () -> Unit) {
        llContainer.addView(TextView(this).apply {
            text = title
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(10f.dp2pxInt())
            setOnClickListener {
                block()
            }
        })
    }
}