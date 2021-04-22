package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.chulan.newtestproject.util.dp2px


/**
 * 仪表盘view (有刻度，指针的仪表盘)
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
        val DASH_WIDTH = 2f.dp2px()
        val DASH_HEIGHT = 10f.dp2px()
    }

    private var paint: Paint = Paint()
    var pointerLength = 0f
    var path = Path()
    var dash = Path()
    lateinit var dashEffect: PathDashPathEffect
    var radius: Float = 0f
        set(value) {
            field = value
            pointerLength = field * 0.75f
        }
    var centerCyclePoint = PointF()

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
        centerCyclePoint.x = width / 2f
        centerCyclePoint.y = height / 2f
        radius = (rect.right - rect.left) / 2
        path.addArc(rect, 270 - ANGLE / 2, ANGLE)
        dash.addRect(RectF().apply {
            left = 0f
            top = 0f
            right = DASH_WIDTH
            bottom = DASH_HEIGHT
        }, Path.Direction.CW)
        val pathMeasure = PathMeasure(path, false)
        // (pathMeasure.length - DASH_WIDTH) / 20 => 空隙20 , 刻齿21  （ - DASH_WIDTH 减去一个 刻齿，避免最后一个不够空间画）
        dashEffect = PathDashPathEffect(dash, (pathMeasure.length - DASH_WIDTH) / 20, 0f, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 画刻度
        paint.pathEffect = dashEffect
        canvas.drawPath(path, paint)
        // 画圈
        paint.pathEffect = null
        canvas.drawPath(path, paint)
        // 画指针
        val radian = Math.toRadians(getMarkAngle(3).toDouble())
        canvas.drawLine(
                centerCyclePoint.x,
                centerCyclePoint.y,
                // 圆心 x起点 + 指针的 x 长度
                centerCyclePoint.x + (Math.cos(radian) * pointerLength).toFloat(),
                // 圆心 y起点 + 指针的 y 长度
                centerCyclePoint.y + (Math.sin(radian) * pointerLength).toFloat(),
                paint
        )
    }

    /**
     * 获得刻度度数
     */
    fun getMarkAngle(mark: Int): Float {
        return 270 - ANGLE / 2f + ANGLE / 20f * mark
    }
}