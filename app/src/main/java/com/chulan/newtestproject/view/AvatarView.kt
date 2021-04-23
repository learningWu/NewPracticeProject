package com.chulan.newtestproject.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.decodeSampledBitmapFromResource
import com.chulan.newtestproject.util.dp2px

/**
 * @author wzx
 * 圆形头像 View
 * 训练 Xfermode SRC_IN 相交部分显示（貌似下层渲染的如果没相交还会显示，上层只显示相交）
 */
class AvatarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        var PADDING = 0f.dp2px()
    }

    private var paint: Paint = Paint()

    private var saveArea: RectF = RectF()

    private val xfermode by lazy {
        PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        saveArea.let {
            it.left = PADDING
            it.top = PADDING
            it.right = PADDING + width
            it.bottom = PADDING + height
        }
    }

    override fun onDraw(canvas: Canvas) {
        // 离屏缓冲
        val bitmap = getBitmap()
        val saveLayer = canvas.saveLayer(saveArea, paint)
        val bitmapWidth = bitmap?.width?.toFloat() ?: 0f
        val bitmapHeight = bitmap?.height?.toFloat() ?: 0f
        val radius = Math.min(bitmapWidth, bitmapHeight) / 2
        canvas.drawCircle(bitmapWidth / 2, bitmapHeight / 2, radius, paint)
        paint.xfermode = xfermode
        bitmap?.let { canvas.drawBitmap(it, PADDING, PADDING, paint) }
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)

    }

    private fun getBitmap() = decodeSampledBitmapFromResource(resources, R.mipmap.avatar, width, height)

}