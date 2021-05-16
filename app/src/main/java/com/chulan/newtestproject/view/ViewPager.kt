package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.abs

class ViewPager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    var downPointF = PointF()
    var downScroll = Point()
    val viewConfiguration = ViewConfiguration.get(context)
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var res = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downPointF.x = event.x
                downPointF.y = event.y
                downScroll.x = scrollX
                downScroll.y = scrollY
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - downPointF.x
                if (abs(dx) > viewConfiguration.scaledPagingTouchSlop) {
                    // 超过某个距离，接管控制，触发滑动
                    // 请求父布局不拦截
                    parent.requestDisallowInterceptTouchEvent(true)
                    res = true
                }
            }
        }
        return res
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 拦截事件后，进入具体处理
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downPointF.x = event.x
                downPointF.y = event.y
                downScroll.x = scrollX
                downScroll.y = scrollY
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = downScroll.x - (event.x - downPointF.x)
                scrollTo(dx.toInt(), 0)
            }
        }
        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetL = 0
        children.forEach {
            it.layout(offsetL + l, t, offsetL + r, b)
            offsetL += measuredWidth
        }
    }


}