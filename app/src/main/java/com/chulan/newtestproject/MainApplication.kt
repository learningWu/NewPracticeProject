package com.chulan.newtestproject

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    companion object {
        lateinit var sCtx: Context
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        sCtx = base
    }
}