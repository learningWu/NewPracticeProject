package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.decodeSampledBitmapFromResource
import com.chulan.newtestproject.util.dp2px

/**
 * Created by wuzixuan on 2021/4/25
 */
class FlipView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    val topRectF = RectF()
    val bottomRectF = RectF()
    val camera = Camera()

    init {
        paint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        camera.rotateX(10f.dp2px())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = getBitmap()!!
        bitmap.let {
            topRectF.let { i ->
                i.left = 0f
                i.top = 0f
                i.right = it.width.toFloat()
                i.bottom = it.height.toFloat() / 2
            }
            bottomRectF.let { i ->
                i.left = 0f
                i.top = it.height.toFloat() / 2
                i.right = it.width.toFloat()
                i.bottom = it.height.toFloat()
            }

//
////            // 绘制上半部分
            canvas.save()
            canvas.clipRect(topRectF)
            canvas.drawBitmap(it, 0f, 0f, paint)
            canvas.restore()

            // 绘制下半部分
            // 反着写
            // 将画布移到 camera 正下方
            canvas.save()
            canvas.translate(it.width / 2f, it.height / 2f)
            camera.applyToCanvas(canvas)
            canvas.translate(-it.width / 2f, -it.height / 2f)
            canvas.clipRect(bottomRectF)
            canvas.drawBitmap(it, 0f, 0f, paint)
            canvas.restore()
        }
    }

    private fun getBitmap() = decodeSampledBitmapFromResource(resources, R.mipmap.avatar, width, height)
}