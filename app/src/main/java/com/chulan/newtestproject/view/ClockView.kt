package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.chulan.newtestproject.util.Day
import com.chulan.newtestproject.util.dp2px


/**
 * 钟表 View
 * draw 圆弧
 * dashEffect 使用
 * 角度计算
 */
class ClockView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        /**
         * 仪表盘离 View 边界padding ( 避免绘制出来有被裁掉一部分)
         */
        var PADDING = 30f.dp2px()

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
         * 秒针 刻度描述
         */
        val SECOND_POINT_BLOCK = ScaleAndPointerDescribe(0f, 0f, 60)

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
            SECOND_POINT_BLOCK.pointerLength = field * 0.8f
            MINUTE_POINT_BLOCK.pointerLength = field * 0.65f
            HOUR_POINT_BLOCK.pointerLength = field * 0.5f
        }
    var centerCyclePoint = PointF()

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f.dp2px()
            color = Color.BLACK
            isAntiAlias = true
            textSize = 14f.dp2px()
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

        // 画时间文字
        drawTimeText(canvas)

        day.updateTime(System.currentTimeMillis())

        // 画 小时 指针
        drawHourPointer(canvas)

        // 画 分钟 指针
        drawMinutePointer(canvas)

        // 画 秒针
        drawSecondPointer(canvas)

        postInvalidateDelayed(1000)
    }

    private fun drawTimeText(canvas: Canvas) {
        val offset = 10f.dp2px()
        for (i in 1..12){
            // TODO(wzx) : 需要将文字平移至中心
            val hourPointerRadian = Math.toRadians(getMarkAngleFromMarkAndScaleNum(i, HOUR_POINT_BLOCK.scaleNum).toDouble())
            canvas.drawText("$i",
                    centerCyclePoint.x + (Math.cos(hourPointerRadian) * (radius + offset)).toFloat(),
                    centerCyclePoint.y + (Math.sin(hourPointerRadian) * (radius + offset)).toFloat(),
                    paint
            )
        }
    }

    private fun drawHourPointer(canvas: Canvas) {
        val offset = (PANEL_ANGLE / HOUR_POINT_BLOCK.scaleNum) * (day.minute.toFloat() / 60)
        val hourPointerRadian = Math.toRadians((getMarkAngleFromMarkAndScaleNum(day.hour, HOUR_POINT_BLOCK.scaleNum) + offset).toDouble())
        canvas.drawLine(
                centerCyclePoint.x,
                centerCyclePoint.y,
                // 圆心 x起点 + 指针的 x 长度
                centerCyclePoint.x + (Math.cos(hourPointerRadian) * HOUR_POINT_BLOCK.pointerLength).toFloat(),
                // 圆心 y起点 + 指针的 y 长度
                centerCyclePoint.y + (Math.sin(hourPointerRadian) * HOUR_POINT_BLOCK.pointerLength).toFloat(),
                paint
        )
    }

    private fun drawMinutePointer(canvas: Canvas) {
        // PANEL_ANGLE /  MINUTE_POINT_BLOCK.scaleNum 秒针的格子度数
        val offset = (PANEL_ANGLE / MINUTE_POINT_BLOCK.scaleNum) * (day.second.toFloat() / 60)
        val minutePointerRadian = Math.toRadians((getMarkAngleFromMarkAndScaleNum(day.minute, MINUTE_POINT_BLOCK.scaleNum) + offset).toDouble())
        canvas.drawLine(
                centerCyclePoint.x,
                centerCyclePoint.y,
                // 圆心 x起点 + 指针的 x 长度
                centerCyclePoint.x + (Math.cos(minutePointerRadian) * MINUTE_POINT_BLOCK.pointerLength).toFloat(),
                // 圆心 y起点 + 指针的 y 长度
                centerCyclePoint.y + (Math.sin(minutePointerRadian) * MINUTE_POINT_BLOCK.pointerLength).toFloat(),
                paint
        )
    }

    private fun drawSecondPointer(canvas: Canvas) {
        val secondPointerRadian = Math.toRadians(getMarkAngleFromMarkAndScaleNum(day.second, SECOND_POINT_BLOCK.scaleNum).toDouble())
        canvas.drawLine(
                centerCyclePoint.x,
                centerCyclePoint.y,
                // 圆心 x起点 + 指针的 x 长度
                centerCyclePoint.x + (Math.cos(secondPointerRadian) * SECOND_POINT_BLOCK.pointerLength).toFloat(),
                // 圆心 y起点 + 指针的 y 长度
                centerCyclePoint.y + (Math.sin(secondPointerRadian) * SECOND_POINT_BLOCK.pointerLength).toFloat(),
                paint
        )
    }

    /**
     * 获得指针从起始点到指定刻度的度数
     * @param mark 刻度
     * @param scaleNum 刻度总数
     */
    private fun getMarkAngleFromMarkAndScaleNum(mark: Int, scaleNum: Int): Float {
        return START_ANGLE + PANEL_ANGLE / scaleNum * mark
    }

}

/**
 * @param width 刻齿 宽度
 * @param height 刻齿 长度
 * @param scaleNum 刻度 数量 （环形如钟表结构，刻度间隔数 == 刻齿数）
 * @param pointerLength 指针长度
 */
data class ScaleAndPointerDescribe(val width: Float, val height: Float, val scaleNum: Int, var pointerLength: Float = 0f)