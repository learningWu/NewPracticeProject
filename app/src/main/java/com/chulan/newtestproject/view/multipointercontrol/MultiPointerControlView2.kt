package com.chulan.newtestproject.view.multipointercontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.decodeSampledBitmapFromResource

/**
 * Created by wuzixuan on 2021/5/10
 * 协作型多点触控
 */
class MultiPointerControlView2 @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmap by lazy {
        decodeSampledBitmapFromResource(resources, R.mipmap.avatar, width, height)!!
    }
    private var paint: Paint = Paint()

    private var offsetX = 0f
    private var offsetY = 0f

    private val pointDownF = PointF()
    private var initialOffsetX = 0f
    private var initialOffsetY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("event",
                "actionMasked:" + event.actionMasked
                        + "actionIndex:" + event.actionIndex
                        + "pointerId:" + event.getPointerId(event.actionIndex)
                        + "pointerCount:" + event.pointerCount
        )
        var focusX = 0f
        var focusY = 0f
        var sumX = 0f
        var sumY = 0f
        for (i in 0 until event.pointerCount) {
            if (!(event.actionMasked == MotionEvent.ACTION_POINTER_UP && i == event.actionIndex)) {
                // 抬起事件 仍会计算在 event.pointerCount 中，故移除此手指 x,y
                sumX += event.getX(i)
                sumY += event.getY(i)
            }
        }
        // 抬起事件 仍会计算在 event.pointerCount 中，故移除此手指 x,y
        val pointCount = if (event.actionMasked == MotionEvent.ACTION_POINTER_UP) event.pointerCount - 1 else event.pointerCount
        focusX = sumX / pointCount
        focusY = sumY / pointCount
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                pointDownF.x = focusX
                pointDownF.y = focusY
                initialOffsetX = offsetX
                initialOffsetY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = initialOffsetX + (focusX - pointDownF.x)
                offsetY = initialOffsetY + (focusY - pointDownF.y)
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