package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.chulan.newtestproject.util.Day
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
        /**
         * 仪表盘离 View 边界padding ( 避免绘制出来有被裁掉一部分)
         */
        var PADDING = 10f.dp2px()

        /**
         * 仪表盘 扫过角度大小
         */
        const val PANEL_ANGLE = 360f

        /**
         * 时针 刻度描述
         */
        val HOUR_POINT_BLOCK = ScaleAndPointerDescribe(2f.dp2px(), 10f.dp2px(), 12)

        /**
         * 分针 刻度描述
         */
        val MINUTE_POINT_BLOCK = ScaleAndPointerDescribe(1f.dp2px(), 5f.dp2px(), 60)

        /**
         * 钟的起点度数为 270f (负的y轴)
         */
        const val START_ANGLE = 270f
    }

    private var paint: Paint = Paint()

    var dash = Path()
    lateinit var dashEffect: PathDashPathEffect

    /**
     * 长指针（分钟）长度
     */
    var minuteDash = Path()
    lateinit var minuteDashEffect: PathDashPathEffect

    var path = Path()

    var radius: Float = 0f
        set(value) {
            field = value
            HOUR_POINT_BLOCK.pointerLength = field * 0.75f
            MINUTE_POINT_BLOCK.pointerLength = field * 0.5f
        }
    var centerCyclePoint = PointF()

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f.dp2px()
            color = Color.BLACK
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
        path.addArc(rect, START_ANGLE, PANEL_ANGLE)

        createHourDash()
        createMinuteDash()
    }

    /**
     * 创建 时针 刻度 dash
     */
    private fun createHourDash() {
        dash.addRect(RectF().apply {
            left = 0f
            top = 0f
            right = HOUR_POINT_BLOCK.width
            bottom = HOUR_POINT_BLOCK.height
        }, Path.Direction.CW)
        val pathMeasure = PathMeasure(path, false)
        // (pathMeasure.length - HOUR_POINT_BLOCK.width) / 20 => 空隙20 , 刻齿21  （ - HOUR_POINT_BLOCK.width 减去一个 刻齿，避免最后一个不够空间画）
//        dashEffect = PathDashPathEffect(dash, (pathMeasure.length - HOUR_POINT_BLOCK.width) / HOUR_POINT_BLOCK.scaleNum, 0f, PathDashPathEffect.Style.ROTATE)
        dashEffect = PathDashPathEffect(dash, (pathMeasure.length) / HOUR_POINT_BLOCK.scaleNum, 0f, PathDashPathEffect.Style.ROTATE)

    }

    /**
     * 创建 分针 刻度 dash
     */
    private fun createMinuteDash() {
        minuteDash.addRect(RectF().apply {
            left = 0f
            top = 0f
            right = MINUTE_POINT_BLOCK.width
            bottom = MINUTE_POINT_BLOCK.height
        }, Path.Direction.CW)
        val pathMeasure = PathMeasure(path, false)
        minuteDashEffect = PathDashPathEffect(minuteDash, (pathMeasure.length) / MINUTE_POINT_BLOCK.scaleNum, 0f, PathDashPathEffect.Style.ROTATE)

    }

    val day = Day()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 画小时刻度
        paint.pathEffect = dashEffect
        canvas.drawPath(path, paint)

        // 画分钟刻度
        paint.pathEffect = minuteDashEffect
        canvas.drawPath(path, paint)

        // 画圈
        paint.pathEffect = null
        canvas.drawPath(path, paint)

        day.updateTime(System.currentTimeMillis())

        // 画 小时 指针
        drawHourPointer(canvas)

        // 画 分钟 指针
        drawMinutePointer(canvas)
    }

    private fun drawHourPointer(canvas: Canvas) {
        val hourPointerRadian = Math.toRadians(getHourMarkAngle(day.hour).toDouble())
        canvas.drawLine(
                centerCyclePoint.x,
                centerCyclePoint.y,
                // 圆心 x起点 + 指针的 x 长度
                centerCyclePoint.x + (Math.cos(hourPointerRadian) * MINUTE_POINT_BLOCK.pointerLength).toFloat(),
                // 圆心 y起点 + 指针的 y 长度
                centerCyclePoint.y + (Math.sin(hourPointerRadian) * MINUTE_POINT_BLOCK.pointerLength).toFloat(),
                paint
        )
    }

    private fun drawMinutePointer(canvas: Canvas) {
        val minutePointerRadian = Math.toRadians(getMinuteMarkAngle(day.minute).toDouble())
        canvas.drawLine(
                centerCyclePoint.x,
                centerCyclePoint.y,
                // 圆心 x起点 + 指针的 x 长度
                centerCyclePoint.x + (Math.cos(minutePointerRadian) * HOUR_POINT_BLOCK.pointerLength).toFloat(),
                // 圆心 y起点 + 指针的 y 长度
                centerCyclePoint.y + (Math.sin(minutePointerRadian) * HOUR_POINT_BLOCK.pointerLength).toFloat(),
                paint
        )
    }

    /**
     * 获得时针刻度度数
     */
    private fun getHourMarkAngle(mark: Int): Float {
        return START_ANGLE + PANEL_ANGLE / HOUR_POINT_BLOCK.scaleNum * mark
    }

    /**
     * 获得分针刻度度数
     */
    private fun getMinuteMarkAngle(mark: Int): Float {
        return START_ANGLE + PANEL_ANGLE / MINUTE_POINT_BLOCK.scaleNum * mark
    }

}

/**
 * @param width 刻齿 宽度
 * @param height 刻齿 长度
 * @param scaleNum 刻度 数量 （环形如钟表结构，刻度间隔数 == 刻齿数）
 * @param pointerLength 指针长度
 */
data class ScaleAndPointerDescribe(val width: Float, val height: Float, val scaleNum: Int, var pointerLength: Float = 0f)