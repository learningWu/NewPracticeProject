package com.chulan.newtestproject.view.drag

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.EDGE_LEFT
import androidx.customview.widget.ViewDragHelper.EDGE_TOP

/**
 * Created by wuzixuan on 2021/5/21
 * 其实就是不断改变拖拽 view 的layout位置
 */
class DragUpDownLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var mCurrentView: View

    private val captureViewOriginLeftTopPoint = PointF()

    private val callback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        /**
         * 是否拽起这个View
         */
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            mCurrentView = child
            return true
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            captureViewOriginLeftTopPoint.x = capturedChild.x
            captureViewOriginLeftTopPoint.y = capturedChild.y
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
            // 只能纵向滑动
//            return mCurrentView.left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            // 回弹 settleCapturedViewAt
            dragHelper.settleCapturedViewAt(
                captureViewOriginLeftTopPoint.x.toInt(),
                captureViewOriginLeftTopPoint.y.toInt()
            )
            invalidate()
        }

        /**
         * 边缘触发，父布局和子View共同的边缘（类 DrawLayout）
         */
        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)
            mCurrentView = getChildAt(childCount-1)
            dragHelper.captureChildView(getChildAt(childCount-1),pointerId)
        }

        /**
         * 解决 子 View 有响应事件（如点击按钮）时不可滑动
         */
        override fun getViewHorizontalDragRange(child: View): Int {
            // 大于等于 1 时可滑动
            return 1
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 1
        }
    }
    val dragHelper: ViewDragHelper = ViewDragHelper.create(this, callback).apply {
        setEdgeTrackingEnabled(EDGE_LEFT or EDGE_TOP)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }
}