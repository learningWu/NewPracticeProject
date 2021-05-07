package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.decodeSampledBitmapFromResource

/**
 * Created by wuzixuan on 2021/5/7
 */
class MultiPointerControlView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmap by lazy {
        decodeSampledBitmapFromResource(resources, R.mipmap.avatar, width, height)!!
    }
    private var paint: Paint = Paint()

    private var downPoint = PointF()

    private var offsetX = 0f
    private var offsetY = 0f
    private var initialX = 0f
    private var initialY = 0f

    private var activePointerId = -1
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("event",
                "actionMasked:" + event.actionMasked
                + "actionIndex:" + event.actionIndex
                + "pointerId:" + event.getPointerId(event.actionIndex)
                + "activePointerId:" + activePointerId
        )
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                activePointerId = event.getPointerId(event.actionIndex)
                initialX = offsetX
                initialY = offsetY
                downPoint.x = event.x
                downPoint.y = event.y
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                activePointerId = event.getPointerId(event.actionIndex)
                initialX = offsetX
                initialY = offsetY
                downPoint.x = event.getX(event.actionIndex)
                downPoint.y = event.getY(event.actionIndex)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                // 将活跃手指控制交给最后一个进来的手指
                val newActiveIndex = if (event.actionIndex == event.pointerCount - 1) event.pointerCount - 2 else event.pointerCount - 1
                activePointerId = event.getPointerId(newActiveIndex)

                // 相当于重新 “down下” 已经还在屏幕的手指
                initialX = offsetX
                initialY = offsetY
                downPoint.x = event.getX(newActiveIndex)
                downPoint.y  = event.getY(newActiveIndex)
            }
            MotionEvent.ACTION_MOVE -> {
                // move时 event.getActionIndex 是0。其实move不关心什么手指，只会得到 0
                // 使用活跃手指的移动控制
                val activeIndex = event.findPointerIndex(activePointerId)
                offsetX = initialX + event.getX(activeIndex) - downPoint.x
                offsetY = initialY + event.getY(activeIndex) - downPoint.y
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        // 绘制到View中间
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

}