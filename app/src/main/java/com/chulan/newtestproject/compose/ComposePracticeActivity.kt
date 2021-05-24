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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chulan.newtestproject.activity.*
import com.chulan.newtestproject.ext.startActivity

/**
 * Created by wuzixuan on 2021/5/24
 * Compose 专项练习
 */
class ComposePracticeActivity : ComponentActivity() {

    /**
     * 通过点击按钮给 mutableStateListOf 类型 add 数据。驱动列表自动增加item
     */
    private val names = mutableStateListOf("哈哈哈","哈哈哈","哈哈哈")

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