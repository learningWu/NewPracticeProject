package com.chulan.newtestproject.util

import android.content.res.Resources
import android.util.TypedValue

fun Float.dp2px(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)