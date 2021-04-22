package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.chulan.newtestproject.util.dp2px


/**
 * 仪表盘view
 * draw 圆弧
 * 虚线使用
 * 角度计算
 */
class PanelView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        var PADDING = 10f.dp2px()
        const val ANGLE = 270f
        var DASH_WIDTH = 2f.dp2px()
        var DASH_HEIGHT = 10f.dp2px()
    }

    private var paint: Paint = Paint()
    var path = Path()
    var dash = Path()
    lateinit var dashEffect :PathDashPathEffect
    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f.dp2px()
            color = Color.GREEN
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rect = RectF().apply {
            left = PADDING
            top = PADDING
            right = width.toFloat() - PADDING
            bottom = height.toFloat() - PADDING
        }
        path.addArc(rect, 270 - ANGLE / 2, ANGLE)
        dash.addRect(RectF().apply {
            left = 0f
            top = 0f
            right = DASH_WIDTH
            bottom = DASH_HEIGHT
        },Path.Direction.CW)
        dashEffect = PathDashPathEffect(dash, 20f, 0f, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 画刻度
        paint.pathEffect = dashEffect
        canvas.drawPath(path, paint)
        // 画圈
        paint.pathEffect = null
        canvas.drawPath(path, paint)
    }
}