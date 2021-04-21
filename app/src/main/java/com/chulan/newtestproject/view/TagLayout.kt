package com.chulan.newtestproject.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class TagLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    data class Bound(var l: Int = 0, var t: Int = 0, var r: Int = 0, var b: Int = 0)

    private val boundList = arrayListOf<Bound>()
    private val bound = Bound()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 测量所有子 view。存储位置。
        var usedWidth = 0
        var usedHeight = 0
        var maxNeedWidth = 0
        var lineWidth = 0
        var lineHeight = 0
        for (i in 0 until childCount) {
            // 计算宽度
            getChildAt(i).apply {
                measureChildWithMargins(this, widthMeasureSpec, 0, heightMeasureSpec, 0)
                lineWidth += measuredWidth
                bound.l = usedWidth
                bound.t = lineHeight
                bound.r = usedWidth + measuredWidth
                bound.b = usedHeight + measuredHeight
                usedWidth += measuredWidth
                usedHeight = Math.max(usedHeight, measuredHeight)
            }
        }
        setMeasuredDimension(View.resolveSize(lineWidth, widthMeasureSpec), View.resolveSize(lineHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            getChildAt(i).apply {
                layout(boundList[i].l, boundList[i].t, boundList[i].r, boundList[i].b)
            }
        }
    }
}