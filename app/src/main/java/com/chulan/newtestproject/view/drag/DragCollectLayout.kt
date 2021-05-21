package com.chulan.newtestproject.view.drag

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent.ACTION_DROP
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.chulan.newtestproject.R
import com.chulan.newtestproject.ext.toast

/**
 * Created by wuzixuan on 2021/5/19
 * 使用 DragListener ：拖拽时出现透明“像素”View
 */
class DragCollectLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnLongClickListener {

    private lateinit var tvContent: TextView
    private lateinit var vRect: View

    val onDragListener by lazy {
        OnDragListener { v, event ->
            // event.localState 整个事件序列都能取到值
//            event.localState.toast()
            when (event.action) {
                ACTION_DROP -> {
                    if (v.id == R.id.targetLayout) {
                        if (event.clipData != null && event.clipData.itemCount > 0) {
                            event.clipData.getItemAt(0).text.toast()
                        }
                    }

                }
            }
            true
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvContent = findViewById<TextView>(R.id.tvContent)
        tvContent.setOnLongClickListener(this)
        vRect = findViewById(R.id.vRect)
        vRect.setOnLongClickListener(this)
        // 想要监听界面拖拽事件的 (同 TouchEvent 分发机制)
        findViewById<View>(R.id.targetLayout).setOnDragListener(onDragListener)
    }

    override fun onLongClick(v: View): Boolean {
        ViewCompat.startDragAndDrop(
            v, ClipData.newPlainText("name", v.contentDescription),
            DragShadowBuilder(v), 10, 0
        )
        return true
    }
}