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
 * 训练 Xfermode
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
        val saveLayer = canvas.saveLayer(saveArea, paint)
        canvas.drawOval(saveArea,paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        getBitmap()?.let { canvas.drawBitmap(it, PADDING, PADDING, paint) }
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }

    private fun getBitmap() = decodeSampledBitmapFromResource(resources, R.mipmap.icon_teacher, width, height)

}