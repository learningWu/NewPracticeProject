package com.chulan.newtestproject.view.multipointercontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.core.util.forEach
import androidx.core.util.set
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.decodeSampledBitmapFromResource
import com.chulan.newtestproject.util.dp2px

/**
 * Created by wuzixuan on 2021/5/10
 * 各自为战型多点触控
 */
class MultiPointerControlView3 @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var paint: Paint = Paint()

    private val sparseArray = SparseArray<Path>()

    init {
        paint.apply {
            strokeWidth = 2f.dp2px()
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("event",
                "actionMasked:" + event.actionMasked
                        + "actionIndex:" + event.actionIndex
                        + "pointerId:" + event.getPointerId(event.actionIndex)
                        + "pointerCount:" + event.pointerCount
        )
        var pointerId = 0
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                pointerId = event.getPointerId(event.actionIndex)
                sparseArray[pointerId] = Path()
                sparseArray[pointerId].moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))
            }
            MotionEvent.ACTION_MOVE -> {
                // 由于 ACTION_MOVE 时获得的 event.actionIndex 一直是0，所以遍历所有手指
                for (i in 0 until event.pointerCount) {
                    sparseArray[event.getPointerId(i)].lineTo(event.getX(i), event.getY(i))
                }
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                pointerId = event.getPointerId(event.actionIndex)
                sparseArray[pointerId].reset()
                sparseArray.remove(pointerId)
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("event", "" + sparseArray.size())
        sparseArray.forEach { key, value ->
            canvas.drawPath(value, paint)
        }
    }

}