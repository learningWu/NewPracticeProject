package com.chulan.newtestproject.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

/**
 * Created by wuzixuan on 2021/5/24
 * Compose 专项练习
 */
class ComposePracticeActivity : ComponentActivity() {

    /**
     * 通过点击按钮给 mutableStateListOf 类型 add 数据。驱动列表自动增加item
     */
    private val names = mutableStateListOf("哈哈哈", "哈哈哈", "哈哈哈")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
        val counterState = remember {
            mutableStateOf(0)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            NameList(names = names, Modifier.weight(1f))
            Counter(count = counterState.value) { newCount ->
                counterState.value = newCount
            }
        }
    }


    @Composable
    private fun Counter(count: Int, updateCount: (Int) -> Unit) {
        Button(
            onClick = {
                updateCount(count + 1)
                names.add("$count")
            }, colors = ButtonDefaults.buttonColors(
                backgroundColor = if (count > 5) Color.Green else Color.White
            )
        ) {
            Text(text = "I've been clicked $count times")
        }
    }

    @Composable
    private fun Greeting(name: String) {
        var isSelected by remember {
            mutableStateOf(false)
        }
        val backgroundColor by animateColorAsState(if (isSelected) Color.Red else Color.Transparent)

        Text(
            text = name, modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .defaultMinSize(40.dp)
                .background(color = backgroundColor)
                .clickable(onClick = { isSelected = !isSelected })
        )
    }

    @Composable
    private fun NameList(names: List<String>, modifier: Modifier = Modifier) {
        LazyColumn(modifier = modifier) {
            items(items = names) { name ->
                Greeting(name = name)
                Divider(color = Color.Black)
            }
        }
    }
}

/**
 * Text 的padding 基于
 */
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    // 我理解，一个子元素的父布局，其实是适合适合 layout （注意是小写）{}  Modifier.layout
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Preview
@Composable
fun TestSelfColumn() {
    val (text, setText) = remember { mutableStateOf("") }
    SelfColumn {
        Text(modifier = Modifier.weight(1f),text = "急急急")
        Text(text = "babybaby")
    }
}

/**
 * 自定义 column 布局
 */
@Composable
inline fun SelfColumn(
    modifier: Modifier = Modifier,
    content: @Composable SelfColumnScope.() -> Unit
) {
    Layout(
        modifier = modifier,
        content = { SelfColumnScope.content() }
    ) { measurables, constraints ->
        // 测量
        val placeables = measurables.map {
            it.measure(constraints = constraints)
        }
        var yPoi = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPoi)
                yPoi += placeable.height
            }
        }
    }
}

/**
 * 扩展函数 作用域控制
 */
interface SelfColumnScope {

    /**
     * 假装的 weight
     */
    fun Modifier.weight(
        weight: Float,
        fill: Boolean = true
    ): Modifier {
        return this
    }

    companion object : SelfColumnScope
}

/**
 * 网格布局
 */
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // 测量 -> 放置

        // Keep track of the width of each row
        val rowWidths = IntArray(rows)

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows)

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->

            // 测量每个 child
            val placeable = measurable.measure(constraints)

            // 记录每一行的宽高
            val row = index % rows
            // 累计一行的宽度
            rowWidths[row] += placeable.width
            // 记录一行的最大高度
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)
            placeable
        }
        // 将所有行中最大的宽作为 自己（ViewGroup）的宽
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // 将所有行中最大的宽作为 自己（ViewGroup）的宽
        // 需要在约束宽高范围内 coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))
        val height = rowHeights.sumBy { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // 计算每一行 "layout" 的位置
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        // 设置本"ViewGroup"的大小 layout(width, height)
        layout(width, height) {
            // 计算每个子 view 放置的 x 位置
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                // 将每个子view放置到 x,y
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                // 同一行的元素，x累计往后放
                rowX[row] += placeable.width
            }
        }

    }
}