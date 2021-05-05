package com.chulan.newtestproject.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.decodeSampledBitmapFromResource
import com.chulan.newtestproject.util.noOpDelegate
import java.lang.Math.abs

class ScalableImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var paint: Paint = Paint()
    private val bitmap by lazy {
        decodeSampledBitmapFromResource(resources, R.mipmap.avatar, width, height)!!
    }

    /**
     * 短的一边靠到底，另一边超出屏幕
     */
    private var bigScale = 0f

    /**
     * 挑选一边靠到底，另一边不超过
     */
    private var smallScale = 0f

    private var isBig = false

    var frascation = 0f
        set(value) {
            field = value
            invalidate()
        }

    var offsetX = 0f
    var offsetY = 0f
    val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            if (isBig) {
                // 控制最远不能偏移到边界之外
                val wantOffset = offsetX - distanceX
                if (wantOffset > 0) {
                    offsetX = Math.min((bitmap.width * bigScale - width) / 2f, wantOffset)
                } else {
                    offsetX = Math.max(-(bitmap.width * bigScale - width) / 2f, wantOffset)
                }

                val wantOffsetY = offsetY - distanceY
                if (wantOffsetY > 0) {
                    offsetY = Math.min((bitmap.height * bigScale - height) / 2f, wantOffsetY)
                } else {
                    offsetY = Math.max((bitmap.height * bigScale - height) / 2f, wantOffsetY)
                }


                invalidate()
            }
            return true
        }
    })

    init {
        gestureDetector.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                isBig = !isBig
                if (isBig) {
                    animator.start()
                } else {
                    animator.reverse()
                }
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                return true
            }
        })
    }

    private val animator = ObjectAnimator.ofFloat(this, "frascation", 0f, 1f)


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (bitmap.width / bitmap.height > width / height) {
            // 宽更容易接近边，故 smallScale是 放大宽
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat()
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        // 注意是要先 scale 放大后平移，所以要 “反着写”
        if (!isBig) {
            offsetY = 0f
            offsetX = 0f
        }
        canvas.translate(offsetX, offsetY)

        val x = (width - bitmapWidth) / 2f
        val y = (height - bitmapHeight) / 2f

        val scale = smallScale + ((bigScale - smallScale) * frascation)
        canvas.scale(scale, scale, width / 2f, height / 2f)
        // 绘制到View中间
        canvas.drawBitmap(bitmap, x, y, paint)
    }
}