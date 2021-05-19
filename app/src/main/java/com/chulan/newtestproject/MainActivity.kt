package com.chulan.newtestproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chulan.newtestproject.activity.*
import com.chulan.newtestproject.ext.startActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }

    @Composable
    private fun Content() = Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .width(IntrinsicSize.Max)
            .verticalScroll(rememberScrollState())
    ) {
        ActionButton(
            getString(R.string.avatar_text),
        ) {
            startActivity<AvatarActivity>()
        }

        ActionButton(
            getString(R.string.clock_text),
        ) {
            startActivity<ClockActivity>()
        }

        ActionButton(
            getString(R.string.tag_layout_text),
        ) {
            startActivity<TagLayoutActivity>()
        }

        ActionButton(
            getString(R.string.flip_book),
        ) {
            startActivity<FlipActivity>()
        }

        ActionButton(
            getString(R.string.scalable_image),
        ) {
            startActivity<ScalableImageViewActivity>()
        }

        ActionButton(getString(R.string.multi_pointer_control_image)) {
            startActivity<MultiPointerControlActivity>()
        }

        ActionButton(getString(R.string.view_pager)) {
            startActivity<ViewPagerActivity>()
        }

        ActionButton(getString(R.string.drag_layout)) {
            startActivity<DragLayoutActivity>()
        }
    }

    @Composable
    private fun ActionButton(content: String, onClick: () -> Unit) = TextButton(
        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xffffffff)),
        onClick = onClick,
        modifier = Modifier
            .padding(10.dp)
            .wrapContentHeight()
            .fillMaxWidth(0.8f)
            .background(Color(0x81ff6600))
            .clip(shape = RoundedCornerShape(8.dp)),
        content = { Text(content) }
    )
}