package com.chulan.newtestproject.view

import android.animation.ObjectAnimator
import android.app.ActionBar
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import com.chulan.newtestproject.R
import com.chulan.newtestproject.util.decodeSampledBitmapFromResource

/**
 * Created by Admin on 2021/5/23
 * 嵌套滑动
 * 相同方向时，消费距离与父布局协商
 */
class NestedScalableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Runnable, NestedScrollingChild2 {
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

    private var overScale = 1f

    var currentScale = smallScale
        set(value) {
            if (value in smallScale.rangeTo(bigScale) && value != currentScale) {
                field = value
                invalidate()
            }
        }

    var offsetX = 0f
    var offsetY = 0f

    var doubleClickPointF = PointF()
    val gestureDetector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (isBig) {
                    // 控制最远不能偏移到边界之外

                    val wantOffset = offsetX - distanceX
                    if (wantOffset > 0) {
                        offsetX = Math.min((bitmap.width * bigScale - width) / 2f, wantOffset)
                    } else {
                        offsetX = Math.max(-(bitmap.width * bigScale - width) / 2f, wantOffset)
                    }
                    // y 方向还有未消耗的滑动
                    var unconsumed: Float = 0f
                    val wantOffsetY = offsetY - distanceY
                    if (wantOffsetY > 0) {
                        if (wantOffsetY > (bitmap.height * bigScale - height) / 2f) {
                            unconsumed = (bitmap.height * bigScale - height) / 2f - wantOffsetY
                        }
                        offsetY = Math.min((bitmap.height * bigScale - height) / 2f, wantOffsetY)
                    } else {
                        if (wantOffsetY < -(bitmap.height * bigScale - height) / 2f) {
                            unconsumed = -(bitmap.height * bigScale - height) / 2f - wantOffsetY
                        }
                        offsetY = Math.max(-(bitmap.height * bigScale - height) / 2f, wantOffsetY)
                    }
                    if (unconsumed != 0f) {
                        childNestedScrollingChildHelper.dispatchNestedScroll(0, 0, 0, unconsumed.toInt(), null)
                    } else {
                        invalidate()
                    }
                }
                return true
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                // 惯性滑动
                if (isBig) {
                    overScroller.fling(
                        offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                        (-(bitmap.width * bigScale - width) / 2f).toInt(),
                        ((bitmap.width * bigScale - width) / 2f).toInt(),
                        (-(bitmap.height * bigScale - height) / 2f).toInt(),
                        ((bitmap.height * bigScale - height) / 2f).toInt()
                    )
                    postOnAnimation(this@NestedScalableImageView)
                }
                return true
            }
        })

    val overScroller = OverScroller(context)

    val scaleGestureDetector =
        ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {
            private var initialScale = 0f
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                initialScale = currentScale
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                currentScale = initialScale * detector.scaleFactor
                // 返回 true 会更新 prev 值（怪异现象）
                return false
            }
        })

    private val childNestedScrollingChildHelper = NestedScrollingChildHelper(this)

    init {
        childNestedScrollingChildHelper.isNestedScrollingEnabled = true
        gestureDetector.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                doubleClickPointF.x = e.x
                doubleClickPointF.y = e.y
                isBig = !isBig

                if (isBig) {
                    // 将点击位置偏移回来( 点击位置为锚点放大图片的效果 )
                    offsetX = (e.x - width / 2) - (e.x - width / 2) * bigScale / smallScale
                    offsetY = (e.y - height / 2) - (e.y - height / 2) * bigScale / smallScale
                    invalidate()
                    animator.setFloatValues(currentScale, bigScale)
                    animator.start()
                } else {
                    animator.setFloatValues(currentScale, smallScale)
                    animator.start()
                }
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                return true
            }
        })
    }

    private val animator = ObjectAnimator.ofFloat(this, "currentScale", 0f, 1f)


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 优先双指放大手势
        var res = scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            // 其次单击双击滑动等手势
            res = gestureDetector.onTouchEvent(event)
            childNestedScrollingChildHelper.startNestedScroll(SCROLL_AXIS_VERTICAL)
        }
        return res
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (bitmap.width / bitmap.height > width / height) {
            // 宽更容易接近边，故 smallScale是 放大宽
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat() + overScale
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat() + overScale
        }
        currentScale = smallScale
    }

    override fun onDraw(canvas: Canvas) {
        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        // 注意是要先 scale 放大后平移，所以要 “反着写”  (offset 也跟随 动画frascation 就会流畅)
        val fracsation = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * fracsation, offsetY * fracsation)

        val x = (width - bitmapWidth) / 2f
        val y = (height - bitmapHeight) / 2f

        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        // 绘制到View中间
        canvas.drawBitmap(bitmap, x, y, paint)
    }

    override fun run() {
        // 循环执行 fling 动画
        // 滑动未结束时 computeScrollOffset -> true
        if (overScroller.computeScrollOffset()) {
            offsetX = overScroller.currX.toFloat()
            offsetY = overScroller.currY.toFloat()
            invalidate()
            postOnAnimation(this)
        }
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return childNestedScrollingChildHelper.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        childNestedScrollingChildHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return childNestedScrollingChildHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return childNestedScrollingChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return childNestedScrollingChildHelper.dispatchNestedPreScroll(
            dx,
            dy,
            consumed,
            offsetInWindow,
            type
        )
    }
}