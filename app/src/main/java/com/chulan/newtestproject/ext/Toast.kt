package com.chulan.newtestproject.ext

import android.widget.Toast
import com.chulan.newtestproject.MainApplication

/**
 * Created by wuzixuan on 2021/5/19
 */
fun Any.toast(){
    Toast.makeText(MainApplication.sCtx, this.toString(), Toast.LENGTH_SHORT).show()
}