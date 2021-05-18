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
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 测量所有子 view。存储位置。
        var usedWidth = 0
        var usedHeight = 0
        var lineWidth = 0
        var lineHeight = 0
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        for (i in 0 until childCount) {
            // 计算宽度
            getChildAt(i).apply {
                // 父布局测量规格 + 子布局 LayoutParam = 子布局的测量规格  =》应用 child.measure
                measureChildWithMargins(this, widthMeasureSpec, 0, heightMeasureSpec, 0)
//                val childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
//                        0, layoutParams.width)
//                val childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
//                        0, layoutParams.height)
//
//                this.measure(childWidthMeasureSpec, childHeightMeasureSpec)
                if (usedWidth + measuredWidth > maxWidth) {
                    // 超出一行宽度。切到下一行 (宽从头，高从已使用行高之下)
                    usedHeight = lineHeight
                    usedWidth = 0
                }
                val bound = Bound()
                // 将 margin 添加到 layout 位置
                bound.l = usedWidth + (layoutParams as MarginLayoutParams).leftMargin
                bound.t = usedHeight + (layoutParams as MarginLayoutParams).topMargin
                bound.r = usedWidth + (layoutParams as MarginLayoutParams).leftMargin + measuredWidth
                bound.b = usedHeight + (layoutParams as MarginLayoutParams).topMargin + measuredHeight
                usedWidth += (measuredWidth + (layoutParams as MarginLayoutParams).leftMargin
                        + (layoutParams as MarginLayoutParams).rightMargin)
                // 一直记录最大使用行宽（给自己这个ViewGroup设置宽高使用）
                lineWidth = Math.max(lineWidth, usedWidth + measuredWidth)
                lineHeight = Math.max(lineHeight, usedHeight
                        + (layoutParams as MarginLayoutParams).topMargin
                        + (layoutParams as MarginLayoutParams).bottomMargin
                        + measuredHeight)
                boundList.add(bound)
            }
        }
        // resolveSize符合父布局期望规格内，设置自己的计算值  宽：lineWidth   高： lineHeight
        // lineWidth，lineHeight 是最后自己只需要多少的宽高
        setMeasuredDimension(View.resolveSize(lineWidth, widthMeasureSpec), View.resolveSize(lineHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            getChildAt(i).apply {
                layout(boundList[i].l, boundList[i].t, boundList[i].r, boundList[i].b)
            }
        }
    }

    /**
     * 父布局给子View生成的 LayoutParams
     * （挺嚣张）
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        val params = MarginLayoutParams(context, attrs)
        return params
    }
}