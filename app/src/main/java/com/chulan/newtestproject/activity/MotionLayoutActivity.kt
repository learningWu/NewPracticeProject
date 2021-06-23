package com.chulan.newtestproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.Constraints
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.dp2px
import com.chulan.newtestproject.util.dp2pxInt

class MotionLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout_demo)

        initView()
    }

    private var isStart = true
    private var progress = 0f
    private fun initView() {
        // 0️⃣ 通过id获取ConstraintSet，id 是xml中对应id
        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)
        val constraintSet =
            motionLayout.getConstraintSet(R.id.start)
        //1️⃣ 通过id获取Constraint
        constraintSet.getConstraint(R.id.red_star).apply {
            // 2️⃣
            //设置layout 的一些属性
            //↓↓这行代码↓↓ =>layout_constraintTop_toTopOf="parent"
//            layout.topMargin = 200f.dp2pxInt()
        }
        // 设置 ignore 没用，必须设置这个
        motionLayout.getConstraintSet(R.id.start).getConstraint(R.id.xxx).propertySet.mVisibilityMode = 1
        findViewById<View>(R.id.tv).setOnClickListener {
            findViewById<View>(R.id.xxx).visibility = View.VISIBLE
            if (isStart){
                motionLayout.transitionToEnd()
                isStart = false
            }else{
                motionLayout.transitionToStart()
                isStart = true
            }
        }
    }
}