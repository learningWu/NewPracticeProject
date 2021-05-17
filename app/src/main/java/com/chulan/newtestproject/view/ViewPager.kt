package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs

/**
 * ViewPager
 * 手势处理，滑动控制，速度计算
 */
class ViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    /**
     * 抄的 sdk ViewPager 中的 MIN_FLING_VELOCITY
     */
    val MIN_FLING_VELOCITY = 400 * context.resources.displayMetrics.density

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    var downPointF = PointF()
    var downScroll = Point()
    val velocityTracker = VelocityTracker.obtain()
    val viewConfiguration = ViewConfiguration.get(context)
    var isScrolling = false
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var res = false
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downPointF.x = event.x
                downPointF.y = event.y
                downScroll.x = scrollX
                downScroll.y = scrollY
                isScrolling = false
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - downPointF.x
                if (!isScrolling) {
                    if (abs(dx) > viewConfiguration.scaledPagingTouchSlop) {
                        // 超过某个距离，接管控制，触发滑动
                        // 请求父布局不拦截
                        isScrolling = true
                        parent.requestDisallowInterceptTouchEvent(true)
                        res = true
                    }
                }

            }
        }
        return res
    }

    val overScroller = OverScroller(context)
    val maxVelocity = viewConfiguration.scaledMaximumFlingVelocity
    val minVelocity = MIN_FLING_VELOCITY
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 拦截事件后，进入具体处理
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downPointF.x = event.x
                downPointF.y = event.y
                downScroll.x = scrollX
                downScroll.y = scrollY
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = downScroll.x - (event.x - downPointF.x)
                if (dx in 0..width)
                    scrollTo(dx.toInt(), 0)
            }
            MotionEvent.ACTION_UP -> {
                // 判断是否需要翻页 1. 此时翻页过半 2. 手指滑动用力
                velocityTracker.computeCurrentVelocity(1000, maxVelocity.toFloat())
                val vX = velocityTracker.xVelocity
                var targetPage = 0
                if (abs(vX) < minVelocity) {
                    // 速度不够，由当前偏向哪一页决定
                    targetPage = if (scrollX > width / 2) 1 else 0
                } else {
                    // 速度方向 决定前一页还是后一页
                    targetPage = if (vX < 0) 1 else 0
                }
                // 1 往下一页滑，距离增加 width - scrollX
                // 0 往回滑
                val distance = if (targetPage == 1) width - scrollX else -scrollX
                // 启动滑动
                overScroller.startScroll(scrollX, 0, distance, 0)
                postInvalidateOnAnimation()
            }
        }
        return true
    }

    override fun onDetachedFromWindow() {
        velocityTracker.recycle()
        super.onDetachedFromWindow()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetL = 0
        children.forEach {
            it.layout(offsetL + l, t, offsetL + r, b)
            offsetL += measuredWidth
        }
    }

    /**
     * 1. draw -> 2.computerScroll -> 3.scrollTo,scrollBy -> 4.invalidate -> 回到 1
     */
    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }

}